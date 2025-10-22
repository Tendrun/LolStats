package com.backendwebsite.Api.DTO.ChampionDetails;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ChampionDetailsResponse {
    public int championId;
    public String name;
    public float winRate;
    public float banRate;
    public float pickRate;
    public int totalMatchesPicked;
    public int wonMatches;
    public int bannedMatches;

    public ChampionDetailsResponse(int championId, String name) {
        this.championId = championId;
        this.name = name;
        this.winRate = 0.0f;
        this.banRate = 0.0f;
        this.pickRate = 0.0f;
        this.totalMatchesPicked = 0;
        this.wonMatches = 0;
        this.bannedMatches = 0;
    }

    @JsonCreator
    public ChampionDetailsResponse(int championId, String name, float winRate, float banRate, float pickRate,
                                   int totalMatchesPicked, int wonMatches, int bannedMatches) {
        this.championId = championId;
        this.name = name;
        this.winRate = winRate;
        this.banRate = banRate;
        this.pickRate = pickRate;
        this.totalMatchesPicked = totalMatchesPicked;
        this.wonMatches = wonMatches;
        this.bannedMatches = bannedMatches;
    }
}
