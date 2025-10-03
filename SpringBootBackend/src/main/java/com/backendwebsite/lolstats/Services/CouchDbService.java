package com.backendwebsite.lolstats.Services;

import com.backendwebsite.lolstats.DTOs.LeagueEntryDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.*;
import java.util.UUID;

@Service
public class CouchDbService {

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    private final String dbUrl = "http://localhost:5984/lolstats"; // Zmień na właściwe

    public LeagueEntryDTO save(LeagueEntryDTO entry) {
        try {
            if (entry.get_id() == null || entry.get_id().isEmpty()) {
                entry.set_id(UUID.randomUUID().toString());
            }

            String body = mapper.writeValueAsString(entry);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(dbUrl + "/" + entry.get_id()))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("CouchDB response: " + response.body());

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return entry;
    }
}

