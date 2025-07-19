package com.backendwebsite.lolstats.Services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import static com.backendwebsite.lolstats.Constants.KeysLoader.loadSecretValue;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class MatchDataService {

    private final String riotGamesApiKey;
    private final String couchDbUrl;

    public MatchDataService(String riotGamesApiKey, String couchDbUrl) {
        this.riotGamesApiKey = loadSecretValue("RIOTGAMES_API_KEY");
        this.couchDbUrl = loadSecretValue("COUCHDB_URL"); // e.g. http://admin:admin@localhost:5984
    }

    public List<String> getAllMatchesFromCouchDB() {
        List<String> matches = new ArrayList<>();

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            String fullUrl = couchDbUrl + "/matches/_all_docs?include_docs=true";

            HttpGet get = new HttpGet(fullUrl);
            String auth = "admin:admin";
            String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
            get.setHeader("Authorization", "Basic " + encodedAuth);

            HttpResponse response = httpClient.execute(get);
            if (response.getStatusLine().getStatusCode() == 200) {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(response.getEntity().getContent());

                for (JsonNode row : root.get("rows")) {
                    JsonNode id = row.get("id");
                    matches.add(id.asText());
                }
            } else {
                System.err.println("Failed to fetch matches: " + response.getStatusLine());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return matches;
    }

    public void getMatchDataFromRiot(String matchId) {
        String region = "europe";
        String url = "https://" + region + ".api.riotgames.com/lol/match/v5/matches/" + matchId;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("X-Riot-Token", riotGamesApiKey)
                .build();

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
                System.out.println("PUT -> " + fullUrl);
                HttpResponse dbResponse = httpClient.execute(put);

                System.out.println("Status: " + dbResponse.getStatusLine());

                BufferedReader reader = new BufferedReader(new InputStreamReader(dbResponse.getEntity().getContent()));
                String responseBody = reader.lines().reduce("", (a, b) -> a + b);
                System.out.println("PUT response body: " + responseBody);

                if (dbResponse.getStatusLine().getStatusCode() != 201 &&
                        dbResponse.getStatusLine().getStatusCode() != 202) {
                    System.err.println("Failed to save match: " + dbResponse.getStatusLine());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
