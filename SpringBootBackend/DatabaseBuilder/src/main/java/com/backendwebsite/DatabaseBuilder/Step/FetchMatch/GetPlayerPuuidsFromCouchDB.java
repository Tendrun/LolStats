package com.backendwebsite.DatabaseBuilder.Step.FetchMatch;

import com.backendwebsite.DatabaseBuilder.Client.CouchDBClient;
import com.backendwebsite.DatabaseBuilder.Context.FetchMatchesContext;
import com.backendwebsite.DatabaseBuilder.Step.IStep;
import com.backendwebsite.DatabaseBuilder.Step.Log.StepLog;
import com.backendwebsite.DatabaseBuilder.Step.StepsOrder;
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
                for (JsonNode doc : docs) {
                    JsonNode puuidNode = doc.get("puuid");
                    if (puuidNode != null && !puuidNode.isNull()) {
                        String puuid = puuidNode.asText();
                        context.puuids.add(puuid);
                        context.logs.computeIfAbsent(getClass().getSimpleName(), k -> new java.util.ArrayList<>())
                            .add(new StepLog(response.status(), this.getClass().getSimpleName(), response.message()));
                        logger.debug("Get = {}", puuid);
                    }
                }
            } else {
                context.logs.computeIfAbsent(getClass().getSimpleName(), k -> new java.util.ArrayList<>())
                        .add(new StepLog(StepsOrder.RequestStatus.FAILED, this.getClass().getSimpleName(),
                                "Error: CouchDB response missing 'docs' array" + " Response body: " +
                                        response.body()));
                logger.warn("CouchDB response missing 'rows' array)");
            }
        } catch (Exception e) {
            context.logs.computeIfAbsent(getClass().getSimpleName(), k -> new java.util.ArrayList<>())
                    .add(new StepLog(StepsOrder.RequestStatus.FAILED, this.getClass().getSimpleName(),
                            "Exception: " + e.getMessage()));
            logger.error("Exception while fetching players puuids from CouchDB", e);
        }
    }
}
