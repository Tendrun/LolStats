package com.backendwebsite.DatabaseBuilder.Context;

import com.backendwebsite.DatabaseBuilder.DTO.RiotApi.MatchDetails.MatchDTO;
import com.backendwebsite.DatabaseBuilder.Step.Log.StepLog;

import java.util.*;

public class FetchMatchDetailsContext implements IContext {

    public enum teamPosition {
        TOP,
        JUNGLE,
        MIDDLE,
        BOTTOM,
        UTILITY
    }

    public Set<String> matchIds = new LinkedHashSet<>();
    public HashMap<String, List<StepLog>> logs = new HashMap<>();
    public List<MatchDTO> existingMatchDetails = new ArrayList<>();
    public List<MatchDTO> validatedMatchDetails = new ArrayList<>();
    public List<MatchDTO> fetchedMatchDetails = new ArrayList<>();
    public List<MatchDTO> finalMatchDetails = new ArrayList<>();

    public enum Region {
        europe
    }

    public int playerMatchLimit;
    public Region region;

    public FetchMatchDetailsContext(int playerMatchLimit, Region region){
        this.playerMatchLimit = playerMatchLimit;
        this.region = region;
    }
}
