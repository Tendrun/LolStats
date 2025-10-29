package com.backendwebsite.DatabaseBuilder.Step.FetchPlayers;

import com.backendwebsite.DatabaseBuilder.Client.CouchDBClient;
import com.backendwebsite.DatabaseBuilder.Context.FetchPlayersContext;
import com.backendwebsite.DatabaseBuilder.Domain.Player.Player;
import com.backendwebsite.DatabaseBuilder.Step.IStep;
import com.backendwebsite.DatabaseBuilder.Step.Log.StepLog;
import com.backendwebsite.DatabaseBuilder.Step.StepsOrder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        try {
            String urn = "/players/_all_docs?include_docs=true";
            CouchDBClient.Response response = couchDBClient.sendGet(urn);

            JsonNode rows = response.body().get("rows");
            if (rows != null && rows.isArray()) {
                for (JsonNode row : rows) {
                    JsonNode doc = row != null ? row.get("doc") : null;
                    if (doc == null || doc.isNull()) {
                        context.logs.add(new StepLog(StepsOrder.RequestStatus.FAILED, this.getClass().getSimpleName(),
                                "Error: Docs empty in row"));
                        logger.debug("Skipping row without 'doc': {}", row);
                        continue;
                    }
                    Player player = mapper.treeToValue(doc, Player.class);
                    context.existingPlayers.add(player);

                    context.logs.add(new StepLog(response.status(), this.getClass().getSimpleName(),
                            response.message() + " - Fetched player ID: " + player._id));
                    logger.debug("Get = {}", player._id);
                }
            }
            else {
                context.logs.add(new StepLog(StepsOrder.RequestStatus.FAILED, this.getClass().getSimpleName(),
                        "Error: CouchDB response missing 'rows' array"));
                logger.warn("CouchDB response missing 'rows' array)");
            }
        } catch (Exception e) {
            context.logs.add(new StepLog(StepsOrder.RequestStatus.FAILED, this.getClass().getSimpleName(), "Exception: "
                    + e.getMessage()));
            logger.error("Exception while fetching players from CouchDB", e);
        }
    }
}
