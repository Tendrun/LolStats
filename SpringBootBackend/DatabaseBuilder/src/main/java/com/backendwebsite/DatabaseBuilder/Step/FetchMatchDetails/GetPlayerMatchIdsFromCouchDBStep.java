package com.backendwebsite.DatabaseBuilder.Step.FetchMatchDetails;

import com.backendwebsite.DatabaseBuilder.Client.CouchDBClient;
import com.backendwebsite.DatabaseBuilder.Context.FetchMatchDetailsContext;
import com.backendwebsite.DatabaseBuilder.Step.IStep;
import com.backendwebsite.DatabaseBuilder.Step.Log.StepLog;
import com.backendwebsite.DatabaseBuilder.Step.StepsOrder;
import com.backendwebsite.DatabaseBuilder.Util.LogFormatter;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class GetPlayerMatchIdsFromCouchDBStep implements IStep<FetchMatchDetailsContext> {
    private static final Logger logger = LoggerFactory.getLogger(GetPlayerMatchIdsFromCouchDBStep.class);

    private final CouchDBClient couchDBClient;

    public GetPlayerMatchIdsFromCouchDBStep(CouchDBClient couchDBClient){
        this.couchDBClient = couchDBClient;
    }

    @Override
    public void execute(FetchMatchDetailsContext context) {
        long startTime = System.currentTimeMillis();
        try {
            String urn = "/matches/_find";
            String body = """
            {
              "selector": {},
              "fields": [
                "matchIds"
              ],
              "limit": %d
            }
            """.formatted(context.playerMatchLimit);

            CouchDBClient.Response response = couchDBClient.sendPost(urn, body);

            JsonNode respBody = response.body();
            JsonNode docs = respBody != null ? respBody.get("docs") : null;

            if (docs != null && docs.isArray()) {
                int fetchedCount = 0;
                for (JsonNode doc : docs) {
                    JsonNode matchIdsNode = doc != null ? doc.get("matchIds") : null;
                    if (matchIdsNode != null && matchIdsNode.isArray()) {
                        for (JsonNode idNode : matchIdsNode) {
                            String matchId = idNode.asText();
                            context.matchIds.add(matchId);
                            fetchedCount++;
                        }
                    }
                }

                String summary = String.format("Fetched %d match IDs", fetchedCount);
                context.logs.computeIfAbsent(getClass().getSimpleName(), k -> new ArrayList<>())
                        .add(new StepLog(response.status(), this.getClass().getSimpleName(),
                                summary, System.currentTimeMillis() - startTime));

                logger.info(LogFormatter.formatStepLog(getClass().getSimpleName(), response.status(),
                        summary, System.currentTimeMillis() - startTime));
            } else {
                context.logs.computeIfAbsent(getClass().getSimpleName(), k -> new ArrayList<>())
                        .add(new StepLog(StepsOrder.RequestStatus.FAILED, this.getClass().getSimpleName(),
                                "Error: CouchDB response missing 'docs' array. Response body: " + respBody,
                                System.currentTimeMillis() - startTime));
                logger.warn(LogFormatter.formatStepLog(getClass().getSimpleName(), StepsOrder.RequestStatus.FAILED,
                        "CouchDB response missing 'docs' array", System.currentTimeMillis() - startTime));
            }

        } catch (Exception e) {
            context.logs.computeIfAbsent(getClass().getSimpleName(), k -> new ArrayList<>())
                    .add(new StepLog(StepsOrder.RequestStatus.FAILED,
                            this.getClass().getSimpleName(), "Exception: " + e.getMessage(),
                            System.currentTimeMillis() - startTime));
            logger.error(LogFormatter.formatStepLog(getClass().getSimpleName(), StepsOrder.RequestStatus.FAILED,
                    "Exception while fetching player match ids from CouchDB", System.currentTimeMillis() - startTime), e);
        }
    }
}