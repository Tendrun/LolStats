package com.backendwebsite.DatabaseBuilder.Step.Players;

import com.backendwebsite.DatabaseBuilder.Client.CouchDBClient;
import com.backendwebsite.DatabaseBuilder.Context.BuildPlayerContext;
import com.backendwebsite.DatabaseBuilder.DTO.getPlayers.LeagueEntryDTO;
import com.backendwebsite.DatabaseBuilder.Step.IStep;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class GetPlayersFromCouchDBStep implements IStep<BuildPlayerContext> {
    private final CouchDBClient couchDBClient;
    private final ObjectMapper mapper;
    public GetPlayersFromCouchDBStep(ObjectMapper mapper, CouchDBClient couchDBClient) {
        this.mapper = mapper;
        this.couchDBClient = couchDBClient;
    }

    @Override
    public void execute(BuildPlayerContext context) {
        try {
            String urn = "/players/_all_docs?include_docs=true";

            CouchDBClient.Response response = couchDBClient.sendGet(urn);

            for (JsonNode row : response.body().get("rows")) {
                JsonNode doc = row.get("doc");
                LeagueEntryDTO player = mapper.treeToValue(doc, LeagueEntryDTO.class);
                context.existingPlayers.add(player);

                System.out.println("Get = " + player._id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
