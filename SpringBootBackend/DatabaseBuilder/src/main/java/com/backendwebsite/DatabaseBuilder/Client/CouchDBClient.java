package com.backendwebsite.DatabaseBuilder.Client;

import com.backendwebsite.DatabaseBuilder.Exception.RiotApiException;
import com.backendwebsite.DatabaseBuilder.Factory.CommunicationFactory;
import com.backendwebsite.DatabaseBuilder.Helper.DatabaseHelper;
import com.backendwebsite.DatabaseBuilder.Step.StepsOrder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.http.HttpClient;
import com.backendwebsite.DatabaseBuilder.Step.StepsOrder.RequestStatus;

@Component
public class CouchDBClient {
    private final CommunicationFactory communicationFactory;
    public CouchDBClient(CommunicationFactory communicationFactory) {
        this.communicationFactory = communicationFactory;
    }

    public RequestStatus sendPut(String urn, String region, String json)  {
        try(CloseableHttpClient httpClient = communicationFactory.createCloseableHttpClient(region)) {
            HttpPut put = communicationFactory.createHttpPut(urn, region);
            put.setEntity(new StringEntity(json));

            HttpResponse dbResponse = httpClient.execute(put);
            int statusCode = dbResponse.getStatusLine().getStatusCode();

            System.out.println("Status: " + dbResponse.getStatusLine());

            String responseBody = DatabaseHelper.reader(new BufferedReader(
                    new InputStreamReader(dbResponse.getEntity().getContent())));

            System.out.println("PUT response body: " + responseBody);

            if (statusCode == 201) {
                System.out.println("CouchDB: Document saved successfully.");
                return RequestStatus.SUCCESSFUL;
            }
            else if (statusCode == 409) {
                System.out.println("CouchDB: Document already exists — skipped.");
                return RequestStatus.SKIPPED;
            }
            else {
                System.err.println("CouchDB request failed with status: " + statusCode);
                return RequestStatus.FAILED;
            }
        }
        catch (Exception e) {
            System.err.println("Unknown CouchDB error: " + e.getMessage());
            return RequestStatus.FAILED;
        }
    }
}
