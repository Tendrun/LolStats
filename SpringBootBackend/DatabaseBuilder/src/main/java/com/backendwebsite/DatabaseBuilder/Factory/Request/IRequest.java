package com.backendwebsite.DatabaseBuilder.Factory.Request;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public interface IRequest {
    HttpRequest create(String riotGamesApiKey, String url);
}
