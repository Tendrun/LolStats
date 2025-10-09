package com.backendwebsite.DatabaseBuilder.Client;

import com.backendwebsite.DatabaseBuilder.Factory.CommunicationFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import com.backendwebsite.DatabaseBuilder.Step.StepsOrder.RequestStatus;


import java.net.http.HttpClient;
import java.net.http.HttpRequest;

@Component
public class RiotApiClient {
    private final HttpClient httpClient;
    private final ObjectMapper mapper;
    private final CommunicationFactory communicationFactory;

    public record Response(RequestStatus status, JsonNode body) { }

    public RiotApiClient(ObjectMapper mapper,
                         CommunicationFactory communicationFactory) {
        this.httpClient = communicationFactory.createHttpClient();
        this.mapper = mapper;
        this.communicationFactory = communicationFactory;
    }

    public Response sendRequest(String urn, String region) {
        HttpRequest request = communicationFactory.createRequest(urn, region);

        try {
            var response = httpClient.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());
            int statusCode = response.statusCode();
            JsonNode body = mapper.readTree(response.body());

            if (statusCode == 201) {
                System.out.println("CouchDB: Document saved successfully.");
                return new Response(RequestStatus.SUCCESSFUL, body);
            }
            else if (statusCode == 409) {
                System.out.println("CouchDB: Document already exists — skipped.");
                return new Response(RequestStatus.SKIPPED, body);
            }
            else {
                System.err.println("CouchDB request failed with status: " + statusCode);
                return new Response(RequestStatus.FAILED, body);
            }
        } catch (Exception e) {
            System.err.println("Unknown error" + e.getMessage());
            return new Response(RequestStatus.FAILED, null);
        }
    }
}
