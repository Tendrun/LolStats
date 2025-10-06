package com.backendwebsite.DatabaseBuilder.Factory;

import com.backendwebsite.DatabaseBuilder.Factory.Request.IRequest;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Component;

import java.net.URI;
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

    public HttpRequest createRequest(String urn, String region) {
        String uri = "https://" + region + ".api.riotgames.com" + urn;
        System.out.println(uri);


        IRequest request = new Request();
        return request.create(apiRiotGamesKey, uri);
    }

    public CloseableHttpClient createClient(String region) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        return httpClient;
    }

    public HttpPut createHttpPut(String urn, String region) {
        HttpPut put = new HttpPut(couchDbUrl + urn);
        put.setHeader("Content-type", "application/json");
        String auth = "admin:admin"; // lub z pliku
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
        put.setHeader("Authorization", "Basic " + encodedAuth);

        return put;
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
