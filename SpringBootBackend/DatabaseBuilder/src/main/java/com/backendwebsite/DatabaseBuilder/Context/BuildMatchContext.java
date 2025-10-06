package com.backendwebsite.DatabaseBuilder.Context;

import com.backendwebsite.DatabaseBuilder.Step.IStep;
import com.backendwebsite.DatabaseBuilder.Step.Players.FetchPlayersStep;

import java.util.ArrayList;
import java.util.List;

public class BuildMatchContext implements IContext {

    private final String region, tier, division, queue;
    public BuildMatchContext(String region, String tier, String division, String queue) {
        this.region = region; this.tier = tier; this.division = division; this.queue = queue;
    }
    public String getRegion() { return region; }
    public String getTier() { return tier; }
    public String getDivision() { return division; }
    public String getQueue() { return queue; }
}
