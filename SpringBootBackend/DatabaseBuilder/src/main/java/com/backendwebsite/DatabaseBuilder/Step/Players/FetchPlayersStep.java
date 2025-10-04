package com.backendwebsite.DatabaseBuilder.Step.Players;

import com.backendwebsite.DatabaseBuilder.Helper.DatabaseHelper;
import com.backendwebsite.DatabaseBuilder.Step.IStep;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.UUID;

public class FetchPlayersStep implements IStep {

    @Override
    public void execute() {

    }

    public void getPlayersFromRiot(String tier, String division, String queue) {
        String urnRiot = "/lol/league/v4/entries/" + queue + "/" + tier + "/" + division + "?page=1";

        System.out.println(urnRiot);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = communicationFactory.createRequest(urnRiot);

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

            try (CloseableHttpClient httpClient = communicationFactory.createClient()) {
                for (JsonNode match : root) {

                    //TO DO
                    //This is very bad
                    String id = match.has("leagueId") ? match.get("leagueId").asText() : UUID.randomUUID().toString();
                    String json = match.toString();
                    String urnCouchDB = "/players/" + id;

                    HttpPut put = communicationFactory.createHttpPut(urnCouchDB);
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
