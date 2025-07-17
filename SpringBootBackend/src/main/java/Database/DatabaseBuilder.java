package Database;

import Database.Models.LeagueEntryDTO;
import Services.ChampionService;
import Services.MatchDataService;
import Services.PlayersService;
import Static.ChampionStatsMap;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

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

    private final MatchDataService matchDataService;
    private final PlayersService playersService;
    private final ChampionService championService;

    public DatabaseBuilder() {
        this.riotGamesApiKey = loadSecretValue("RIOTGAMES_API_KEY");
        this.couchDbUrl = loadSecretValue("COUCHDB_URL"); // e.g. http://admin:admin@localhost:5984

        this.matchDataService = new MatchDataService(riotGamesApiKey, couchDbUrl);
        this.playersService = new PlayersService(riotGamesApiKey, couchDbUrl);
        this.championService = new ChampionService(riotGamesApiKey, couchDbUrl);
    }

    public void buildDatabase() {
        /*
        playersService.getPlayers("IRON"); // krok 1: pobierz/odśwież graczy z Riot API
        playersService.getPlayers("BRONZE"); // Od rangi Iron do Diamond
        playersService.getPlayers("SILVER");
        playersService.getPlayers("GOLD");
        playersService.getPlayers("PLATINUM");
        playersService.getPlayers("EMERALD");
        playersService.getPlayers("DIAMOND");


        List<LeagueEntryDTO> players = playersService.getAllPlayers(); // krok 2: pobierz wszystkich graczy z bazy

        for (LeagueEntryDTO player : players) {
            String puuid = player.getPuuid();
            if (puuid != null && !puuid.isEmpty()) {
                System.out.println("=== Fetching matches for: " + puuid + " ===");
                getPlayerMatches(puuid, "2"); // krok 3: pobierz mecze i zapisz
            } else {
                System.out.println("Skipping player with missing PUUID: " + player.get_id());
            }
        }

        /*
        for (String match : getAllMatches()){
            getMatchData(match);
        }
        */
        /*
        for (String match : getAllMatches()){
            countChampionDetails(match);
        }
        pushChampionDetails();
        */
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
