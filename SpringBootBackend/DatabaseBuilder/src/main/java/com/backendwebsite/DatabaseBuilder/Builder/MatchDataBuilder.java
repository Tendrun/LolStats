package com.backendwebsite.DatabaseBuilder.Builder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.http.HttpClient;
import java.util.Base64;

public class MatchDataBuilder implements IBuilder {

    @Override
    public void build() {
        throw new RuntimeException();
    }
/*
    public void getMatchDataFromRiot() {
        String region = "europe";
        String url = "https://" + region + ".api.riotgames.com/lol/match/v5/matches/" + matchId;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = Request.create(riotGamesApiKey, url);

        try {
            java.net.http.HttpResponse<String> response = client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                System.err.println("Riot API error: " + response.statusCode());
                return;
            }

            String json = response.body(); // ← cały JSON meczu

            String fullUrl = couchDbUrl + "/detailedmatches/" + matchId;

            HttpPut put = new HttpPut(fullUrl);
            put.setHeader("Content-type", "application/json");
            String auth = "admin:admin";
            String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
            put.setHeader("Authorization", "Basic " + encodedAuth);
            put.setEntity(new StringEntity(json));

            try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
                HttpResponse dbResponse = httpClient.execute(put);


                BufferedReader reader = new BufferedReader(new InputStreamReader(dbResponse.getEntity().getContent()));
                String responseBody = reader.lines().reduce("", (a, b) -> a + b);

                if (dbResponse.getStatusLine().getStatusCode() != 201 &&
                        dbResponse.getStatusLine().getStatusCode() != 202) {
                    System.err.println("Failed to save match: " + dbResponse.getStatusLine());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
*/
    @Override
    public Object getResult() {
        return null;
    }
}
