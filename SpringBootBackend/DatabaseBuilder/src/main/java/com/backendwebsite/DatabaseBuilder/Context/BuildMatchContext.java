package com.backendwebsite.DatabaseBuilder.Context;

import com.backendwebsite.DatabaseBuilder.Domain.Match.Match;
import com.backendwebsite.DatabaseBuilder.Step.Log.StepLog;

import java.util.ArrayList;
import java.util.List;

public class BuildMatchContext implements IContext {
    public enum Region {
        europe,
    }

    public final Region region;
    public List<String> puuids = new ArrayList<>();
    public List<String> fetchedMatches = new ArrayList<>();
    public List<Match> validatedMatches = new ArrayList<>();
    public List<Match> existingMatches = new ArrayList<>();
    public List<Match> finalMatches = new ArrayList<>();
    public List<StepLog> logs = new ArrayList<>();

    public BuildMatchContext(Region region) {
        this.region = region;
    }
}
