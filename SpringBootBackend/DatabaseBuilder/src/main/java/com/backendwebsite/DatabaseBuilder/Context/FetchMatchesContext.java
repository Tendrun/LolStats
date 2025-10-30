package com.backendwebsite.DatabaseBuilder.Context;

import com.backendwebsite.DatabaseBuilder.Domain.Match.PlayerMatches;
import com.backendwebsite.DatabaseBuilder.Step.Log.StepLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FetchMatchesContext implements IContext {
    public enum Region {
        europe,
    }

    public enum Type {
        ranked,
        normal,
        tourney,
        tutorial
    }

    public enum Tier {
        IRON,
        BRONZE,
        SILVER,
        GOLD,
        PLATINUM,
        EMERALD,
        DIAMOND
    }
    // TO DO
    // Player Matches need additional fields region, average rank

    public int playerLimit;
    public final Region region;
    public final Tier tier;
    public final Type type;
    public List<String> puuids = new ArrayList<>();
    public HashMap<String, PlayerMatches> existingMatches = new HashMap<>();
    public HashMap<String, PlayerMatches> fetchedMatches = new HashMap<>();
    public List<PlayerMatches> finalPlayerMatches = new ArrayList<>();
    public HashMap<String, List<StepLog>> logs = new HashMap<>();

    public FetchMatchesContext(Region region, int playerLimit, Type type, Tier tier) {
        this.region = region;
        this.playerLimit = playerLimit;
        this.type = type;
        this.tier = tier;
    }
}
