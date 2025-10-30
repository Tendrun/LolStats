package com.backendwebsite.DatabaseBuilder.Step.ChampionAnalytics;

import com.backendwebsite.DatabaseBuilder.Client.CouchDBClient;
import com.backendwebsite.DatabaseBuilder.Constant.ChampionStatsMap.ChampionDetails;
import com.backendwebsite.DatabaseBuilder.Context.BuildChampionAnalyticsContext;
import com.backendwebsite.DatabaseBuilder.Step.IStep;
import com.backendwebsite.DatabaseBuilder.Step.Log.StepLog;
import com.backendwebsite.DatabaseBuilder.Step.StepsOrder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UpsertChampStatsStep implements IStep<BuildChampionAnalyticsContext> {
    CouchDBClient couchDBClient;
    ObjectMapper mapper;
    private static final Logger logger = LoggerFactory.getLogger(UpsertChampStatsStep.class);

    public UpsertChampStatsStep(CouchDBClient couchDBClient, ObjectMapper mapper) {
        this.mapper = mapper;
        this.couchDBClient = couchDBClient;
    }

    @Override
    public void execute(BuildChampionAnalyticsContext context) {
        try {
            ArrayNode docs = mapper.createArrayNode();

            List<String> championIds = context.championStatsMap.CHAMPION_MAP.stream()
                    .map(championDetails -> championDetails.name + ":" + championDetails.championId)
                    .collect(Collectors.toList());

            String normalizedChampionIds = mapper.writeValueAsString(championIds);

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
            """.formatted(normalizedChampionIds);

            CouchDBClient.Response response = couchDBClient.sendPost(urn, body);

            JsonNode respBody = response != null ? response.body() : null;
            JsonNode docsNode = respBody != null ? respBody.get("docs") : null;

            if (docsNode != null && docsNode.isArray()) {
                for (JsonNode row : docsNode) {
                    ChampionDetails championDetailCouchDb = mapper.treeToValue(row, ChampionDetails.class);

                    context.championStatsMap.CHAMPION_MAP.stream()
                            .filter(championDetail -> championDetail.championId == championDetailCouchDb.championId)
                            .findFirst()
                            .ifPresent(championDetail -> championDetail._rev = championDetailCouchDb._rev);
                }
            } else {
                logger.warn("CouchDB response missing 'docs' for urn {} - body: {}", urn, respBody);
                context.logs.computeIfAbsent(getClass().getSimpleName(), k -> new java.util.ArrayList<>())
                        .add(new StepLog(StepsOrder.RequestStatus.FAILED, this.getClass().getSimpleName(),
                                "CouchDB find for champion details returned no docs. Response body: " + respBody));
            }

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

            CouchDBClient.Response bulkResp = couchDBClient.sendPost(urnCouchDB, json);

            if (bulkResp != null && bulkResp.status() == StepsOrder.RequestStatus.SUCCESSFUL) {
                context.logs.computeIfAbsent(getClass().getSimpleName(), k -> new java.util.ArrayList<>())
                        .add(new StepLog(bulkResp.status(), this.getClass().getSimpleName(),
                                bulkResp.message() + " - Upserted " + context.championStatsMap.CHAMPION_MAP.size() + " docs"));
                logger.info("Upserted {} champion docs. Response: {}", context.championStatsMap.CHAMPION_MAP.size(), bulkResp.message());
            } else {
                context.logs.computeIfAbsent(getClass().getSimpleName(), k -> new java.util.ArrayList<>())
                        .add(new StepLog(bulkResp != null ? bulkResp.status() : StepsOrder.RequestStatus.FAILED,
                                this.getClass().getSimpleName(),
                                "Failed to upsert champion stats to CouchDB. Response: " + (bulkResp != null ? bulkResp.message() : "null")));
                logger.warn("Failed to upsert champion stats. Response: {}", bulkResp != null ? bulkResp.message() : "null");
            }
        } catch (Exception e) {
            context.logs.computeIfAbsent(getClass().getSimpleName(), k -> new java.util.ArrayList<>())
                    .add(new StepLog(StepsOrder.RequestStatus.FAILED, this.getClass().getSimpleName(), "Exception: "
                            + e.getMessage()));
            logger.error("Exception in UpsertChampStatsStep", e);
        }
    }
}
