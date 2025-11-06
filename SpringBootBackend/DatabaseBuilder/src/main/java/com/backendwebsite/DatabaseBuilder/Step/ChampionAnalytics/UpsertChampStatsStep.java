package com.backendwebsite.DatabaseBuilder.Step.ChampionAnalytics;

import com.backendwebsite.DatabaseBuilder.Client.CouchDBClient;
import com.backendwebsite.DatabaseBuilder.Constant.ChampionStatsMap.ChampionDetails;
import com.backendwebsite.DatabaseBuilder.Context.BuildChampionAnalyticsContext;
import com.backendwebsite.DatabaseBuilder.Step.IStep;
import com.backendwebsite.DatabaseBuilder.Step.Log.StepLog;
import com.backendwebsite.DatabaseBuilder.Step.StepsOrder;
import com.backendwebsite.DatabaseBuilder.Util.LogFormatter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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
        long startTime = System.currentTimeMillis();
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
                            .filter(championDetail ->
                                    championDetail.championId == championDetailCouchDb.championId)
                            .findFirst()
                            .ifPresent(championDetail ->
                                    championDetail._rev = championDetailCouchDb._rev);
                }
            } else {
                String respStr = respBody != null ? respBody.toString() : "null";
                String msg = "CouchDB find for champion details returned no docs. Response body: " + respStr;
                context.logs.computeIfAbsent(getClass().getSimpleName(), k -> new java.util.ArrayList<>())
                        .add(new StepLog(StepsOrder.RequestStatus.FAILED, this.getClass().getSimpleName(),
                                msg, System.currentTimeMillis() - startTime));
                logger.warn(LogFormatter.formatStepLog(getClass().getSimpleName(), StepsOrder.RequestStatus.FAILED,
                        "CouchDB find returned no docs for urn " + urn, System.currentTimeMillis() - startTime)
                        + " responseBody=" + respStr);
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

            if (bulkResp == null || bulkResp.status() == StepsOrder.RequestStatus.FAILED) {
                List<String> failedIds = (bulkResp != null && bulkResp.failedIds() != null) ? bulkResp.failedIds() : new ArrayList<>();
                int total = context.championStatsMap.CHAMPION_MAP.size();
                int failedCount = failedIds.size();
                int succeededCount = Math.max(0, total - failedCount);

                String summary = String.format("total=%d succeeded=%d failed=%d", total, succeededCount, failedCount);
                String msg = String.format("Upsert summary: %s failedIds=%s", summary, failedIds);

                context.logs.computeIfAbsent(getClass().getSimpleName(), k -> new java.util.ArrayList<>())
                        .add(new StepLog(StepsOrder.RequestStatus.FAILED, this.getClass().getSimpleName(),
                                msg, System.currentTimeMillis() - startTime));

                String responseBodyStr = (bulkResp != null && bulkResp.body() != null) ? bulkResp.body().toString() : "null";
                String formattedPrefix = LogFormatter.formatStepLog(getClass().getSimpleName(), StepsOrder.RequestStatus.FAILED,
                        "Upsert failed for champion docs " + msg, System.currentTimeMillis() - startTime);
                logger.error("{} responseBody={}", formattedPrefix, responseBodyStr);
                return;
            }

            context.logs.computeIfAbsent(getClass().getSimpleName(), k -> new java.util.ArrayList<>())
                    .add(new StepLog(bulkResp.status(), this.getClass().getSimpleName(),
                            bulkResp.message() + " - Upserted " +
                                    context.championStatsMap.CHAMPION_MAP.size() + " docs. Response body: " +
                                    (bulkResp.body() != null ? bulkResp.body().toString() : "null"),
                            System.currentTimeMillis() - startTime));

            logger.info(LogFormatter.formatStepLog(getClass().getSimpleName(), bulkResp.status(),
                    "Upserted " + context.championStatsMap.CHAMPION_MAP.size() + " champion docs",
                    System.currentTimeMillis() - startTime)
                    + " responseBody=" + (bulkResp.body() != null ? bulkResp.body().toString() : "null"));
        } catch (Exception e) {
            context.logs.computeIfAbsent(getClass().getSimpleName(), k -> new java.util.ArrayList<>())
                    .add(new StepLog(StepsOrder.RequestStatus.FAILED, this.getClass().getSimpleName(),
                            "Exception: "
                            + e.getMessage(), System.currentTimeMillis() - startTime));
            logger.error("Exception in UpsertChampStatsStep", e);
        }
    }
}
