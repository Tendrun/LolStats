package com.backendwebsite.DatabaseBuilder.Step.FetchPlayers;

import com.backendwebsite.DatabaseBuilder.Client.CouchDBClient;
import com.backendwebsite.DatabaseBuilder.Context.FetchPlayersContext;
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
public class UpsertPlayersStep implements IStep<FetchPlayersContext> {
    private static final Logger logger = LoggerFactory.getLogger(UpsertPlayersStep.class);
    CouchDBClient couchDBClient;
    ObjectMapper mapper;

    public UpsertPlayersStep(CouchDBClient couchDBClient, ObjectMapper mapper) {
        this.mapper = mapper;
        this.couchDBClient = couchDBClient;
    }

    @Override
    public void execute(FetchPlayersContext context) {
        sendPlayerToCouchDB(context);
    }

    public void sendPlayerToCouchDB(FetchPlayersContext context) {
        long startTime = System.currentTimeMillis();
        try {
            String json = mapper.writeValueAsString(Map.of("docs", context.finalPlayers));
            String urnCouchDB = "/players/_bulk_docs";

            CouchDBClient.Response response = couchDBClient.sendPost(urnCouchDB, json);

            if (response.status() == StepsOrder.RequestStatus.FAILED) {
                String failMsg = response.message() + " - Upsert failed for " + context.finalPlayers.size() + " docs. Response body: " +
                        response.body();
                context.logs.computeIfAbsent(getClass().getSimpleName(), k -> new ArrayList<>())
                        .add(new StepLog(StepsOrder.RequestStatus.FAILED, this.getClass().getSimpleName(),
                                failMsg,
                                System.currentTimeMillis() - startTime, "count: " + context.finalPlayers.size()));

                logger.error(LogFormatter.formatStepLog(getClass().getSimpleName(), StepsOrder.RequestStatus.FAILED,
                        "Upsert failed for " + context.finalPlayers.size() + " players",
                        System.currentTimeMillis() - startTime)
                        + " responseBody=" + response.body());
                return;
            }

            context.logs.computeIfAbsent(getClass().getSimpleName(), k -> new ArrayList<>())
                    .add(new StepLog(response.status(), this.getClass().getSimpleName(),
                            response.message() + " - Upserted " + context.finalPlayers.size() +
                                    " docs. Response body: " + response.body(),
                            System.currentTimeMillis() - startTime, "count: " + context.finalPlayers.size()));

            logger.info(LogFormatter.formatStepLog(getClass().getSimpleName(), response.status(),
                    "Upserted " + context.finalPlayers.size() + " players", System.currentTimeMillis() - startTime));

        } catch (Exception e) {
            String exceptionMessage = "Exception while upserting players to CouchDB Exception: " + e.getMessage();

            context.logs.computeIfAbsent(getClass().getSimpleName(), k -> new ArrayList<>())
                    .add(new StepLog(StepsOrder.RequestStatus.FAILED, this.getClass().getSimpleName(),
                            exceptionMessage,
                            System.currentTimeMillis() - startTime, ""));

            logger.error(LogFormatter.formatStepLog(getClass().getSimpleName(),
                    StepsOrder.RequestStatus.FAILED,
                    exceptionMessage,
                    System.currentTimeMillis() - startTime), e);
        }
    }
}
