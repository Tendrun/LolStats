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

@Component
public class GetPlayersFromCouchDBStep implements IStep<FetchPlayersContext> {
    private final CouchDBClient couchDBClient;
    private final ObjectMapper mapper;
    public GetPlayersFromCouchDBStep(ObjectMapper mapper, CouchDBClient couchDBClient) {
        this.mapper = mapper;
        this.couchDBClient = couchDBClient;
    }

    @Override
    public void execute(FetchPlayersContext context) {
        try {
            String urn = "/players/_all_docs?include_docs=true";
            CouchDBClient.Response response = couchDBClient.sendGet(urn);

            for (JsonNode row : response.body().get("rows")) {
                JsonNode doc = row.get("doc");
                Player player = mapper.treeToValue(doc, Player.class);
                context.existingPlayers.add(player);

                System.out.println("Get = " + player._id);
            }
            context.logs.add(new StepLog(response.status(), this.getClass().getSimpleName(), response.message()));
        } catch (Exception e) {
            context.logs.add(new StepLog(StepsOrder.RequestStatus.FAILED, this.getClass().getSimpleName(), "Exception: "
                    + e.getMessage()));
            e.printStackTrace();
        }
    }
}
