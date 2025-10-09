package com.backendwebsite.DatabaseBuilder.DTO.getPlayers;

public class getPlayersRequest {
    public final String region, tier, division, queue, page;

    public getPlayersRequest(String region, String tier, String division, String queue, String page) {
        this.region = region;
        this.tier = tier;
        this.division = division;
        this.queue = queue;
        this.page = page;
    }
}
