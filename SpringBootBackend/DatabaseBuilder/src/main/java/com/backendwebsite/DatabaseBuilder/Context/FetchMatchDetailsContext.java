package com.backendwebsite.DatabaseBuilder.Context;

import com.backendwebsite.DatabaseBuilder.Step.Log.StepLog;

import java.util.ArrayList;
import java.util.List;

public class FetchMatchDetailsContext implements IContext {
    public List<String> matchIds = new ArrayList<>();
    public List<StepLog> logs = new ArrayList<>();
    public List<String> fetchedMatches = new ArrayList<>();
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
