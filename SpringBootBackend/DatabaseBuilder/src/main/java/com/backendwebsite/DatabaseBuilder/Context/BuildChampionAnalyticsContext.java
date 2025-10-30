package com.backendwebsite.DatabaseBuilder.Context;

import com.backendwebsite.DatabaseBuilder.Constant.ChampionStatsMap;
import com.backendwebsite.DatabaseBuilder.DTO.RiotApi.MatchDetails.MatchDTO;
import com.backendwebsite.DatabaseBuilder.Step.Log.StepLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BuildChampionAnalyticsContext implements IContext {
    public int limitMatches;
    public HashMap<String, List<StepLog>> logs = new HashMap<>();
    public List<MatchDTO> matchDetails = new ArrayList<>();
    public ChampionStatsMap championStatsMap = new ChampionStatsMap();
    public BuildChampionAnalyticsContext(int limitMatches){
        this.limitMatches = limitMatches;
    }
}
