package com.backendwebsite.DatabaseBuilder.DTO.getPlayers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LeagueEntryDTO {
    public String _id; // CouchDB uses _id
    public final String leagueId;
    public final String queueType;
    public final String tier;
    public final String rank;
    public final String summonerId;
    public final String puuid;
    public final int leaguePoints;
    public final int wins;
    public final int losses;
    public final boolean veteran;
    public final boolean inactive;
    public final boolean freshBlood;
    public final boolean hotStreak;

    public LeagueEntryDTO(String _id, String leagueId, String queueType, String tier,
                          String rank, String summonerId, String puuid,
                          int leaguePoints, int wins, int losses,
                          boolean veteran, boolean inactive, boolean freshBlood, boolean hotStreak) {
        this._id = _id;
        this.leagueId = leagueId;
        this.queueType = queueType;
        this.tier = tier;
        this.rank = rank;
        this.summonerId = summonerId;
        this.puuid = puuid;
        this.leaguePoints = leaguePoints;
        this.wins = wins;
        this.losses = losses;
        this.veteran = veteran;
        this.inactive = inactive;
        this.freshBlood = freshBlood;
        this.hotStreak = hotStreak;
    }
}
