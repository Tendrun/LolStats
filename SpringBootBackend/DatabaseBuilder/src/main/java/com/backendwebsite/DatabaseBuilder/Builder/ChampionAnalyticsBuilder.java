package com.backendwebsite.DatabaseBuilder.Builder;

import com.backendwebsite.DatabaseBuilder.Context.BuildChampionAnalyticsContext;
import com.backendwebsite.DatabaseBuilder.Pipeline.Pipeline;
import com.backendwebsite.DatabaseBuilder.Step.ChampionAnalytics.CalculateChampStatsStep;
import com.backendwebsite.DatabaseBuilder.Step.ChampionAnalytics.GetMatchDetailsFromCouchDBStep;
import com.backendwebsite.DatabaseBuilder.Step.IStep;
import com.backendwebsite.DatabaseBuilder.Step.StepsOrder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ChampionAnalyticsBuilder implements IBuilder<BuildChampionAnalyticsContext> {

    private final StepsOrder<BuildChampionAnalyticsContext> steps;

    public ChampionAnalyticsBuilder(GetMatchDetailsFromCouchDBStep getMatchDetailsFromCouchDBStep,
                                    CalculateChampStatsStep calculateChampStatsStep) {

        List<IStep<BuildChampionAnalyticsContext>> stepsList = new ArrayList<>();

        stepsList.add(getMatchDetailsFromCouchDBStep);
        stepsList.add(calculateChampStatsStep);

        this.steps = new StepsOrder<>(stepsList);
    }

    @Override
    public void build(BuildChampionAnalyticsContext context) {
        getMatchesDetailsFromRiot(context);
    }
    public void getMatchesDetailsFromRiot(BuildChampionAnalyticsContext context) {
        Pipeline.executeSteps(steps, context);
    }
}
