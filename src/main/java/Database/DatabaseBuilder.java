package Database;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class DatabaseBuilder {

    private final String riotGamesApiKey;
    private final String mongoConnectionString;

    public DatabaseBuilder(){
        this.riotGamesApiKey = loadApiKey();
        this.mongoConnectionString = loadMongoConnectionString();
    }

    public void buildDatabase(){
        getMatches();
    }

    public void getMatches(){
        String region = "eun1";
        String url = "https://" + region + ".api.riotgames.com/lol/league/v4/entries/RANKED_SOLO_5x5/IRON/IV?page=1";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("X-Riot-Token", riotGamesApiKey)
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Response Code: " + response.statusCode());
            String responseBody = response.body();
            System.out.println("Response Body: " + responseBody);

            // Parse JSON array response and convert each element to a MongoDB Document
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(responseBody);
            if (rootNode.isArray()) {
                List<Document> documents = new ArrayList<>();
                for (JsonNode node : rootNode) {
                    Document doc = Document.parse(node.toString());
                    documents.add(doc);
                }
                System.out.println("Parsed " + documents.size() + " documents from the response.");

                // Connect to MongoDB and insert the documents
                MongoClient mongoClient = MongoClients.create(mongoConnectionString);
                // Replace "your_database_name" with your desired database name
                MongoDatabase database = mongoClient.getDatabase("MainDatabase");
                // Replace "matches" with your desired collection name
                MongoCollection<Document> collection = database.getCollection("matches");

                if (!documents.isEmpty()) {
                    collection.insertMany(documents);
                    System.out.println("Inserted " + documents.size() + " documents into MongoDB.");
                } else {
                    System.err.println("No documents to insert into MongoDB.");
                }
                mongoClient.close();
            } else {
                System.err.println("Unexpected JSON structure. Expected an array.");
            }
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
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("RIOTGAMES_API_KEY=")) {
                    String[] parts = line.split("=", 2);
                    return parts[1].replace("'", "").trim();
                }
            }
            throw new RuntimeException("RIOTGAMES_API_KEY not found in " + fileLocation);
        } catch (IOException e) {
            throw new RuntimeException("Error reading API key from " + fileLocation, e);
        }
    }

    private String loadMongoConnectionString() {
        String fileLocation = "secret/secret.txt";
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileLocation);
        if (inputStream == null) {
            throw new RuntimeException("Secret file not found: " + fileLocation);
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("MONGODB_CONNECTION_STRING=")) {
                    String[] parts = line.split("=", 2);
                    return parts[1].replace("'", "").trim();
                }
            }
            throw new RuntimeException("MONGODB_CONNECTION_STRING not found in " + fileLocation);
        } catch (IOException e) {
            throw new RuntimeException("Error reading MongoDB connection string from " + fileLocation, e);
        }
    }
}
