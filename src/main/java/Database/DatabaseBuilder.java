package Database;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.IOException;

public class DatabaseBuilder {

    private final String riotGamesApiKey;

    public DatabaseBuilder(){
        this.riotGamesApiKey = loadApiKey();
    }

    public void exampleQuery(){
        // Replace with the summoner name you want to query
        String summonerName = "PopeSlayer123";
        // Use appropriate region (e.g., "na1", "euw1", etc.)
        String region = "eun1";
        String url = "https://" + region + ".api.riotgames.com/lol/platform/v3/champion-rotations";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("X-Riot-Token", riotGamesApiKey)
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Response Code: " + response.statusCode());
            System.out.println("Response Body: " + response.body());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private String loadApiKey() {
        String fileLocation = "secret/secret.txt";

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileLocation);
        if (inputStream == null) {
            throw new RuntimeException("API key file not found: " + fileLocation);
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line = reader.readLine();
            if (line != null && line.contains("=")) {
                // Split the line into key and value
                String[] parts = line.split("=", 2);
                // Get the part after equal sign ( = )
                // After that remove any single quotes and trim whitespace
                return parts[1].replace("'", "").trim();
            }
            throw new RuntimeException("Invalid API key format in " + fileLocation);
        } catch (IOException e) {
            throw new RuntimeException("Error reading API key from " + fileLocation, e);
        }
    }
}
