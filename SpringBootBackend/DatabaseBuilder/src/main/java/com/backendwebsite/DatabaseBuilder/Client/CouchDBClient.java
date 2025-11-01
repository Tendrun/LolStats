package com.backendwebsite.DatabaseBuilder.Client;

import com.backendwebsite.DatabaseBuilder.Exception.RiotApiException;
import com.backendwebsite.DatabaseBuilder.Factory.CommunicationFactory;
import com.backendwebsite.DatabaseBuilder.Helper.DatabaseHelper;
import com.backendwebsite.DatabaseBuilder.Step.StepsOrder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.catalina.mapper.Mapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.http.HttpClient;
import com.backendwebsite.DatabaseBuilder.Step.StepsOrder.RequestStatus;

@Component
public class CouchDBClient {
    private final CommunicationFactory communicationFactory;
    private final ObjectMapper mapper;
    CloseableHttpClient httpClient;
    public CouchDBClient(ObjectMapper mapper, CommunicationFactory communicationFactory) {
        this.httpClient = communicationFactory.createCloseableHttpClient();
        this.mapper = mapper;
        this.communicationFactory = communicationFactory;
    }

    public record Response(RequestStatus status, @Nullable JsonNode body,  @Nullable String message) { }

    public Response sendPut(String urn, String json)  {

        int statusCode = 0;

        try {
            HttpPut put = communicationFactory.createHttpPut(urn);
            put.setEntity(new StringEntity(json));

            HttpResponse dbResponse = httpClient.execute(put);
            statusCode = dbResponse.getStatusLine().getStatusCode();

            System.out.println("Status: " + dbResponse.getStatusLine());

            String responseBody = DatabaseHelper.reader(new BufferedReader(
                    new InputStreamReader(dbResponse.getEntity().getContent())));

            System.out.println("PUT response body: " + responseBody);

            if (statusCode == 201 || statusCode == 202) {
                System.out.println("CouchDB: Document saved successfully.");
                return new Response(RequestStatus.SUCCESSFUL, null, "CouchDB: Document get successfully");
            }
            else if (statusCode == 409) {
                System.out.println("CouchDB: Document already exists — skipped.");
                return new Response(RequestStatus.SKIPPED, null, "Document already exists — skipped.");
            }
            else {
                System.err.println("CouchDB request failed with status: " + statusCode);
                return new Response(RequestStatus.FAILED, null, "CouchDB request failed with status: " +
                        "\" + statusCode");
            }
        }
        catch (Exception e) {
            System.err.println("Unknown CouchDB error: " + e.getMessage());
            return new Response(RequestStatus.FAILED, null, "Unknown CouchDB error: " + statusCode);
        }
    }

    public Response sendPost(String urn, String json) {

        int statusCode = 0;

        try {
            HttpPost post = communicationFactory.createHttpPost(urn);
            post.setEntity(new StringEntity(json));

            HttpResponse dbResponse = httpClient.execute(post);
            statusCode = dbResponse.getStatusLine().getStatusCode();

            System.out.println("Status: " + dbResponse.getStatusLine());

            String responseBody = DatabaseHelper.reader(new BufferedReader(
                    new InputStreamReader(dbResponse.getEntity().getContent())));

            System.out.println("POST response body: " + responseBody);

            JsonNode body = mapper.readTree(responseBody);

            // Detect CouchDB per-document conflicts when bulk-inserting (response is an array)
            try {
                boolean hasConflict = false;
                // collect conflicting items for easier debugging
                java.util.List<JsonNode> conflicts = new java.util.ArrayList<>();

                if (body.isArray()) {
                    for (JsonNode item : body) {
                        if (item.has("error") && "conflict".equals(item.get("error").asText())) {
                            hasConflict = true;
                            conflicts.add(item);
                        }
                    }
                } else if (body.isObject()) {
                    if (body.has("error") && "conflict".equals(body.get("error").asText())) {
                        hasConflict = true;
                        conflicts.add(body);
                    }
                }

                if (hasConflict) {
                    System.out.println("CouchDB: Some documents had conflicts during POST.");
                    // return SKIPPED so caller can handle partial conflicts; include full body for inspection
                    return new Response(RequestStatus.FAILED, body,
                            "CouchDB: Some documents had conflicts during POST. Conflicts count: " + conflicts.size());
                }
            } catch (Exception parseEx) {
                // If conflict-detection fails for any reason, continue and treat as successful read
                System.err.println("Warning: failed to parse POST response for conflicts: " + parseEx.getMessage());
            }


            if (statusCode == 200) {
                System.out.println("CouchDB: Document saved successfully.");
                return new Response(RequestStatus.SUCCESSFUL, body, "CouchDB: Document post successfully");
            }
            else if (statusCode == 201) {
                System.out.println("CouchDB: Document saved successfully.");
                return new Response(RequestStatus.SUCCESSFUL, body, " CouchDB: Document post successfully");
            }
            else if (statusCode == 409) {
                System.out.println("CouchDB: Document already exists — skipped.");
                return new Response(RequestStatus.SKIPPED, body, "CouchDB: Document already exists — skipped.");
            }
            else {
                System.err.println("CouchDB request failed with status: " + statusCode);
                return new Response(RequestStatus.FAILED, body, "CouchDB: Document already exists — skipped." +
                        statusCode);
            }
        }
        catch (Exception e) {
            System.err.println("Unknown CouchDB error: " + e.getMessage());
            return new Response(RequestStatus.FAILED, null, "CouchDB: Unknown error" + statusCode);

        }
    }
    public Response sendGet(String urn) {
        try {
            HttpGet get = communicationFactory.createHttpGet(urn);
            HttpResponse dbResponse = httpClient.execute(get);

            int statusCode = dbResponse.getStatusLine().getStatusCode();

            System.out.println("Status: " + dbResponse.getStatusLine());

            JsonNode responseBody = mapper.readTree(dbResponse.getEntity().getContent());

            System.out.println("GET response body: " + responseBody);

            if (statusCode == 200) {
                System.out.println("CouchDB: Document get successfully.");
                return new Response(RequestStatus.SUCCESSFUL, responseBody, "Document get successfully");
            }
            else {
                System.err.println("CouchDB request failed with status: " + statusCode);
                return new Response(RequestStatus.FAILED, responseBody, "Error");
            }
        }
        catch (Exception e){
            System.err.println("Unknown CouchDB error: " + e.getMessage());
            return new Response(RequestStatus.FAILED, null, "Unknown Error");
        }
    }
}
