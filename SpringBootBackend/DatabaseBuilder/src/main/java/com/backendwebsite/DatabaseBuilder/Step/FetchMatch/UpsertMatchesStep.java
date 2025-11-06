package com.backendwebsite.DatabaseBuilder.Step.FetchMatch;

import com.backendwebsite.DatabaseBuilder.Client.CouchDBClient;
import com.backendwebsite.DatabaseBuilder.Context.FetchMatchesContext;
import com.backendwebsite.DatabaseBuilder.Domain.Match.PlayerMatches;
import com.backendwebsite.DatabaseBuilder.Step.IStep;
import com.backendwebsite.DatabaseBuilder.Step.Log.StepLog;
import com.backendwebsite.DatabaseBuilder.Step.StepsOrder;
import com.backendwebsite.DatabaseBuilder.Util.LogFormatter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.ArrayList;

@Component
public class UpsertMatchesStep implements IStep<FetchMatchesContext> {
    CouchDBClient couchDBClient;
    ObjectMapper mapper;
    private static final Logger logger = LoggerFactory.getLogger(UpsertMatchesStep.class);

    public UpsertMatchesStep(CouchDBClient couchDBClient, ObjectMapper mapper) {
        this.mapper = mapper;
        this.couchDBClient = couchDBClient;
    }

    @Override
    public void execute(FetchMatchesContext context) {
        sendMatchesToCouchDB(context);
    }

    public void sendMatchesToCouchDB(FetchMatchesContext context) {
        long startTime = System.currentTimeMillis();
        try {
            ArrayNode docs = mapper.createArrayNode();

            for (PlayerMatches playerMatch : context.finalPlayerMatches) {
                ObjectNode doc = mapper.valueToTree(playerMatch);
                String rev = playerMatch._rev();
                if (rev != null && !rev.trim().isEmpty()) {
                    doc.put("_rev", rev);
                } else {
                    doc.remove("_rev");
                }
                docs.add(doc);
            }

            String json = mapper.writeValueAsString(Map.of("docs", docs));
            String urnCouchDB = "/matches/_bulk_docs";

            CouchDBClient.Response response = couchDBClient.sendPost(urnCouchDB, json);

            if (response.status() == StepsOrder.RequestStatus.FAILED) {
                java.util.List<String> failedIds = (response != null && response.failedIds() != null) ? response.failedIds() : new ArrayList<>();
                int total = context.finalPlayerMatches.size();
                int failedCount = failedIds.size();
                int succeededCount = Math.max(0, total - failedCount);

                String summary = String.format("total=%d succeeded=%d failed=%d", total, succeededCount, failedCount);
                String msg = String.format("Upsert summary: %s failedIds=%s", summary, failedIds);

                context.logs.computeIfAbsent(getClass().getSimpleName(), k -> new ArrayList<>())
                        .add(new StepLog(StepsOrder.RequestStatus.FAILED, this.getClass().getSimpleName(),
                                msg,
                                System.currentTimeMillis() - startTime));

                String responseBodyStr = (response != null && response.body() != null) ? response.body().toString() : "null";
                String formattedPrefix = LogFormatter.formatStepLog(getClass().getSimpleName(), StepsOrder.RequestStatus.FAILED,
                        "Upsert failed for matches " + msg, System.currentTimeMillis() - startTime);
                logger.error("{} responseBody={}", formattedPrefix, responseBodyStr);
                return;
            }

            context.logs.computeIfAbsent(getClass().getSimpleName(), k -> new ArrayList<>())
                    .add(new StepLog(response.status(), this.getClass().getSimpleName(),
                            response.message() + " - Upserted " + context.finalPlayerMatches.size() + " docs. Response body: " + response.body(),
                            System.currentTimeMillis() - startTime));

            logger.info(LogFormatter.formatStepLog(getClass().getSimpleName(), response.status(),
                    "Upserted " + context.finalPlayerMatches.size() + " Final player matches",
                    System.currentTimeMillis() - startTime));

        } catch (Exception e) {
            context.logs.computeIfAbsent(getClass().getSimpleName(), k -> new ArrayList<>())
                    .add(new StepLog(StepsOrder.RequestStatus.FAILED, this.getClass().getSimpleName(), "Exception: "
                            + e.getMessage(), System.currentTimeMillis() - startTime));
            logger.error(LogFormatter.formatStepLog(getClass().getSimpleName(), StepsOrder.RequestStatus.FAILED,
                    "Exception while sending matches to CouchDB", System.currentTimeMillis() - startTime), e);
        }
    }
}
