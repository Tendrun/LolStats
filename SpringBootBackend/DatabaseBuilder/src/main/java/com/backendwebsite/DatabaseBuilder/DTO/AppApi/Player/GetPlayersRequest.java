package com.backendwebsite.DatabaseBuilder.DTO.AppApi.Player;

public record GetPlayersRequest(String region, String tier, String division, String queue, String page) { }