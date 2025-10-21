package com.backendwebsite.DatabaseBuilder.Step.ChampionAnalytics;

import com.backendwebsite.DatabaseBuilder.Client.CouchDBClient;
import com.backendwebsite.DatabaseBuilder.Constant.ChampionStatsMap.ChampionDetails;
import com.backendwebsite.DatabaseBuilder.Context.BuildChampionAnalyticsContext;
import com.backendwebsite.DatabaseBuilder.Domain.Match.PlayerMatches;
import com.backendwebsite.DatabaseBuilder.Step.IStep;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UpsertChampStatsStep implements IStep<BuildChampionAnalyticsContext> {
    CouchDBClient couchDBClient;
    ObjectMapper mapper;

    public UpsertChampStatsStep(CouchDBClient couchDBClient, ObjectMapper mapper) {
        this.mapper = mapper;
        this.couchDBClient = couchDBClient;
    }

    @Override
    public void execute(BuildChampionAnalyticsContext context) {
        try {
            ArrayNode docs = mapper.createArrayNode();
/*
            String championIds = context.championStatsMap.CHAMPION_MAP.stream()
                .map(championDetails -> championDetails.name + ":" + championDetails.championId)
                .collect(Collectors.joining(", "));

            String urn = "/championdetails/_find";
            String body = """
            {
              "selector": {
                "_id": {
                  "$in": %s
                }
              },
              "limit": 999999
            }
            """.formatted(championIds);

            CouchDBClient.Response response = couchDBClient.sendPost(urn, body);
            JsonNode row = response.body().get("docs");

            for (JsonNode row : response.body().get("docs")) {
                PlayerMatches playerMatches = mapper.treeToValue(row, PlayerMatches.class);

                context.existingMatches.put(playerMatches.puuid(), playerMatches);
                System.out.println("Get = " + playerMatches);
            }
*/
            for (ChampionDetails championDetails : context.championStatsMap.CHAMPION_MAP) {
                championDetails._id = championDetails.name + ":" + championDetails.championId;
                ObjectNode doc = mapper.valueToTree(championDetails);
                String rev = championDetails._rev;
                if(rev != null && !rev.trim().isEmpty()) {
                    doc.put("_rev", rev);
                } else {
                    doc.remove("_rev");
                }
                docs.add(doc);
            }

            String json = mapper.writeValueAsString(Map.of("docs", docs));
            String urnCouchDB = "/championdetails/_bulk_docs";

            couchDBClient.sendPost(urnCouchDB, json);
            System.out.println("Request is send to CouchDB");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
