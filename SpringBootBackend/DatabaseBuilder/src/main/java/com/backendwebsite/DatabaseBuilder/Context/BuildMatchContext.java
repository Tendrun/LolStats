package com.backendwebsite.DatabaseBuilder.Context;

import com.backendwebsite.DatabaseBuilder.Domain.Match.PlayerMatches;
import com.backendwebsite.DatabaseBuilder.Step.Log.StepLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BuildMatchContext implements IContext {
    public enum Region {
        europe,
    }
    // TO DO
    // Player Matches need additional fields region, average rank

    public int playerLimit;
    public final Region region;
    public List<String> puuids = new ArrayList<>();
    public HashMap<String, PlayerMatches> existingMatches = new HashMap<>();
    public HashMap<String, PlayerMatches> fetchedMatches = new HashMap<>();
    public List<PlayerMatches> finalPlayerMatches = new ArrayList<>();
    public List<StepLog> logs = new ArrayList<>();

    public BuildMatchContext(Region region, int playerLimit) {
        this.region = region;
        this.playerLimit = playerLimit;
    }
}
