package com.backendwebsite.DatabaseBuilder.Step.Players;

import com.backendwebsite.DatabaseBuilder.Client.CouchDBClient;
import com.backendwebsite.DatabaseBuilder.Client.RiotApiClient;
import com.backendwebsite.DatabaseBuilder.Context.BuildPlayerContext;
import com.backendwebsite.DatabaseBuilder.Step.IStep;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class FetchPlayersStep implements IStep<BuildPlayerContext> {

    private final RiotApiClient riotApiClient;
    private final CouchDBClient couchDBClient;

    public FetchPlayersStep(RiotApiClient riotApiClient,
                            CouchDBClient couchDBClient){
        this.riotApiClient = riotApiClient;
        this.couchDBClient = couchDBClient;
    }
    
    @Override
    public void execute(BuildPlayerContext context) {
        getPlayersFromRiot(context);
    }

    public void getPlayersFromRiot(BuildPlayerContext context) {
        String urnRiot = "/lol/league/v4/entries/" + context.queue + "/" + context.tier + "/" +
                context.division + "?page=" + context.page;

        System.out.println(urnRiot);

        try {
            RiotApiClient.Response root = riotApiClient.sendRequest(urnRiot, context.region);

            for (JsonNode match : root.body()) {

                //TO DO
                //This is very bad
                String id = match.has("leagueId") ? match.get("leagueId").asText() : UUID.randomUUID().toString();
                String json = match.toString();
                String urnCouchDB = "/players/" + id;

                couchDBClient.sendPut(urnCouchDB, context.region, json);
            }
            System.out.println("All matches uploaded to CouchDB");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
