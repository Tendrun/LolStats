package Database.Configuration;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Configuration
public class MongoConfig {

    @Bean
    public MongoClient mongoClient() {
        return MongoClients.create(loadMongoConnectionString());
    }

    private String loadMongoConnectionString() {
        String fileLocation = "secret/secret.txt";

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileLocation);
        if (inputStream == null) {
            throw new RuntimeException("API key file not found: " + fileLocation);
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line = reader.readLine();
            if (line != null && line.contains("MONGODB_CONNECTION_STRING=")) {
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
