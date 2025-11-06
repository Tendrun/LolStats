package com.backendwebsite.DatabaseBuilder.Step.FetchPlayers;

import com.backendwebsite.DatabaseBuilder.Client.CouchDBClient;
import com.backendwebsite.DatabaseBuilder.Context.FetchPlayersContext;
import com.backendwebsite.DatabaseBuilder.Domain.Player.Player;
import com.backendwebsite.DatabaseBuilder.Step.IStep;
import com.backendwebsite.DatabaseBuilder.Step.Log.StepLog;
import com.backendwebsite.DatabaseBuilder.Step.StepsOrder;
import com.backendwebsite.DatabaseBuilder.Util.LogFormatter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

@Component
public class GetPlayersFromCouchDBStep implements IStep<FetchPlayersContext> {
    private final CouchDBClient couchDBClient;
    private final ObjectMapper mapper;
    private static final Logger logger = LoggerFactory.getLogger(GetPlayersFromCouchDBStep.class);
    public GetPlayersFromCouchDBStep(ObjectMapper mapper, CouchDBClient couchDBClient) {
        this.mapper = mapper;
        this.couchDBClient = couchDBClient;
    }

    @Override
    public void execute(FetchPlayersContext context) {
        long startTime = System.currentTimeMillis();
        try {
            String urn = "/players/_all_docs?include_docs=true";
            CouchDBClient.Response response = couchDBClient.sendGet(urn);

            JsonNode rows = response.body() != null ? response.body().get("rows") : null;
            if (rows != null && rows.isArray()) {
                for (JsonNode row : rows) {
                    JsonNode doc = row != null ? row.get("doc") : null;
                    if (doc == null || doc.isNull()) {
                        context.logs.computeIfAbsent(getClass().getSimpleName(), k -> new ArrayList<>())
                                .add(new StepLog(StepsOrder.RequestStatus.FAILED, this.getClass().getSimpleName(),
                                        "Row missing 'doc' field or doc is null",
                                        System.currentTimeMillis() - startTime));

                        logger.warn(LogFormatter.formatStepLog(getClass().getSimpleName(), StepsOrder.RequestStatus.FAILED,
                                "Row missing 'doc' field or doc is null",
                                System.currentTimeMillis() - startTime));
                        continue;
                    }

                    Player player = mapper.treeToValue(doc, Player.class);
                    context.existingPlayers.add(player);
                    context.logs.computeIfAbsent(getClass().getSimpleName(), k -> new ArrayList<>())
                            .add(new StepLog(StepsOrder.RequestStatus.SUCCESSFUL, this.getClass().getSimpleName(),
                                            "Loaded player from CouchDB",
                                    System.currentTimeMillis() - startTime));

                    logger.info("Loaded player puuid={}", player.puuid);
                }
            }
            else {
                context.logs.computeIfAbsent(getClass().getSimpleName(), k -> new ArrayList<>())
                        .add(new StepLog(StepsOrder.RequestStatus.FAILED, this.getClass().getSimpleName(),
                                "CouchDB response missing 'rows' array",
                                System.currentTimeMillis() - startTime));

                logger.warn(LogFormatter.formatStepLog(getClass().getSimpleName(), StepsOrder.RequestStatus.FAILED,
                        "CouchDB response missing 'rows' array",
                        System.currentTimeMillis() - startTime));
             }
         } catch (Exception e) {
            // Include exception message and make the log message clearer
            context.logs.computeIfAbsent(getClass().getSimpleName(), k -> new ArrayList<>())
                    .add(new StepLog(StepsOrder.RequestStatus.FAILED,
                            this.getClass().getSimpleName(),
                            "Exception fetching players from CouchDB: " + e.getMessage(),
                            System.currentTimeMillis() - startTime));

            logger.error(LogFormatter.formatStepLog(getClass().getSimpleName(),
                    StepsOrder.RequestStatus.FAILED,
                    "Exception while fetching players from CouchDB: " + e.getMessage(),
                    System.currentTimeMillis() - startTime), e);
         }
     }
 }
