package com.backendwebsite.DatabaseBuilder.Client;

import com.backendwebsite.DatabaseBuilder.Factory.CommunicationFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import com.backendwebsite.DatabaseBuilder.Step.StepsOrder.RequestStatus;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class RiotApiClient {
    private static final Logger logger = LoggerFactory.getLogger(RiotApiClient.class);

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
                logger.info("Riot Client: Document received successfully for {} (status={}).", urn, statusCode);
                return new Response(RequestStatus.SUCCESSFUL, body);
            } else if (statusCode == 401) {
                logger.error("Riot Client: Check Riot API Key (status={}).", statusCode);
                return new Response(RequestStatus.FAILED, body);
            } else if (statusCode == 429) {
                int retry = 0;
                final int maxRetries = 2;

                logger.warn("Riot Client: rate limited (429), retry will occur for {}.", urn);
                while (retry < maxRetries) {

                    logger.warn("Riot Client: Retry attempt {} for {}.", retry + 1, urn);
                    try {
                        Thread.sleep(120000);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        logger.error("Riot Client: retry interrupted", ie);
                        return new Response(RequestStatus.FAILED, body);
                    }

                    request = communicationFactory.createRequest(urn, region);
                    response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
                    statusCode = response.statusCode();
                    body = mapper.readTree(response.body());

                    if (statusCode != 429) {
                        break;
                    }

                    retry++;
                }

                if (statusCode == 200) {
                    logger.info("Riot Client: Document received successfully after retry for {}.", urn);
                    return new Response(RequestStatus.SUCCESSFUL, body);
                } else {
                    logger.error("Riot Client: request failed after {} retries. Status: {}", maxRetries, statusCode);
                    return new Response(RequestStatus.FAILED, body);
                }
            }
            else {
                logger.error("Riot Client: request failed with status: {} for {}.", statusCode, urn);
                return new Response(RequestStatus.FAILED, body);
            }
        } catch (Exception e) {
            logger.error("Riot Client: Unknown error while sending request for {}", urn, e);
            return new Response(RequestStatus.FAILED, null);
        }
    }
}
