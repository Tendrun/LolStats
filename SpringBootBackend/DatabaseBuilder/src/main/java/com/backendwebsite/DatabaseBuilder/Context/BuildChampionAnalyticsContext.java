package com.backendwebsite.DatabaseBuilder.Context;

public class BuildChampionAnalyticsContext implements IContext {
    int limitMatches;
    public BuildChampionAnalyticsContext(int limitMatches){
        this.limitMatches = limitMatches;
    }
}
