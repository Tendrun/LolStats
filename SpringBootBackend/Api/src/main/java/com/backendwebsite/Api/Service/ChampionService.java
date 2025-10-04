package com.backendwebsite.Api.Service;

import com.backendwebsite.Api.DTO.ChampionDetails.ChampionDetailsResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;
import static com.backendwebsite.Helper.KeysLoader.loadSecretValue;

@Service
public class ChampionService {

    private final String couchDbUrl;

    public ChampionService() {
        this.couchDbUrl = loadSecretValue("COUCHDB_URL");
    }

    public List<ChampionDetailsResponse> getAllChampDetailsFromCouchDB() {
        List<ChampionDetailsResponse> championDetailsList = new ArrayList<>();
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            String URL = couchDbUrl + "/championdetails/_all_docs?include_docs=true";
            String auth = "admin:admin";
            String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());

            // GET dla _rev
            HttpGet getChampionsDetails = new HttpGet(URL);
            getChampionsDetails.setHeader("Authorization", "Basic " + encodedAuth);
            HttpResponse response = httpClient.execute(getChampionsDetails);


            String json = new BufferedReader(new InputStreamReader(response.getEntity().getContent()))
                    .lines()
                    .collect(Collectors.joining());

            System.out.println(json);
            if (response.getStatusLine().getStatusCode() != 201) {
                System.err.println("Failed to save match: " + response.getStatusLine());
            }

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(json);

            JsonNode rows = root.get("rows");
            for (JsonNode row : rows) {
                JsonNode doc = row.get("doc");

                int championID = doc.get("_id").asInt();
                String championName = doc.get("championName").asText();
                float winRate = doc.get("winRate").floatValue();
                float banRate = doc.get("banRate").floatValue();
                float pickRate = doc.get("pickRate").floatValue();
                int totalMatchesPicked = doc.get("totalMatchesPicked").asInt();
                int wonMatches = doc.get("wonMatches").asInt();
                int bannedMatches = doc.get("bannedMatches").asInt();

                ChampionDetailsResponse championDetailsResponse = new ChampionDetailsResponse(championID, championName, winRate, banRate,
                        pickRate, totalMatchesPicked, wonMatches, bannedMatches);
                championDetailsList.add(championDetailsResponse);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return championDetailsList;
    }
}
