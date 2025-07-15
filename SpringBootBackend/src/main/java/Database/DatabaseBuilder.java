package Database;

import Database.Models.LeagueEntryDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.HttpResponse;

import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

public class DatabaseBuilder {

    private final String riotGamesApiKey;
    private final String couchDbUrl;

    private final List<String> ListOfMatches = new ArrayList<>();

    public DatabaseBuilder() {
        this.riotGamesApiKey = loadSecretValue("RIOTGAMES_API_KEY");
        this.couchDbUrl = loadSecretValue("COUCHDB_URL"); // e.g. http://admin:admin@localhost:5984
    }

    public void buildDatabase() {
        /*getPlayers("IRON"); // krok 1: pobierz/odśwież graczy z Riot API
        getPlayers("BRONZE"); // Od rangi Iron do Diamond
        getPlayers("SILVER");
        getPlayers("GOLD");
        getPlayers("PLATINUM");
        getPlayers("EMERALD");
        getPlayers("DIAMOND");
        */
        /*
        List<LeagueEntryDTO> players = getAllPlayers(); // krok 2: pobierz wszystkich graczy z bazy

        for (LeagueEntryDTO player : players) {
            String puuid = player.getPuuid();
            if (puuid != null && !puuid.isEmpty()) {
                System.out.println("=== Fetching matches for: " + puuid + " ===");
                getPlayerMatches(puuid, "2"); // krok 3: pobierz mecze i zapisz
            } else {
                System.out.println("Skipping player with missing PUUID: " + player.get_id());
            }
        }
        */
        for (String match : getAllMatches()){
            getMatchData(match);
        }

    }



    public List<LeagueEntryDTO> getAllPlayers() {
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
    }

    public void getPlayers(String tier) {
        String region = "eun1";
        String url = "https://" + region + ".api.riotgames.com/lol/league/v4/entries/RANKED_SOLO_5x5/" + tier + "/IV?page=1";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("X-Riot-Token", riotGamesApiKey)
                .build();

        try {
            java.net.http.HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
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

            try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
                for (JsonNode match : root) {
                    String id = match.has("leagueId") ? match.get("leagueId").asText() : UUID.randomUUID().toString();
                    String json = match.toString();
                    String fullUrl = couchDbUrl + "/players/" + id;

                    HttpPut put = new HttpPut(fullUrl);
                    put.setHeader("Content-type", "application/json"); // dodaj autoryzację!
                    String auth = "admin:admin"; // lub z pliku
                    String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
                    put.setHeader("Authorization", "Basic " + encodedAuth);

                    put.setEntity(new StringEntity(json));

                    System.out.println("PUT -> " + fullUrl);

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

    public void getPlayerMatches(String puuid, String count) {
        String region = "europe";
        String url = "https://" + region + ".api.riotgames.com/lol/match/v5/matches/by-puuid/" + puuid
                + "/ids?start=0&count=" + count;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("X-Riot-Token", riotGamesApiKey)
                .build();

        try {
            java.net.http.HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
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

            try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
                for (JsonNode match : root) {
                    String matchId = match.asText(); // <- poprawka!
                    String fullUrl = couchDbUrl + "/matches/" + matchId;

                    // prosty JSON dokument do zapisania
                    String json = "{ \"_id\": \"" + matchId + "\", \"puuid\": \"" + puuid + "\" }";

                    HttpPut put = new HttpPut(fullUrl);
                    put.setHeader("Content-type", "application/json");
                    String auth = "admin:admin"; // z pliku w przyszłości
                    String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
                    put.setHeader("Authorization", "Basic " + encodedAuth);
                    put.setEntity(new StringEntity(json));

                    System.out.println("PUT -> " + fullUrl);
                    HttpResponse dbResponse = httpClient.execute(put);
                    System.out.println("Status: " + dbResponse.getStatusLine());

                    BufferedReader reader = new BufferedReader(new InputStreamReader(dbResponse.getEntity().getContent()));
                    String responseBody = reader.lines().reduce("", (a, b) -> a + b);
                    ListOfMatches.add(responseBody); // Dodaj mecz do listy
                    System.out.println("PUT response body: " + responseBody);

                    if (dbResponse.getStatusLine().getStatusCode() != 201 &&
                            dbResponse.getStatusLine().getStatusCode() != 202) {
                        System.err.println("Failed to save match: " + dbResponse.getStatusLine());
                    }
                }
                System.out.println("All match IDs uploaded to CouchDB");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<String> getAllMatches() {
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
                System.err.println("Failed to fetch players: " + response.getStatusLine());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return matches;
    }

    public void getMatchData(String matchId) {
        String region = "europe";
        String url = "https://" + region + ".api.riotgames.com/lol/match/v5/matches/" + matchId;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("X-Riot-Token", riotGamesApiKey)
                .build();

        try {
            java.net.http.HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
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


    private String loadSecretValue(String key) {
        String fileLocation = "secret/secret.txt";
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileLocation);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(key + "=")) {
                    return line.split("=", 2)[1].replace("'", "").trim();
                }
            }
            throw new RuntimeException(key + " not found in " + fileLocation);
        } catch (IOException | NullPointerException e) {
            throw new RuntimeException("Error reading secret from file: " + fileLocation, e);
        }
    }
}
