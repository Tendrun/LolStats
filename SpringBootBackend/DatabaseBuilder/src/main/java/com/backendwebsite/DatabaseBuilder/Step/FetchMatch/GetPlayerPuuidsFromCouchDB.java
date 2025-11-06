package com.backendwebsite.DatabaseBuilder.Step.FetchMatch;

import com.backendwebsite.DatabaseBuilder.Client.CouchDBClient;
import com.backendwebsite.DatabaseBuilder.Context.FetchMatchesContext;
import com.backendwebsite.DatabaseBuilder.Step.IStep;
import com.backendwebsite.DatabaseBuilder.Step.Log.StepLog;
import com.backendwebsite.DatabaseBuilder.Step.StepsOrder;
import com.backendwebsite.DatabaseBuilder.Util.LogFormatter;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class GetPlayerPuuidsFromCouchDB implements IStep<FetchMatchesContext> {
    private static final Logger logger = LoggerFactory.getLogger(GetPlayerPuuidsFromCouchDB.class);
    private final CouchDBClient couchDBClient;

    public GetPlayerPuuidsFromCouchDB(CouchDBClient couchDBClient){
        this.couchDBClient = couchDBClient;
    }

    @Override
    public void execute(FetchMatchesContext context) {
        long startTime = System.currentTimeMillis();
        try {
            String urn = "/players/_find";
            String body = """
            {
              "selector": {
                "tier": {
                  "$eq": "%s"
                }
              },
              "fields": [
                "puuid"
              ],
              "limit": %d
            }
            """.formatted(context.tier , context.playerLimit);

            CouchDBClient.Response response = couchDBClient.sendPost(urn, body);

            JsonNode bodyNode = response.body();
            JsonNode docs = bodyNode != null ? bodyNode.get("docs") : null;

            if (docs != null && docs.isArray()) {
                int count = 0;
                for (JsonNode doc : docs) {
                    JsonNode puuidNode = doc.get("puuid");
                    if (puuidNode != null && !puuidNode.isNull()) {
                        String puuid = puuidNode.asText();
                        context.puuids.add(puuid);
                        count++;
                        logger.info("Fetched puuid: {}", puuid);
                    }
                }
                String msg = "Fetched " + count + " player puuids from CouchDB";
                context.logs.computeIfAbsent(getClass().getSimpleName(), k -> new java.util.ArrayList<>())
                    .add(new StepLog(response.status(), this.getClass().getSimpleName(), msg,
                            System.currentTimeMillis() - startTime));
                logger.info(LogFormatter.formatStepLog(getClass().getSimpleName(), response.status(), msg,
                        System.currentTimeMillis() - startTime));
             } else {
                String msg = "CouchDB response missing 'docs' array. Response body: " + bodyNode;
                 context.logs.computeIfAbsent(getClass().getSimpleName(), k -> new java.util.ArrayList<>())
                         .add(new StepLog(StepsOrder.RequestStatus.FAILED, this.getClass().getSimpleName(),
                                msg, System.currentTimeMillis() - startTime));
                logger.warn(LogFormatter.formatStepLog(getClass().getSimpleName(), StepsOrder.RequestStatus.FAILED, msg,
                        System.currentTimeMillis() - startTime));
             }
         } catch (Exception e) {
            String msg = "Exception while fetching player puuids from CouchDB: " + e.getMessage();
             context.logs.computeIfAbsent(getClass().getSimpleName(), k -> new java.util.ArrayList<>())
                     .add(new StepLog(StepsOrder.RequestStatus.FAILED, this.getClass().getSimpleName(),
                            msg, System.currentTimeMillis() - startTime));
            logger.error(LogFormatter.formatStepLog(getClass().getSimpleName(), StepsOrder.RequestStatus.FAILED, msg,
                    System.currentTimeMillis() - startTime), e);
         }
     }
 }
