package com.backendwebsite.DatabaseBuilder.Client;

import com.backendwebsite.DatabaseBuilder.Factory.CommunicationFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Nullable;
import org.springframework.stereotype.Component;
import com.backendwebsite.DatabaseBuilder.Step.StepsOrder.RequestStatus;


import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

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
            var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            int statusCode = response.statusCode();
            JsonNode body = mapper.readTree(response.body());

            if (statusCode == 200) {
                System.out.println("Riot Client: Document received successfully.");
                return new Response(RequestStatus.SUCCESSFUL, body);
            } else if (statusCode == 401) {
                System.err.println("Riot Client: Check Riot API Key");
                return new Response(RequestStatus.FAILED, body);
            } else if (statusCode == 429) {
                int retry = 0;

                System.err.println("Riot Client: Extended number of API calls, retry will occur");
                while(retry < 2) {

                    System.err.println("Retry:" + retry);
                    Thread.sleep(120000);

                    request = communicationFactory.createRequest(urn, region);
                    response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
                    statusCode = response.statusCode();
                    body = mapper.readTree(response.body());

                    if (retry >= 2) {
                        System.err.println("Riot Client: request failed with status: " + statusCode);
                        return new Response(RequestStatus.FAILED, body);
                    } else if (statusCode != 429) {
                        break;
                    }

                    retry++;
                }

                if (statusCode == 200) {
                    System.out.println("Riot Client: Document received successfully after retry.");
                    return new Response(RequestStatus.SUCCESSFUL, body);
                } else {
                    System.err.println("Riot Client: request failed after " + retry + 1
                            + " retries. Status: " + statusCode);
                    return new Response(RequestStatus.FAILED, body);
                }
            }
            else {
                System.err.println("Riot Client: request failed with status: " + statusCode);
                return new Response(RequestStatus.FAILED, body);
            }
        } catch (Exception e) {
            System.err.println("Unknown error: " + e.getMessage());
            return new Response(RequestStatus.FAILED, null);
        }
    }
}
