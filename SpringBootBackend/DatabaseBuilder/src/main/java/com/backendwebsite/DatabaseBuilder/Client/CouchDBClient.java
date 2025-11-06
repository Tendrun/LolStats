package com.backendwebsite.DatabaseBuilder.Client;

import com.backendwebsite.DatabaseBuilder.Factory.CommunicationFactory;
import com.backendwebsite.DatabaseBuilder.Helper.DatabaseHelper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import com.backendwebsite.DatabaseBuilder.Step.StepsOrder.RequestStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class CouchDBClient {
    private final CommunicationFactory communicationFactory;
    private final ObjectMapper mapper;
    CloseableHttpClient httpClient;
    private static final Logger logger = LoggerFactory.getLogger(CouchDBClient.class);
    public CouchDBClient(ObjectMapper mapper, CommunicationFactory communicationFactory) {
        this.httpClient = communicationFactory.createCloseableHttpClient();
        this.mapper = mapper;
        this.communicationFactory = communicationFactory;
    }

    public record Response(RequestStatus status, @Nullable JsonNode body,  @Nullable String message, List<String> failedIds) { }

    public Response sendPut(String urn, String json)  {

        int statusCode = 0;

        try {
            HttpPut put = communicationFactory.createHttpPut(urn);
            put.setEntity(new StringEntity(json));

            HttpResponse dbResponse = httpClient.execute(put);
            statusCode = dbResponse.getStatusLine().getStatusCode();

            String responseBody = DatabaseHelper.reader(new BufferedReader(
                    new InputStreamReader(dbResponse.getEntity().getContent())));

            if (statusCode == 201 || statusCode == 202) {
                return new Response(RequestStatus.SUCCESSFUL, null, "CouchDB: Document created successfully", Collections.emptyList());
            }
            else if (statusCode == 409) {
                return new Response(RequestStatus.SKIPPED, null, "Document already exists — skipped.", Collections.emptyList());
            }
            else {
                String msg = "CouchDB request failed with status: " + statusCode + " responseBody=" + responseBody;
                logger.warn(msg);
                return new Response(RequestStatus.FAILED, null, msg, Collections.emptyList());
            }
        }
        catch (Exception e) {
            logger.error("Exception during PUT to {}", urn, e);
            return new Response(RequestStatus.FAILED, null, "Unknown CouchDB error: " + statusCode, Collections.emptyList());
        }
    }

    public Response sendPost(String urn, String json) {

        int statusCode = 0;

        try {
            HttpPost post = communicationFactory.createHttpPost(urn);
            post.setEntity(new StringEntity(json));

            HttpResponse dbResponse = httpClient.execute(post);
            statusCode = dbResponse.getStatusLine().getStatusCode();

            String responseBody = DatabaseHelper.reader(new BufferedReader(
                    new InputStreamReader(dbResponse.getEntity().getContent())));

            JsonNode body = mapper.readTree(responseBody);

            try {
                boolean hasError = false;
                java.util.List<String> failedIds = new java.util.ArrayList<>();

                if (body.isArray()) {
                    for (JsonNode item : body) {
                        if (item.has("error")) {
                            hasError = true;
                            String id = item.has("id") ? item.get("id").asText() : "<no-id>";
                            failedIds.add(id);
                        }
                    }
                } else if (body.isObject()) {
                    if (body.has("error")) {
                        String topError = body.has("error") ? body.get("error").asText() : "<no-error>";
                        String topReason = body.has("reason") ? body.get("reason").asText() : "";
                        // TO DO - try to find failing ids inside the bulk JSON by probing individual docs
                        java.util.List<String> probedFailedIds = tryFindFailingIdsInBulk(urn, json);
                        String help = probedFailedIds.isEmpty() ? "(no failing doc id found by probing)" : ("failedIds=" + probedFailedIds);
                        String msg = "CouchDB request-level error: " + topError + " reason=" + topReason + " " + help;
                        logger.error(msg + " responseBody=" + responseBody);
                        return new Response(RequestStatus.FAILED, body, msg, probedFailedIds);
                    }
                }

                if (hasError) {
                    return new Response(RequestStatus.FAILED, body,
                            "CouchDB: Some documents had errors during POST. Errors count: " + failedIds.size(), failedIds);
                }
            } catch (Exception parseEx) {
                logger.warn("Failed to parse POST response for errors (urn={}). Treating as successful. responseBody={}", urn, responseBody, parseEx);
            }


            if (statusCode == 200) {
                return new Response(RequestStatus.SUCCESSFUL, body, "CouchDB: Document post successfully", Collections.emptyList());
            }
            else if (statusCode == 201) {
                return new Response(RequestStatus.SUCCESSFUL, body, " CouchDB: Document post successfully", Collections.emptyList());
            }
            else if (statusCode == 409) {
                return new Response(RequestStatus.SKIPPED, body, "CouchDB: Document already exists — skipped.", Collections.emptyList());
            }
            else {
                return new Response(RequestStatus.FAILED, body, "CouchDB: Document post failed. status=" +
                        statusCode + " responseBody=" + responseBody, Collections.emptyList());
            }
        }
        catch (Exception e) {
            logger.error("Exception during POST to {}", urn, e);
            return new Response(RequestStatus.FAILED, null, "CouchDB: Unknown error" + statusCode, Collections.emptyList());

        }
    }

    // Probe individual docs inside the provided bulk JSON to find which ones cause a request-level failure
    private java.util.List<String> tryFindFailingIdsInBulk(String urn, String bulkJson) {
        java.util.List<String> result = new java.util.ArrayList<>();
        try {
            JsonNode root = mapper.readTree(bulkJson);
            JsonNode docs = root.has("docs") ? root.get("docs") : null;
            if (docs == null || !docs.isArray()) return result;

            for (JsonNode doc : docs) {
                try {
                    // Build single-doc bulk payload
                    String single = mapper.writeValueAsString(mapper.createObjectNode().set("docs", mapper.createArrayNode().add(doc)));
                    HttpPost p = communicationFactory.createHttpPost(urn);
                    p.setEntity(new StringEntity(single));
                    HttpResponse r = httpClient.execute(p);
                    String resp = DatabaseHelper.reader(new BufferedReader(new InputStreamReader(r.getEntity().getContent())));
                    // Parse response: if array and first item has error -> record id
                    JsonNode parsed = mapper.readTree(resp);
                    if (parsed.isArray() && parsed.size() > 0) {
                        JsonNode item = parsed.get(0);
                        if (item.has("error")) {
                            String id = item.has("id") ? item.get("id").asText() : (doc.has("_id") ? doc.get("_id").asText() : "<no-id>");
                            result.add(id);
                        }
                    } else if (parsed.isObject() && parsed.has("error")) {
                        // some CouchDB responses may return object-level error even for single doc
                        String id = doc.has("_id") ? doc.get("_id").asText() : "<no-id>";
                        result.add(id);
                    }
                } catch (Exception ex) {
                    logger.debug("Probing single doc failed, skipping: {}", ex.toString());
                }
            }
        } catch (Exception e) {
            logger.debug("Failed to parse bulk JSON while probing: {}", e.toString());
        }
        return result;
    }

    public Response sendGet(String urn) {
        try {
            HttpGet get = communicationFactory.createHttpGet(urn);
            HttpResponse dbResponse = httpClient.execute(get);

            int statusCode = dbResponse.getStatusLine().getStatusCode();

            JsonNode responseBody = mapper.readTree(dbResponse.getEntity().getContent());

            if (statusCode == 200) {
                return new Response(RequestStatus.SUCCESSFUL, responseBody, "Document get successfully", Collections.emptyList());
            }
            else {
                return new Response(RequestStatus.FAILED, responseBody, "Error", Collections.emptyList());
            }
        }
        catch (Exception e){
            logger.error("Exception during GET to {}", urn, e);
            return new Response(RequestStatus.FAILED, null, "Unknown Error", Collections.emptyList());
        }
    }
}
