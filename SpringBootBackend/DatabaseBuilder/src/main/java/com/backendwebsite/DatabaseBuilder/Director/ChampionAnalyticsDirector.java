package com.backendwebsite.DatabaseBuilder.Director;

import com.backendwebsite.DatabaseBuilder.Builder.ChampionAnalyticsBuilder;
import com.backendwebsite.DatabaseBuilder.Context.BuildChampionAnalyticsContext;
import org.springframework.stereotype.Component;

@Component
public class ChampionAnalyticsDirector {
    private final ChampionAnalyticsBuilder championAnalyticsBuilder;
    public ChampionAnalyticsDirector(ChampionAnalyticsBuilder championAnalyticsBuilder) { this.championAnalyticsBuilder = championAnalyticsBuilder; }
    public void startWork(BuildChampionAnalyticsContext context) {
        championAnalyticsBuilder.build(context);
    }
}
