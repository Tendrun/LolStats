package com.backendwebsite.DatabaseBuilder.Step.FetchMatch;

import com.backendwebsite.DatabaseBuilder.Client.CouchDBClient;
import com.backendwebsite.DatabaseBuilder.Context.BuildMatchContext;
import com.backendwebsite.DatabaseBuilder.Step.IStep;
import com.backendwebsite.DatabaseBuilder.Step.Log.StepLog;
import com.backendwebsite.DatabaseBuilder.Step.StepsOrder;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

@Component
public class GetPlayersPuuidFromCouchDB implements IStep<BuildMatchContext> {
    private final CouchDBClient couchDBClient;

    public GetPlayersPuuidFromCouchDB(CouchDBClient couchDBClient){
        this.couchDBClient = couchDBClient;
    }

    @Override
    public void execute(BuildMatchContext context) {
        try {

            System.err.println("HERE IS SET LIMIT");
            String urn = "/players/_find";
            String body = """
            {
              "selector": {},
              "fields": [
                "puuid"
              ],
              "limit": 1
            }
            """;

            CouchDBClient.Response response = couchDBClient.sendPost(urn, body);

            for (JsonNode doc : response.body().get("docs")) {
                String puuid = doc.get("puuid").asText();
                context.puuids.add(puuid);
                System.out.println("Get = " + puuid);
            }

            context.logs.add(new StepLog(response.status(), this, response.message()));
        } catch (Exception e) {
            context.logs.add(new StepLog(StepsOrder.RequestStatus.FAILED, this, "Exception: "
                    + e.getMessage()));
            e.printStackTrace();
        }
    }
}
