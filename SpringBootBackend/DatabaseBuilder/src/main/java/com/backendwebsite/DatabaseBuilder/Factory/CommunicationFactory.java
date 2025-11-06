package com.backendwebsite.DatabaseBuilder.Factory;

import com.backendwebsite.DatabaseBuilder.Factory.Request.IRequest;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.Base64;

import static com.backendwebsite.Helper.KeysLoader.loadSecretValue;

@Component
public class CommunicationFactory {

    final String region;
    final String apiRiotGamesKey;
    final String couchDbUrl;

    public CommunicationFactory() {
        this.apiRiotGamesKey = loadSecretValue("RIOTGAMES_API_KEY");
        this.region = "eun1";
        this.couchDbUrl = loadSecretValue("COUCHDB_URL");
    }

    ///
    /// RIOT API Requests
    ///
    public HttpRequest createRequest(String urn, String region) {
        String uri = "https://" + region + ".api.riotgames.com" + urn;
        IRequest request = new Request();
        return request.create(apiRiotGamesKey, uri);
    }

    ///
    /// Clients
    ///
    public CloseableHttpClient createCloseableHttpClient() {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        return httpClient;
    }

    public HttpClient createHttpClient() {
        HttpClient httpClient = HttpClient.newHttpClient();
        return httpClient;
    }

    ///
    /// CouchDB Requests
    ///
    public HttpPut createHttpPut(String urn) {
        HttpPut put = new HttpPut(couchDbUrl + urn);
        String encodedAuth = getEncodedAuth();
        put.setHeader("Content-type", "application/json");
        put.setHeader("Authorization", "Basic " + encodedAuth);

        return put;
    }

    public HttpPost createHttpPost(String urn) {
        HttpPost post = new HttpPost(couchDbUrl + urn);
        String encodedAuth = getEncodedAuth();
        post.setHeader("Content-type", "application/json");
        post.setHeader("Authorization", "Basic " + encodedAuth);

        return post;
    }

    public HttpGet createHttpGet(String urn) {
        HttpGet get = new HttpGet(couchDbUrl + urn);
        String encodedAuth = getEncodedAuth();
        get.setHeader("Content-type", "application/json");
        get.setHeader("Authorization", "Basic " + encodedAuth);

        return get;
    }

    String getEncodedAuth(){
        String auth = "admin:admin";
        return Base64.getEncoder().encodeToString(auth.getBytes());
    }

    static class Request implements IRequest {
        public HttpRequest create(String riotGamesApiKey, String url){
            return HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("X-Riot-Token", riotGamesApiKey)
                    .build();
        }
    }
}
