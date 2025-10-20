package com.backendwebsite.DatabaseBuilder.Step.FetchMatchDetails;

import com.backendwebsite.DatabaseBuilder.Client.CouchDBClient;
import com.backendwebsite.DatabaseBuilder.Context.FetchMatchDetailsContext;
import com.backendwebsite.DatabaseBuilder.Step.IStep;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class UpsertMatchDetailsStep implements IStep<FetchMatchDetailsContext> {
    CouchDBClient couchDBClient;
    ObjectMapper mapper;

    public UpsertMatchDetailsStep(CouchDBClient couchDBClient, ObjectMapper mapper) {
        this.mapper = mapper;
        this.couchDBClient = couchDBClient;
    }

    @Override
    public void execute(FetchMatchDetailsContext context) {
        sendMatchDetailsToCouchDB(context);
    }

    public void sendMatchDetailsToCouchDB(FetchMatchDetailsContext context) {
        try {
            System.out.println(context.finalMatchDetails.getClass());
            context.finalMatchDetails.forEach(e -> System.out.println(e.getClass()));
            String json = mapper.writeValueAsString(Map.of("docs", context.finalMatchDetails));
            String urnCouchDB = "/matchdetails/_bulk_docs";

            couchDBClient.sendPost(urnCouchDB, json);
            System.out.println("Request is send to CouchDB");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
