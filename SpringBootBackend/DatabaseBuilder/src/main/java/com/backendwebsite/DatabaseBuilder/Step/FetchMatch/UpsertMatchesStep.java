package com.backendwebsite.DatabaseBuilder.Step.FetchMatch;

import com.backendwebsite.DatabaseBuilder.Client.CouchDBClient;
import com.backendwebsite.DatabaseBuilder.Context.BuildMatchContext;
import com.backendwebsite.DatabaseBuilder.Step.IStep;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class UpsertMatchesStep implements IStep<BuildMatchContext> {

    CouchDBClient couchDBClient;
    ObjectMapper mapper;

    public UpsertMatchesStep(CouchDBClient couchDBClient, ObjectMapper mapper) {
        this.mapper = mapper;
        this.couchDBClient = couchDBClient;
    }

    @Override
    public void execute(BuildMatchContext context) {
        sendMatchesToCouchDB(context);
    }

    public void sendMatchesToCouchDB(BuildMatchContext context) {
        try {
            String json = mapper.writeValueAsString(Map.of("docs", context.finalPlayerMatches));
            String urnCouchDB = "/matches/_bulk_docs";

            couchDBClient.sendPost(urnCouchDB, json);
            System.out.println("Request is send to CouchDB");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
