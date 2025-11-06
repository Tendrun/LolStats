package com.backendwebsite.DatabaseBuilder.Step.FetchMatchDetails;

import com.backendwebsite.DatabaseBuilder.Client.CouchDBClient;
import com.backendwebsite.DatabaseBuilder.Context.FetchMatchDetailsContext;
import com.backendwebsite.DatabaseBuilder.Step.IStep;
import com.backendwebsite.DatabaseBuilder.Step.Log.StepLog;
import com.backendwebsite.DatabaseBuilder.Step.StepsOrder;
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
            logger.debug("Final match details collection class: {}", context.finalMatchDetails.getClass());
            context.finalMatchDetails.forEach(e -> logger.debug("Element class: {}", e.getClass()));

            String json = mapper.writeValueAsString(Map.of("docs", context.finalMatchDetails));
            String urnCouchDB = "/matchdetails/_bulk_docs";

            CouchDBClient.Response response = couchDBClient.sendPost(urnCouchDB, json);

            if (response != null && response.status() == StepsOrder.RequestStatus.SUCCESSFUL) {
                context.logs.computeIfAbsent(getClass().getSimpleName(), k -> new ArrayList<>())
                        .add(new StepLog(response.status(), this.getClass().getSimpleName(),
                                response.message() + " - Upserted " + context.finalMatchDetails.size() +
                                        " docs", System.currentTimeMillis() - startTime, ""));
                logger.info("Request sent to CouchDB, upserted {} docs. Response: {}", context.finalMatchDetails.size(),
                        response.message());
            } else {
                context.logs.computeIfAbsent(getClass().getSimpleName(), k -> new ArrayList<>())
                        .add(new StepLog(response != null ? response.status() : StepsOrder.RequestStatus.FAILED,
                                this.getClass().getSimpleName(),
                                "Failed to upsert match details to CouchDB. Response: " + (response != null ?
                                        response.message() : "null"), System.currentTimeMillis() - startTime, ""));
                logger.warn("Failed to upsert match details to CouchDB. Response: {}", response != null ?
                        response.message() : "null");
            }
        } catch (Exception e) {
            context.logs.computeIfAbsent(getClass().getSimpleName(), k -> new ArrayList<>())
                    .add(new StepLog(StepsOrder.RequestStatus.FAILED, this.getClass().getSimpleName(),
                            "Exception: "
                            + e.getMessage(), System.currentTimeMillis() - startTime, ""));
            logger.error("Exception while sending match details to CouchDB", e);
        }
    }
}
