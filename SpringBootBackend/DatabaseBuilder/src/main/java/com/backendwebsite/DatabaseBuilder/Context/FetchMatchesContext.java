package com.backendwebsite.DatabaseBuilder.Context;

import com.backendwebsite.DatabaseBuilder.DTO.AppApi.Match.GetMatchesRequest;
import com.backendwebsite.DatabaseBuilder.Domain.Match.PlayerMatches;
import com.backendwebsite.DatabaseBuilder.Step.Log.StepLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FetchMatchesContext implements IContext {
    // TO DO
    // Player Matches need additional fields region, average rank

    public int playerLimit;
    public final GetMatchesRequest.Region region;
    public final GetMatchesRequest.Tier tier;
    public final GetMatchesRequest.Type type;
    public List<String> puuids = new ArrayList<>();
    public HashMap<String, PlayerMatches> existingMatches = new HashMap<>();
    public HashMap<String, PlayerMatches> fetchedMatches = new HashMap<>();
    public List<PlayerMatches> finalPlayerMatches = new ArrayList<>();
    public HashMap<String, List<StepLog>> logs = new HashMap<>();

    public FetchMatchesContext(GetMatchesRequest.Region region, int playerLimit, GetMatchesRequest.Type type,
                               GetMatchesRequest.Tier tier) {
        this.region = region;
        this.playerLimit = playerLimit;
        this.type = type;
        this.tier = tier;
    }
}
