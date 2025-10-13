package com.backendwebsite.DatabaseBuilder.Step.Match;

import com.backendwebsite.DatabaseBuilder.Client.CouchDBClient;
import com.backendwebsite.DatabaseBuilder.Context.BuildMatchContext;
import com.backendwebsite.DatabaseBuilder.Domain.Match.Match;
import com.backendwebsite.DatabaseBuilder.Step.IStep;
import com.backendwebsite.DatabaseBuilder.Step.Log.StepLog;
import com.backendwebsite.DatabaseBuilder.Step.StepsOrder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

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
            String urn = "/matches/_all_docs?include_docs=true";
            CouchDBClient.Response response = couchDBClient.sendGet(urn);

            for (JsonNode row : response.body().get("rows")) {
                JsonNode doc = row.get("doc");
                Match match = mapper.treeToValue(doc, Match.class);
                context.existingMatches.add(match);

                System.out.println("Get = " + match.matchId());
            }
            context.logs.add(new StepLog(response.status(), this, response.message()));
        } catch (Exception e) {
            context.logs.add(new StepLog(StepsOrder.RequestStatus.FAILED, this, "Exception: "
                    + e.getMessage()));
            e.printStackTrace();
        }
    }
}
