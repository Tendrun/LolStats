package com.backendwebsite.Api.DTO.ChampionDetails;

public class ChampionDetailsResponse {
    public int championID;
    public String championName;
    public float winRate;
    public float banRate;
    public float pickRate;
    public int totalMatchesPicked;
    public int wonMatches;
    public int bannedMatches;

    public ChampionDetailsResponse(int championID, String championName) {
        this.championID = championID;
        this.championName = championName;
        this.winRate = 0.0f;
        this.banRate = 0.0f;
        this.pickRate = 0.0f;
        this.totalMatchesPicked = 0;
        this.wonMatches = 0;
        this.bannedMatches = 0;
    }

    public ChampionDetailsResponse(int championID, String championName, float winRate, float banRate, float pickRate,
                           int totalMatchesPicked, int wonMatches, int bannedMatches) {
        this.championID = championID;
        this.championName = championName;
        this.winRate = winRate;
        this.banRate = banRate;
        this.pickRate = pickRate;
        this.totalMatchesPicked = totalMatchesPicked;
        this.wonMatches = wonMatches;
        this.bannedMatches = bannedMatches;
    }
}
