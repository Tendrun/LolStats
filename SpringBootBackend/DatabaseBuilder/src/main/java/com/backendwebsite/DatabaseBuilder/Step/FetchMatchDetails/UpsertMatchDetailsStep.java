package com.backendwebsite.DatabaseBuilder.Step.FetchMatchDetails;

import com.backendwebsite.DatabaseBuilder.Client.CouchDBClient;
import com.backendwebsite.DatabaseBuilder.Context.FetchMatchDetailsContext;
import com.backendwebsite.DatabaseBuilder.Step.IStep;
import com.backendwebsite.DatabaseBuilder.Step.Log.StepLog;
import com.backendwebsite.DatabaseBuilder.Step.StepsOrder;
import com.backendwebsite.DatabaseBuilder.Util.LogFormatter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Map;

@Component
public class UpsertMatchDetailsStep implements IStep<FetchMatchDetailsContext> {
    CouchDBClient couchDBClient;
    ObjectMapper mapper;
    private static final Logger logger = LoggerFactory.getLogger(UpsertMatchDetailsStep.class);

    public UpsertMatchDetailsStep(CouchDBClient couchDBClient, ObjectMapper mapper) {
        this.mapper = mapper;
        this.couchDBClient = couchDBClient;
    }

    @Override
    public void execute(FetchMatchDetailsContext context) {
        sendMatchDetailsToCouchDB(context);
    }

    public void sendMatchDetailsToCouchDB(FetchMatchDetailsContext context) {
        long startTime = System.currentTimeMillis();
        try {
            String json = mapper.writeValueAsString(Map.of("docs", context.finalMatchDetails));
            String urnCouchDB = "/matchdetails/_bulk_docs";

            CouchDBClient.Response response = couchDBClient.sendPost(urnCouchDB, json);

            if (response.status() == StepsOrder.RequestStatus.FAILED) {
                java.util.List<String> failedIds = response.failedIds() != null ? response.failedIds() : new ArrayList<>();
                int total = context.finalMatchDetails.size();
                int failedCount = failedIds.size();
                int succeededCount = Math.max(0, total - failedCount);

                String summary = String.format("total=%d succeeded=%d failed=%d", total, succeededCount, failedCount);
                String msg = String.format("Upsert summary: %s failedIds=%s", summary, failedIds);

                context.logs.computeIfAbsent(getClass().getSimpleName(), k -> new ArrayList<>())
                        .add(new StepLog(StepsOrder.RequestStatus.FAILED, this.getClass().getSimpleName(),
                                msg,
                                System.currentTimeMillis() - startTime));

                String responseBodyStr = response.body() != null ? response.body().toString() : "null";
                String formattedPrefix = LogFormatter.formatStepLog(getClass().getSimpleName(), StepsOrder.RequestStatus.FAILED,
                        "Upsert failed for match details " + msg, System.currentTimeMillis() - startTime);
                logger.error("{} responseBody={}", formattedPrefix, responseBodyStr);
                return;
            }

            context.logs.computeIfAbsent(getClass().getSimpleName(), k -> new ArrayList<>())
                    .add(new StepLog(response.status(), this.getClass().getSimpleName(),
                            response.message() + " - Upserted " + context.finalMatchDetails.size() + " docs. Response body: "
                                    + response.body(),
                            System.currentTimeMillis() - startTime));

            logger.info(LogFormatter.formatStepLog(getClass().getSimpleName(), response.status(),
                    "Upserted " + context.finalMatchDetails.size() + " match details",
                    System.currentTimeMillis() - startTime));
        } catch (Exception e) {
            context.logs.computeIfAbsent(getClass().getSimpleName(), k -> new ArrayList<>())
                    .add(new StepLog(StepsOrder.RequestStatus.FAILED, this.getClass().getSimpleName(),
                            "Exception: "
                            + e.getMessage(), System.currentTimeMillis() - startTime));
            logger.error(LogFormatter.formatStepLog(getClass().getSimpleName(), StepsOrder.RequestStatus.FAILED,
                    "Exception while sending match details to CouchDB", System.currentTimeMillis() - startTime), e);
        }
    }
}
