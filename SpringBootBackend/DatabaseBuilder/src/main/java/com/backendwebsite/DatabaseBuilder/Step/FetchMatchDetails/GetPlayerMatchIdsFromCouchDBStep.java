package com.backendwebsite.DatabaseBuilder.Step.FetchMatchDetails;

import com.backendwebsite.DatabaseBuilder.Client.CouchDBClient;
import com.backendwebsite.DatabaseBuilder.Context.FetchMatchDetailsContext;
import com.backendwebsite.DatabaseBuilder.Step.IStep;
import com.backendwebsite.DatabaseBuilder.Step.Log.StepLog;
import com.backendwebsite.DatabaseBuilder.Step.StepsOrder;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

@Component
public class GetPlayerMatchIdsFromCouchDBStep implements IStep<FetchMatchDetailsContext> {

    private final CouchDBClient couchDBClient;

    public GetPlayerMatchIdsFromCouchDBStep(CouchDBClient couchDBClient){
        this.couchDBClient = couchDBClient;
    }

    @Override
    public void execute(FetchMatchDetailsContext context) {
        try {
            String urn = "/matches/_find";
            String body = """
            {
              "selector": {},
              "fields": [
                "matchIds"
              ],
              "limit": %d
            }
            """.formatted(context.playerMatchLimit);

            CouchDBClient.Response response = couchDBClient.sendPost(urn, body);
            for (JsonNode doc : response.body().get("docs")) {
                JsonNode matchIdsNode = doc.get("matchIds");
                if (matchIdsNode != null && matchIdsNode.isArray()) {
                    for (JsonNode idNode : matchIdsNode) {
                        String matchId = idNode.asText();
                        context.matchIds.add(matchId);
                        System.out.println("Get = " + matchId);

                        context.logs.add(new StepLog(StepsOrder.RequestStatus.SUCCESSFUL,
                                this.getClass().getSimpleName(),
                                "Succeeded: " + response.message()));
                    }
                }
            }

        } catch (Exception e) {
            context.logs.add(new StepLog(StepsOrder.RequestStatus.FAILED,
                    this.getClass().getSimpleName(), "Exception: "
                    + e.getMessage()));
            e.printStackTrace();
        }
    }
}