package com.backendwebsite.DatabaseBuilder.Step.FetchMatch;

import com.backendwebsite.DatabaseBuilder.Client.CouchDBClient;
import com.backendwebsite.DatabaseBuilder.Context.FetchMatchesContext;
import com.backendwebsite.DatabaseBuilder.Domain.Match.PlayerMatches;
import com.backendwebsite.DatabaseBuilder.Step.IStep;
import com.backendwebsite.DatabaseBuilder.Step.Log.StepLog;
import com.backendwebsite.DatabaseBuilder.Step.StepsOrder;
import com.backendwebsite.DatabaseBuilder.Util.LogFormatter;
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
        long startTime = System.currentTimeMillis();
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
                     context.existingMatches.put(playerMatches._id(), playerMatches);
                     logger.debug("Loaded player matches: {}", playerMatches._id());
                 }
                int fetchedCount = rows.size();
                String msg = "Fetched " + fetchedCount + " player matches from CouchDB";
                 context.logs.computeIfAbsent(getClass().getSimpleName(), k -> new ArrayList<>())
                         .add(new StepLog(response.status(), this.getClass().getSimpleName(), msg,
                                 System.currentTimeMillis() - startTime));
                logger.info(LogFormatter.formatStepLog(getClass().getSimpleName(), response.status(), msg,
                        System.currentTimeMillis() - startTime));
             } else {
                String msg = "CouchDB response missing 'docs' array. Response body: " + rows;
                 context.logs.computeIfAbsent(getClass().getSimpleName(), k -> new ArrayList<>())
                         .add(new StepLog(StepsOrder.RequestStatus.FAILED, this.getClass().getSimpleName(),
                                 msg, System.currentTimeMillis() - startTime));
                logger.warn(LogFormatter.formatStepLog(getClass().getSimpleName(), StepsOrder.RequestStatus.FAILED, msg,
                        System.currentTimeMillis() - startTime));
            }
         } catch (Exception e) {
            String msg = "Exception while fetching player matches from CouchDB: " + e.getMessage();
             context.logs.computeIfAbsent(getClass().getSimpleName(), k -> new ArrayList<>())
                     .add(new StepLog(StepsOrder.RequestStatus.FAILED, this.getClass().getSimpleName(),
                             msg, System.currentTimeMillis() - startTime));
            logger.error(LogFormatter.formatStepLog(getClass().getSimpleName(), StepsOrder.RequestStatus.FAILED, msg,
                    System.currentTimeMillis() - startTime), e);
         }
     }
 }
