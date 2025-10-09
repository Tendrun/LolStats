package com.backendwebsite.DatabaseBuilder.Step.Players;

import com.backendwebsite.DatabaseBuilder.Context.BuildPlayerContext;
import com.backendwebsite.DatabaseBuilder.Step.IStep;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class getPlayersFromCouchDBStep implements IStep<BuildPlayerContext> {
    @Override
    public void execute(BuildPlayerContext context) {
        
        List<LeagueEntryDTO> players = new ArrayList<>();

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            String fullUrl = couchDbUrl + "/players/_all_docs?include_docs=true";

            HttpGet get = new HttpGet(fullUrl);
            String auth = "admin:admin";
            String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
            get.setHeader("Authorization", "Basic " + encodedAuth);

            HttpResponse response = httpClient.execute(get);
            if (response.getStatusLine().getStatusCode() == 200) {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(response.getEntity().getContent());

                for (JsonNode row : root.get("rows")) {
                    JsonNode doc = row.get("doc");
                    LeagueEntryDTO player = mapper.treeToValue(doc, LeagueEntryDTO.class);
                    players.add(player);
                }
            } else {
                System.err.println("Failed to fetch players: " + response.getStatusLine());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return players;
    }*/
}
