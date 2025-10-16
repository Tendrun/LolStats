package com.backendwebsite.DatabaseBuilder.Step.FetchMatchDetails;

import com.backendwebsite.DatabaseBuilder.Client.CouchDBClient;
import com.backendwebsite.DatabaseBuilder.Context.FetchMatchDetailsContext;
import com.backendwebsite.DatabaseBuilder.DTO.RiotApi.MatchDetails.MatchDTO;
import com.backendwebsite.DatabaseBuilder.Domain.Match.PlayerMatches;
import com.backendwebsite.DatabaseBuilder.Step.IStep;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
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
            ArrayNode docs = mapper.createArrayNode();
/*
            for (MatchDTO matchDetails : context.fetchedMatches) {
                ObjectNode doc = mapper.valueToTree(matchDetails);
                String rev = matchDetails._rev();
                if(rev != null && !rev.trim().isEmpty()) {
                    doc.put("_rev", rev);
                } else {
                    doc.remove("_rev");
                }
                docs.add(doc);
            }
*/
            System.out.println(context.fetchedMatches.getClass());
            context.fetchedMatches.forEach(e -> System.out.println(e.getClass()));
            String json = mapper.writeValueAsString(Map.of("docs", context.fetchedMatches));
            String urnCouchDB = "/match_details/_bulk_docs";

            couchDBClient.sendPost(urnCouchDB, json);
            System.out.println("Request is send to CouchDB");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
