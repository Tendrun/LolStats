package com.backendwebsite.DatabaseBuilder.Builder;

import com.backendwebsite.DatabaseBuilder.Factory.CommunicationFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.Base64;
import java.util.UUID;

import static com.backendwebsite.Helper.KeysLoader.loadSecretValue;

public class GetPlayersBuilder implements IBuilder{

    CommunicationFactory communicationFactory;

    public GetPlayersBuilder(CommunicationFactory communicationFactory){
        this.communicationFactory = communicationFactory;
    }

    @Override
    public void build() {
        getPlayersFromRiot("BRONZE","IV", "RANKED_SOLO_5x5");
    }

    @Override
    public Object getResult() {
        return null;
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

                    BufferedReader reader = new BufferedReader(new InputStreamReader(dbResponse.getEntity().getContent()));
                    String responseBody = reader.lines().reduce("", (a, b) -> a + b);
                    System.out.println("PUT response body: " + responseBody);


                    if (dbResponse.getStatusLine().getStatusCode() != 201) {
                        System.err.println("Failed to save match: " + dbResponse.getStatusLine());
                    }
                }
                System.out.println("All matches uploaded to CouchDB");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
