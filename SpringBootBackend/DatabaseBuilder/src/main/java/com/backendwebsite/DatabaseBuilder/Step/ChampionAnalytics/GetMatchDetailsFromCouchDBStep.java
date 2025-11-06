package com.backendwebsite.DatabaseBuilder.Step.ChampionAnalytics;

import com.backendwebsite.DatabaseBuilder.Client.CouchDBClient;
import com.backendwebsite.DatabaseBuilder.Context.BuildChampionAnalyticsContext;
import com.backendwebsite.DatabaseBuilder.DTO.RiotApi.MatchDetails.MatchDTO;
import com.backendwebsite.DatabaseBuilder.Step.IStep;
import com.backendwebsite.DatabaseBuilder.Step.Log.StepLog;
import com.backendwebsite.DatabaseBuilder.Step.StepsOrder;
import com.backendwebsite.DatabaseBuilder.Util.LogFormatter;
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
                    logger.debug("Fetched match detail: {}", matchDetail._id);
                }

                int fetchedCount = docs.size();
                String msg = "Fetched " + fetchedCount + " match details from CouchDB";
                context.logs.computeIfAbsent(getClass().getSimpleName(), k -> new java.util.ArrayList<>())
                        .add(new StepLog(response.status(), this.getClass().getSimpleName(), msg,
                                System.currentTimeMillis() - startTime));
                logger.info(LogFormatter.formatStepLog(getClass().getSimpleName(), response.status(), msg,
                        System.currentTimeMillis() - startTime));
            } else {
                String msg = "CouchDB response missing 'docs' array. Response body: " + respBody;
                context.logs.computeIfAbsent(getClass().getSimpleName(), k -> new java.util.ArrayList<>())
                        .add(new StepLog(StepsOrder.RequestStatus.FAILED, this.getClass().getSimpleName(), msg,
                                System.currentTimeMillis() - startTime));
                logger.warn(LogFormatter.formatStepLog(getClass().getSimpleName(), StepsOrder.RequestStatus.FAILED, msg,
                        System.currentTimeMillis() - startTime));
            }
        } catch (Exception e) {
            String msg = "Exception while fetching match details from CouchDB: " + e.getMessage();
            context.logs.computeIfAbsent(getClass().getSimpleName(), k -> new java.util.ArrayList<>())
                    .add(new StepLog(StepsOrder.RequestStatus.FAILED, this.getClass().getSimpleName(), msg,
                            System.currentTimeMillis() - startTime));
            logger.error(LogFormatter.formatStepLog(getClass().getSimpleName(), StepsOrder.RequestStatus.FAILED, msg,
                    System.currentTimeMillis() - startTime), e);
        }
    }
}
