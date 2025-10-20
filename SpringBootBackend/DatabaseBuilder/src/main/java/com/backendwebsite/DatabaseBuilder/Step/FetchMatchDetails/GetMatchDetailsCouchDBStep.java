package com.backendwebsite.DatabaseBuilder.Step.FetchMatchDetails;

import com.backendwebsite.DatabaseBuilder.Client.CouchDBClient;
import com.backendwebsite.DatabaseBuilder.Context.FetchMatchDetailsContext;
import com.backendwebsite.DatabaseBuilder.DTO.RiotApi.MatchDetails.MatchDTO;
import com.backendwebsite.DatabaseBuilder.Step.IStep;
import com.backendwebsite.DatabaseBuilder.Step.Log.StepLog;
import com.backendwebsite.DatabaseBuilder.Step.StepsOrder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class GetMatchDetailsCouchDBStep implements IStep<FetchMatchDetailsContext> {
    private final CouchDBClient couchDBClient;
    private final ObjectMapper mapper;
    public GetMatchDetailsCouchDBStep(ObjectMapper mapper, CouchDBClient couchDBClient) {
        this.mapper = mapper;
        this.couchDBClient = couchDBClient;
    }

    @Override
    public void execute(FetchMatchDetailsContext context) {
        try {
            String urn = "/matchdetails/_all_docs?include_docs=true";
            CouchDBClient.Response response = couchDBClient.sendGet(urn);

            for (JsonNode row : response.body().get("rows")) {
                JsonNode doc = row.get("doc");
                MatchDTO match = mapper.treeToValue(doc, MatchDTO.class);
                context.existingMatchDetails.add(match);

                System.out.println("Get = " + match._id);
            }
            context.logs.add(new StepLog(response.status(), this.getClass().getSimpleName(), response.message()));
        } catch (Exception e) {
            context.logs.add(new StepLog(StepsOrder.RequestStatus.FAILED, this.getClass().getSimpleName(), "Exception: "
                    + e.getMessage()));
            e.printStackTrace();
        }
    }
}
