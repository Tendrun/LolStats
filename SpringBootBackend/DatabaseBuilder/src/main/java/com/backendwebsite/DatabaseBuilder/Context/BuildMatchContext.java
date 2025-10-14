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

    public final Region region;
    public List<String> puuids = new ArrayList<>();
    public HashMap<String, List<PlayerMatches>> fetchedMatches = new HashMap<>();
    public List<PlayerMatches> finalPlayerMatches = new ArrayList<>();
    public List<String> deduplicateMatches = new ArrayList<>();
    public HashMap<String, List<PlayerMatches>> existingMatches = new HashMap<>();
    public List<StepLog> logs = new ArrayList<>();

    public BuildMatchContext(Region region) {
        this.region = region;
    }
}
