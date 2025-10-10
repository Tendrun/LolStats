package com.backendwebsite.DatabaseBuilder.Step.Players;

import com.backendwebsite.DatabaseBuilder.Client.CouchDBClient;
import com.backendwebsite.DatabaseBuilder.Client.RiotApiClient;
import com.backendwebsite.DatabaseBuilder.Context.BuildPlayerContext;
import com.backendwebsite.DatabaseBuilder.Context.IContext;
import com.backendwebsite.DatabaseBuilder.Step.IStep;
import com.backendwebsite.DatabaseBuilder.Step.Log.StepLog;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class UpsertPlayersStep implements IStep<BuildPlayerContext> {

    CouchDBClient couchDBClient;
    ObjectMapper mapper;

    public UpsertPlayersStep(CouchDBClient couchDBClient, ObjectMapper mapper) {
        this.mapper = mapper;
        this.couchDBClient = couchDBClient;
    }

    @Override
    public void execute(BuildPlayerContext context) {
        sendPlayerToCouchDB(context);
    }

    public void sendPlayerToCouchDB(BuildPlayerContext context) {
        try {
            String json = mapper.writeValueAsString(Map.of("docs", context.finalPlayers));
            String urnCouchDB = "/players/_bulk_docs";

            couchDBClient.sendPost(urnCouchDB, json);
            System.out.println("All matches uploaded to CouchDB");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
