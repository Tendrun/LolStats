package com.backendwebsite.DatabaseBuilder.Step.FetchMatchDetails;

import com.backendwebsite.DatabaseBuilder.Client.CouchDBClient;
import com.backendwebsite.DatabaseBuilder.Context.FetchMatchDetailsContext;
import com.backendwebsite.DatabaseBuilder.DTO.RiotApi.MatchDetails.MatchDTO;
import com.backendwebsite.DatabaseBuilder.Step.IStep;
import com.backendwebsite.DatabaseBuilder.Step.Log.StepLog;
import com.backendwebsite.DatabaseBuilder.Step.StepsOrder;
import com.backendwebsite.DatabaseBuilder.Util.LogFormatter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class GetMatchDetailsCouchDBStep implements IStep<FetchMatchDetailsContext> {
    private final CouchDBClient couchDBClient;
    private final ObjectMapper mapper;
    private static final Logger logger = LoggerFactory.getLogger(GetMatchDetailsCouchDBStep.class);

    public GetMatchDetailsCouchDBStep(ObjectMapper mapper, CouchDBClient couchDBClient) {
        this.mapper = mapper;
        this.couchDBClient = couchDBClient;
    }

    @Override
    public void execute(FetchMatchDetailsContext context) {
        long startTime = System.currentTimeMillis();
        try {
            String urn = "/matchdetails/_all_docs?include_docs=true";
            CouchDBClient.Response response = couchDBClient.sendGet(urn);

            JsonNode body = response != null ? response.body() : null;
            JsonNode rows = body != null ? body.get("rows") : null;

            if (rows != null && rows.isArray()) {
                int fetchedCount = 0;
                int skippedCount = 0;

                for (JsonNode row : rows) {
                    JsonNode doc = row != null ? row.get("doc") : null;
                    if (doc == null || doc.isNull()) {
                        skippedCount++;
                        continue;
                    }

                    MatchDTO match = mapper.treeToValue(doc, MatchDTO.class);
                    context.existingMatchDetails.add(match);
                    fetchedCount++;
                }

                String summary = String.format("Fetched %d match details (skipped: %d)", fetchedCount, skippedCount);
                context.logs.computeIfAbsent(getClass().getSimpleName(), k -> new ArrayList<>())
                        .add(new StepLog(response.status(), this.getClass().getSimpleName(),
                                summary, System.currentTimeMillis() - startTime));

                logger.info(LogFormatter.formatStepLog(getClass().getSimpleName(), response.status(),
                        summary, System.currentTimeMillis() - startTime));
            } else {
                context.logs.computeIfAbsent(getClass().getSimpleName(), k -> new ArrayList<>())
                        .add(new StepLog(StepsOrder.RequestStatus.FAILED, this.getClass().getSimpleName(),
                                "Error: CouchDB response missing 'rows' array. Response body: " + body,
                                System.currentTimeMillis() - startTime));
                logger.warn(LogFormatter.formatStepLog(getClass().getSimpleName(), StepsOrder.RequestStatus.FAILED,
                        "CouchDB response missing 'rows' array", System.currentTimeMillis() - startTime));
            }
        } catch (Exception e) {
            context.logs.computeIfAbsent(getClass().getSimpleName(), k -> new ArrayList<>())
                    .add(new StepLog(StepsOrder.RequestStatus.FAILED, this.getClass().getSimpleName(), "Exception: "
                            + e.getMessage(), System.currentTimeMillis() - startTime));
            logger.error(LogFormatter.formatStepLog(getClass().getSimpleName(), StepsOrder.RequestStatus.FAILED,
                    "Exception while fetching match details from CouchDB", System.currentTimeMillis() - startTime), e);
        }
    }
}
