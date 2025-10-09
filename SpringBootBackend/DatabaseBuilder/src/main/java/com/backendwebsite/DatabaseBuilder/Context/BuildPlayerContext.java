package com.backendwebsite.DatabaseBuilder.Context;

public class BuildPlayerContext implements IContext {

    public final String region, tier, division, queue, page;
    public BuildPlayerContext(String region, String tier, String division, String queue, String page) {
        this.region = region; this.tier = tier; this.division = division; this.queue = queue; this.page = page;
    }
}
