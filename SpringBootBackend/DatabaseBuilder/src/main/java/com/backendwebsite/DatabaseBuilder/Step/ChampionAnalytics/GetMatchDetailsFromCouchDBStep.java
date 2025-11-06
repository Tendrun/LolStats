package com.backendwebsite.DatabaseBuilder.Step.ChampionAnalytics;

import com.backendwebsite.DatabaseBuilder.Client.CouchDBClient;
import com.backendwebsite.DatabaseBuilder.Context.BuildChampionAnalyticsContext;
import com.backendwebsite.DatabaseBuilder.DTO.RiotApi.MatchDetails.MatchDTO;
import com.backendwebsite.DatabaseBuilder.Step.IStep;
import com.backendwebsite.DatabaseBuilder.Step.Log.StepLog;
import com.backendwebsite.DatabaseBuilder.Step.StepsOrder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class GetMatchDetailsFromCouchDBStep implements IStep<BuildChampionAnalyticsContext> {
    private final CouchDBClient couchDBClient;
    private final ObjectMapper mapper;
    private static final Logger logger = LoggerFactory.getLogger(GetMatchDetailsFromCouchDBStep.class);

    public GetMatchDetailsFromCouchDBStep(ObjectMapper mapper, CouchDBClient couchDBClient) {
        this.mapper = mapper;
        this.couchDBClient = couchDBClient;
    }

    @Override
    public void execute(BuildChampionAnalyticsContext context) {
        long startTime = System.currentTimeMillis();
        try {
            String urn = "/matchdetails/_find";
            String body = """
            {
              "selector": {},
              "limit": %d
            }
            """.formatted(context.limitMatches);

            CouchDBClient.Response response = couchDBClient.sendPost(urn, body);

            JsonNode respBody = response != null ? response.body() : null;
            JsonNode docs = respBody != null ? respBody.get("docs") : null;

            if (docs != null && docs.isArray()) {
                for (JsonNode row : docs) {
                    MatchDTO matchDetail = mapper.treeToValue(row, MatchDTO.class);

                    context.matchDetails.add(matchDetail);
                    logger.debug("Get = {}", matchDetail._id);
                }

                context.logs.computeIfAbsent(getClass().getSimpleName(), k -> new java.util.ArrayList<>())
                        .add(new StepLog(response.status(), this.getClass().getSimpleName(), response.message() +
                                " Fetched " + docs.size() + " match details from CouchDB", System.currentTimeMillis() - startTime, ""));
                logger.info("Fetched {} match details from CouchDB", docs.size());
            } else {
                context.logs.computeIfAbsent(getClass().getSimpleName(), k -> new java.util.ArrayList<>())
                        .add(new StepLog(StepsOrder.RequestStatus.FAILED, this.getClass().getSimpleName(),
                                "CouchDB response missing 'docs' array" + " Response body: " + respBody, System.currentTimeMillis() - startTime, ""));
                logger.warn("CouchDB response missing 'docs' array for urn {} - body: {}", urn, respBody);
            }
        } catch (Exception e) {
            context.logs.computeIfAbsent(getClass().getSimpleName(), k -> new java.util.ArrayList<>())
                    .add(new StepLog(StepsOrder.RequestStatus.FAILED, this.getClass().getSimpleName(),
                            "Exception: " + e.getMessage(), System.currentTimeMillis() - startTime, ""));
            logger.error("Exception while fetching match details from CouchDB", e);
        }
    }
}
