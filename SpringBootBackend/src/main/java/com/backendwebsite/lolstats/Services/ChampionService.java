package com.backendwebsite.lolstats.Services;

import com.backendwebsite.lolstats.Constants.ChampionStatsMap;
import com.backendwebsite.lolstats.Constants.ChampionStatsMap.ChampionDetails;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.backendwebsite.lolstats.Constants.ChampionStatsMap.ALL_BAN_COUNT;
import static com.backendwebsite.lolstats.Constants.ChampionStatsMap.ALL_MATCHES_PLAYED;
import static com.backendwebsite.lolstats.Constants.KeysLoader.loadSecretValue;

@Service
public class ChampionService {

    private final String riotGamesApiKey;
    private final String couchDbUrl;

    public ChampionService() {
        this.riotGamesApiKey = loadSecretValue("RIOTGAMES_API_KEY");
        this.couchDbUrl = loadSecretValue("COUCHDB_URL");
    }

    public void pushChampionDetailsToCouchDB() {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            ObjectMapper mapper = new ObjectMapper();
            ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();

            for (ChampionDetails championDetails : ChampionStatsMap.CHAMPION_MAP.values()) {

                String id = String.valueOf(championDetails.championID);
                String updateFullUrl = couchDbUrl + "/championdetails/" + id;
                String auth = "admin:admin";
                String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());

                // GET dla _rev
                HttpGet getRev = new HttpGet(updateFullUrl);
                getRev.setHeader("Authorization", "Basic " + encodedAuth);

                HttpResponse getResponse = httpClient.execute(getRev);

                String currentRev = null;
                if (getResponse.getStatusLine().getStatusCode() == 200) {
                    String revJson = EntityUtils.toString(getResponse.getEntity());  // ważne!
                    JsonNode existingDoc = mapper.readTree(revJson);
                    currentRev = existingDoc.get("_rev").asText();
                } else {
                    EntityUtils.consume(getResponse.getEntity());  // ważne nawet jeśli nie 200
                }

                // Przygotuj JSON
                ObjectNode jsonNode = mapper.valueToTree(championDetails);
                if (currentRev != null) {
                    jsonNode.put("_rev", currentRev);
                }

                HttpPut put = new HttpPut(updateFullUrl);
                put.setHeader("Authorization", "Basic " + encodedAuth);
                put.setHeader("Content-type", "application/json");

                String json = ow.writeValueAsString(jsonNode);
                put.setEntity(new StringEntity(json));

                HttpResponse dbResponse = httpClient.execute(put);

                EntityUtils.consume(dbResponse.getEntity());  // również zamykaj po PUT
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("The end");
    }

    public void countChampionDetails(String match) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            /// Policz jeden mecz
            ALL_MATCHES_PLAYED += 1;

            String fullUrl = couchDbUrl + "/detailedmatches/" + match;

            HttpGet get = new HttpGet(fullUrl);
            String auth = "admin:admin";
            String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
            get.setHeader("Authorization", "Basic " + encodedAuth);

            HttpResponse response = httpClient.execute(get);

            if (response.getStatusLine().getStatusCode() == 200) {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(response.getEntity().getContent());


                ///  Update champion Detail
                JsonNode participants = root.get("info").get("participants");
                for (JsonNode participant : participants) {

                    ///  Wez ChamponId z jsona
                    int championId = participant.get("championId").asInt();

                    ///  Znajdz championa
                    ChampionDetails championToModify = ChampionStatsMap.CHAMPION_MAP.get(championId);

                    /// Policz ile było ogólem meczy
                    championToModify.totalMatchesPicked += 1;

                    /// Policz Won Matches
                    if (participant.get("win").asBoolean()) {
                        championToModify.wonMatches += 1;
                    }
                }

                /// Policz Win Rate
                for (ChampionDetails champion : ChampionStatsMap.CHAMPION_MAP.values()) {
                    if(champion.wonMatches == 0 || champion.totalMatchesPicked == 0)
                        champion.winRate = (float)champion.wonMatches / champion.totalMatchesPicked;
                }

                ///  Policz Ban Rate
                JsonNode teams = root.get("info").get("teams");
                for (JsonNode team : teams) {
                    JsonNode bans = team.get("bans");
                    for (JsonNode ban : bans) {
                        /// Policz jednego bana
                        ALL_BAN_COUNT += 1;

                        ///  Wez ChamponId z jsona
                        int championId = ban.get("championId").asInt();

                        /// Nothing banned
                        if (championId == -1) {
                            continue;
                        }

                        ///  Znajdz championa
                        ChampionDetails championToModify =
                                ChampionStatsMap.CHAMPION_MAP.get(championId);

                        championToModify.bannedMatches += 1;
                    }
                }

                /// Policz Ban Rate
                for (ChampionDetails champion : ChampionStatsMap.CHAMPION_MAP.values()) {
                    if(champion.wonMatches == 0 || champion.totalMatchesPicked == 0)
                        champion.banRate = (float)champion.banRate / ALL_BAN_COUNT;
                }


                /// Policz Pick Rate

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<ChampionDetails> getAllChampDetailsFromCouchDB() {
        List<ChampionDetails> championDetailsList = new ArrayList<>();
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

                ChampionDetails championDetails = new ChampionDetails(championID, championName, winRate, banRate,
                        pickRate, totalMatchesPicked, wonMatches, bannedMatches);
                championDetailsList.add(championDetails);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return championDetailsList;
    }
}
