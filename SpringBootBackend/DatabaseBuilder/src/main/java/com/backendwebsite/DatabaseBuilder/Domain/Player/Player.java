package com.backendwebsite.DatabaseBuilder.Domain.Player;

public class Player {
    public String _id;
    public final String queueType;
    public final String tier;
    public final String rank;
    public final String puuid;
    public final int leaguePoints;
    public final int wins;
    public final int losses;
    public final boolean veteran;
    public final boolean inactive;
    public final boolean freshBlood;
    public final boolean hotStreak;
    public String region;

    public Player(String _id, String queueType, String tier,
                  String rank, String puuid, int leaguePoints, int wins, int losses,
                  boolean veteran, boolean inactive, boolean freshBlood, boolean hotStreak, String region) {
        this._id = _id;
        this.queueType = queueType;
        this.tier = tier;
        this.rank = rank;
        this.puuid = puuid;
        this.leaguePoints = leaguePoints;
        this.wins = wins;
        this.losses = losses;
        this.veteran = veteran;
        this.inactive = inactive;
        this.freshBlood = freshBlood;
        this.hotStreak = hotStreak;
        this.region = region;
    }
}
