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
    // TO DO
    // Player Matches need additional fields region, average rank

    public int playerLimit;
    public final Region region;
    public final Type type;
    public List<String> puuids = new ArrayList<>();
    public HashMap<String, PlayerMatches> existingMatches = new HashMap<>();
    public HashMap<String, PlayerMatches> fetchedMatches = new HashMap<>();
    public List<PlayerMatches> finalPlayerMatches = new ArrayList<>();
    public List<StepLog> logs = new ArrayList<>();

    public FetchMatchesContext(Region region, int playerLimit, Type type) {
        this.region = region;
        this.playerLimit = playerLimit;
        this.type = type;
    }
}
