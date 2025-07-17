package Services;

import Static.ChampionStatsMap;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Base64;

public class ChampionService {

    private final String riotGamesApiKey;
    private final String couchDbUrl;


    public ChampionService(String riotGamesApiKey, String couchDbUrl) {
        this.riotGamesApiKey = riotGamesApiKey;
        this.couchDbUrl = couchDbUrl; // e.g. http://admin:admin@localhost:5984
    }

    public void pushChampionDetailsToCouchDB() {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            ObjectMapper mapper = new ObjectMapper();
            ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();

            for (ChampionStatsMap.ChampionDetails championDetails : ChampionStatsMap.CHAMPION_MAP.values()) {
                System.out.println(championDetails.championID + " " + championDetails.totalMatches);

                String id = String.valueOf(championDetails.championID);
                String updateFullUrl = couchDbUrl + "/championdetails/" + id;
                String auth = "admin:admin";
                String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());

                // GET dla _rev
                HttpGet getRev = new HttpGet(updateFullUrl);
                getRev.setHeader("Authorization", "Basic " + encodedAuth);

                System.out.println("Before " + getRev);
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

                System.out.println("PUT -> " + updateFullUrl);
                HttpResponse dbResponse = httpClient.execute(put);
                System.out.println("Status: " + dbResponse.getStatusLine());

                EntityUtils.consume(dbResponse.getEntity());  // również zamykaj po PUT
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("The end");
    }

    public void countChampionDetails(String match) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            System.out.println(match);
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
                    int championId = participant.get("championId").asInt();

                    ///  Znajdz championa
                    ChampionStatsMap.ChampionDetails championToModify = ChampionStatsMap.CHAMPION_MAP.get(championId);

                    /// Policz ile było ogólem meczy
                    championToModify.totalMatches += 1;
                    /// Policz Won Matches
                    if (participant.get("win").asBoolean()) {
                        championToModify.wonMatches += 1;
                    }
                }

                ///  Policz Ban Rate
                JsonNode teams = root.get("info").get("teams");

                for (JsonNode team : teams) {
                    JsonNode bans = team.get("bans");
                    for (JsonNode ban : bans) {

                        int championId = ban.get("championId").asInt();

                        /// Nothing banned
                        if (championId == -1) {
                            continue;
                        }

                        ChampionStatsMap.ChampionDetails championToModify = ChampionStatsMap.CHAMPION_MAP
                                .get(ban.get("championId").asInt());

                        championToModify.bannedMatches += 1;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
