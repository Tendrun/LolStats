package com.backendwebsite.DatabaseBuilder.Step.FetchMatch;

import com.backendwebsite.DatabaseBuilder.Client.CouchDBClient;
import com.backendwebsite.DatabaseBuilder.Context.FetchMatchesContext;
import com.backendwebsite.DatabaseBuilder.Domain.Match.PlayerMatches;
import com.backendwebsite.DatabaseBuilder.Step.IStep;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class UpsertMatchesStep implements IStep<FetchMatchesContext> {
    CouchDBClient couchDBClient;
    ObjectMapper mapper;

    public UpsertMatchesStep(CouchDBClient couchDBClient, ObjectMapper mapper) {
        this.mapper = mapper;
        this.couchDBClient = couchDBClient;
    }

    @Override
    public void execute(FetchMatchesContext context) {
        sendMatchesToCouchDB(context);
    }

    public void sendMatchesToCouchDB(FetchMatchesContext context) {
        try {
            ArrayNode docs = mapper.createArrayNode();

            for (PlayerMatches playerMatch : context.finalPlayerMatches) {
                ObjectNode doc = mapper.valueToTree(playerMatch);
                String rev = playerMatch._rev();
                if(rev != null && !rev.trim().isEmpty()) {
                    doc.put("_rev", rev);
                } else {
                    doc.remove("_rev");
                }
                docs.add(doc);
            }

            String json = mapper.writeValueAsString(Map.of("docs", docs));
            String urnCouchDB = "/matches/_bulk_docs";

            couchDBClient.sendPost(urnCouchDB, json);
            System.out.println("Request is send to CouchDB");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
