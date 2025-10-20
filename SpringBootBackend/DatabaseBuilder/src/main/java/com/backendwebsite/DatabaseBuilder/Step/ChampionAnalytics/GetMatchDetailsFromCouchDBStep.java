package com.backendwebsite.DatabaseBuilder.Step.ChampionAnalytics;

import com.backendwebsite.DatabaseBuilder.Client.CouchDBClient;
import com.backendwebsite.DatabaseBuilder.Context.BuildChampionAnalyticsContext;
import com.backendwebsite.DatabaseBuilder.DTO.RiotApi.MatchDetails.MatchDTO;
import com.backendwebsite.DatabaseBuilder.Step.IStep;
import com.backendwebsite.DatabaseBuilder.Step.Log.StepLog;
import com.backendwebsite.DatabaseBuilder.Step.StepsOrder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class GetMatchDetailsFromCouchDBStep implements IStep<BuildChampionAnalyticsContext> {
    private final CouchDBClient couchDBClient;
    private final ObjectMapper mapper;

    public GetMatchDetailsFromCouchDBStep(ObjectMapper mapper, CouchDBClient couchDBClient) {
        this.mapper = mapper;
        this.couchDBClient = couchDBClient;
    }

    @Override
    public void execute(BuildChampionAnalyticsContext context) {
        try {
            String urn = "/matchdetails/_find";
            String body = """
            {
              "selector": {},
              "limit": %d
            }
            """.formatted(context.limitMatches);

            CouchDBClient.Response response = couchDBClient.sendPost(urn, body);

            for (JsonNode row : response.body().get("docs")) {
                MatchDTO matchDetail = mapper.treeToValue(row, MatchDTO.class);

                context.matchDetails.add(matchDetail);
                System.out.println("Get = " + matchDetail._id);
            }

            context.logs.add(new StepLog(response.status(), this.getClass().getSimpleName(), response.message()));
        } catch (Exception e) {
            context.logs.add(new StepLog(StepsOrder.RequestStatus.FAILED, this.getClass().getSimpleName(), "Exception: "
                    + e.getMessage()));
            e.printStackTrace();
        }
    }
}
