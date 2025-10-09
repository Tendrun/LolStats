package com.backendwebsite.DatabaseBuilder.Client;

import com.backendwebsite.DatabaseBuilder.Exception.RiotApiException;
import com.backendwebsite.DatabaseBuilder.Factory.CommunicationFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;

@Component
public class RiotApiClient {

    private static final Logger log = LoggerFactory.getLogger(RiotApiClient.class);

    private final HttpClient httpClient;
    private final ObjectMapper mapper;
    private final CommunicationFactory communicationFactory;

    public RiotApiClient(ObjectMapper mapper,
                         CommunicationFactory communicationFactory) {
        this.httpClient = communicationFactory.createHttpClient();
        this.mapper = mapper;
        this.communicationFactory = communicationFactory;
    }

    public <T> T getJson(String urn, String region, TypeReference<T> typeRef) {
        HttpRequest request = communicationFactory.createRequest(urn, region);

        try {
            var response = httpClient.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());

            int code = response.statusCode();
            if (code == 429) {
                throw new RiotApiException("Rate limited (429) by Riot API");
            }
            if (code < 200 || code >= 300) {
                throw new RiotApiException("Riot API error: " + code + " body=" + safeTrim(response.body()));
            }

            return mapper.readValue(response.body(), typeRef);

        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RiotApiException("HTTP/IO error calling Riot API", e);
        }
    }

    /** Pomocnicze: oczekujesz tablicy JSON -> ArrayNode */
    public ArrayNode getArray(String urn, String region) {
        HttpRequest request = communicationFactory.createRequest(urn, region);

        try {
            var response = httpClient.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());

            int code = response.statusCode();
            if (code < 200 || code >= 300) {
                throw new RiotApiException("Riot API error: " + code + " body=" + safeTrim(response.body()));
            }

            JsonNode node = mapper.readTree(response.body());
            if (!node.isArray()) {
                throw new RiotApiException("Expected JSON array but got: " + node.getNodeType());
            }
            return (ArrayNode) node;

        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RiotApiException("HTTP/IO error calling Riot API", e);
        }
    }

    private static String safeTrim(String s) {
        if (s == null) return "";
        return s.length() > 500 ? s.substring(0, 500) + "...(trimmed)" : s;
    }
}
