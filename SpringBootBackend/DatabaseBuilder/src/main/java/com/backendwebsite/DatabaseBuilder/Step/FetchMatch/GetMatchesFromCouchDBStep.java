package com.backendwebsite.DatabaseBuilder.Step.FetchMatch;

import com.backendwebsite.DatabaseBuilder.Client.CouchDBClient;
import com.backendwebsite.DatabaseBuilder.Context.BuildMatchContext;
import com.backendwebsite.DatabaseBuilder.Domain.Match.PlayerMatches;
import com.backendwebsite.DatabaseBuilder.Step.IStep;
import com.backendwebsite.DatabaseBuilder.Step.Log.StepLog;
import com.backendwebsite.DatabaseBuilder.Step.StepsOrder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class GetMatchesFromCouchDBStep implements IStep<BuildMatchContext> {
    private final CouchDBClient couchDBClient;
    private final ObjectMapper mapper;
    public GetMatchesFromCouchDBStep(ObjectMapper mapper, CouchDBClient couchDBClient) {
        this.mapper = mapper;
        this.couchDBClient = couchDBClient;
    }

    @Override
    public void execute(BuildMatchContext context) {
        try {
            String idsJson = mapper.writeValueAsString(context.puuids);

            String urn = "/matches/_find";
            String body = """
            {
              "selector": {
                "_id": {
                  "$in": %s
                }
              },
              "limit": 999999
            }
            """.formatted(idsJson);


            CouchDBClient.Response response = couchDBClient.sendPost(urn, body);

            for (JsonNode row : response.body().get("docs")) {
                PlayerMatches playerMatches = mapper.treeToValue(row, PlayerMatches.class);

                context.existingMatches.put(playerMatches.puuid(), playerMatches);
                System.out.println("Get = " + playerMatches);
            }
            context.logs.add(new StepLog(response.status(), this, response.message()));
        } catch (Exception e) {
            context.logs.add(new StepLog(StepsOrder.RequestStatus.FAILED, this, "Exception: "
                    + e.getMessage()));
            e.printStackTrace();
        }
    }
}
