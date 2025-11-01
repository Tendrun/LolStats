package com.backendwebsite.DatabaseBuilder.Step.FetchMatch;

import com.backendwebsite.DatabaseBuilder.Client.CouchDBClient;
import com.backendwebsite.DatabaseBuilder.Context.FetchMatchesContext;
import com.backendwebsite.DatabaseBuilder.Domain.Match.PlayerMatches;
import com.backendwebsite.DatabaseBuilder.Step.IStep;
import com.backendwebsite.DatabaseBuilder.Step.Log.StepLog;
import com.backendwebsite.DatabaseBuilder.Step.StepsOrder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class GetMatchesFromCouchDBStep implements IStep<FetchMatchesContext> {
    private final CouchDBClient couchDBClient;
    private static final Logger logger = LoggerFactory.getLogger(GetMatchesFromCouchDBStep.class);

    private final ObjectMapper mapper;
    public GetMatchesFromCouchDBStep(ObjectMapper mapper, CouchDBClient couchDBClient) {
        this.mapper = mapper;
        this.couchDBClient = couchDBClient;
    }

    @Override
    public void execute(FetchMatchesContext context) {
        try {
            String idsJson = mapper.writeValueAsString(context.puuids);

            String urn = "/matches/_find";
            String body = """
            {
              "selector": {
                "puuid": {
                  "$in": %s
                }
              },
              "limit": 999999
            }
            """.formatted(idsJson);


            CouchDBClient.Response response = couchDBClient.sendPost(urn, body);
            JsonNode rows = response.body() != null ? response.body().get("docs") : null;

            if (rows != null && rows.isArray()) {
                for (JsonNode row : rows) {
                    PlayerMatches playerMatches = mapper.treeToValue(row, PlayerMatches.class);

                    // TO DO
                    context.existingMatches.put(playerMatches._id(), playerMatches);
                    context.logs.computeIfAbsent(getClass().getSimpleName(), k -> new ArrayList<>())
                            .add(new StepLog(response.status(), this.getClass().getSimpleName(),
                                    response.message() + " - Fetched player matches ID: " + playerMatches._id()
                                            + " Response body: " + response.body()));
                    logger.debug("Get = {}", playerMatches._id());
                }
            } else {
                context.logs.computeIfAbsent(getClass().getSimpleName(), k -> new ArrayList<>())
                        .add(new StepLog(StepsOrder.RequestStatus.FAILED, this.getClass().getSimpleName(),
                                "Error: CouchDB response missing 'rows' array" + " Response body: " + response.body()));
                logger.warn("CouchDB response missing 'rows' array)");
            }
        } catch (Exception e) {
            context.logs.computeIfAbsent(getClass().getSimpleName(), k -> new ArrayList<>())
                    .add(new StepLog(StepsOrder.RequestStatus.FAILED, this.getClass().getSimpleName(),
                            "Exception: " + e.getMessage()));
            logger.error("Exception while fetching players matches from CouchDB", e);
        }
    }
}
