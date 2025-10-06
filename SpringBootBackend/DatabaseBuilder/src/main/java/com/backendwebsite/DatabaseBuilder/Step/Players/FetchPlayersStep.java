package com.backendwebsite.DatabaseBuilder.Step.Players;

import com.backendwebsite.DatabaseBuilder.Context.BuildMatchContext;
import com.backendwebsite.DatabaseBuilder.Context.IContext;
import com.backendwebsite.DatabaseBuilder.Factory.CommunicationFactory;
import com.backendwebsite.DatabaseBuilder.Helper.DatabaseHelper;
import com.backendwebsite.DatabaseBuilder.Step.IStep;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.UUID;

@Component
public class FetchPlayersStep implements IStep<BuildMatchContext> {

    private final CommunicationFactory communicationFactory;

    public FetchPlayersStep(CommunicationFactory communicationFactory){
        this.communicationFactory = communicationFactory;
    }
    
    @Override
    public void execute(BuildMatchContext context) {
        getPlayersFromRiot(context);
    }

    public void getPlayersFromRiot(BuildMatchContext context) {
        String urnRiot = "/lol/league/v4/entries/" + context.getQueue() + "/" + context.getTier() + "/" +
                context.getDivision() + "?page=1";

        System.out.println(urnRiot);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = communicationFactory.createRequest(urnRiot, context.getRegion());

        try {
            java.net.http.HttpResponse<String> response = client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                System.err.println("Riot API error: " + response.statusCode());
                return;
            }

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.body());
            if (!root.isArray()) {
                System.err.println("Expected JSON array");
                return;
            }

            try (CloseableHttpClient httpClient = communicationFactory.createClient(context.getRegion())) {
                for (JsonNode match : root) {

                    //TO DO
                    //This is very bad
                    String id = match.has("leagueId") ? match.get("leagueId").asText() : UUID.randomUUID().toString();
                    String json = match.toString();
                    String urnCouchDB = "/players/" + id;

                    HttpPut put = communicationFactory.createHttpPut(urnCouchDB, context.getRegion());
                    put.setEntity(new StringEntity(json));

                    HttpResponse dbResponse = httpClient.execute(put);

                    System.out.println("Status: " + dbResponse.getStatusLine());

                    String responseBody = DatabaseHelper.reader(new BufferedReader(
                            new InputStreamReader(dbResponse.getEntity().getContent())));

                    System.out.println("PUT response body: " + responseBody);


                    if (dbResponse.getStatusLine().getStatusCode() != 201) {
                        System.err.println("Failed to save match: " + dbResponse.getStatusLine());
                    }

                    if (dbResponse.getStatusLine().getStatusCode() == 409) {
                        System.err.println("Record already exists");
                    }
                }
                System.out.println("All matches uploaded to CouchDB");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
