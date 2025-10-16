package com.backendwebsite.DatabaseBuilder.Step.FetchPlayers;

import com.backendwebsite.DatabaseBuilder.Client.CouchDBClient;
import com.backendwebsite.DatabaseBuilder.Context.FetchPlayersContext;
import com.backendwebsite.DatabaseBuilder.Step.IStep;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class UpsertPlayersStep implements IStep<FetchPlayersContext> {

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
        try {
            String json = mapper.writeValueAsString(Map.of("docs", context.finalPlayers));
            String urnCouchDB = "/players/_bulk_docs";

            couchDBClient.sendPost(urnCouchDB, json);
            System.out.println("Request is send to CouchDB");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
