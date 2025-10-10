package com.backendwebsite.DatabaseBuilder.Step.Players;

import com.backendwebsite.DatabaseBuilder.Client.CouchDBClient;
import com.backendwebsite.DatabaseBuilder.Client.RiotApiClient;
import com.backendwebsite.DatabaseBuilder.Context.BuildPlayerContext;
import com.backendwebsite.DatabaseBuilder.Context.IContext;
import com.backendwebsite.DatabaseBuilder.Step.IStep;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

@Component
public class UpsertPlayersStep implements IStep<BuildPlayerContext> {

    CouchDBClient couchDBClient;

    public UpsertPlayersStep(CouchDBClient couchDBClient) {
        this.couchDBClient = couchDBClient;
    }

    @Override
    public void execute(BuildPlayerContext context) {
        sendPlayerToCouchDB();
    }

    public void sendPlayerToCouchDB() {
        /*
        try {
            for (JsonNode match : body) {

                /// Skip when no id
                if (!match.has("leagueId")) continue;

                String id = match.get("leagueId").asText();
                String json = match.toString();
                String urnCouchDB = "/players/" + id;

                couchDBClient.sendPut(urnCouchDB, json);
            }
            System.out.println("All matches uploaded to CouchDB");
        } catch (Exception e) {
            e.printStackTrace();
        }
        */

    }
}
