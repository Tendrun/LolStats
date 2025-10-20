package com.backendwebsite.DatabaseBuilder.Builder;

import com.backendwebsite.DatabaseBuilder.Context.FetchMatchesContext;
import com.backendwebsite.DatabaseBuilder.Pipeline.Pipeline;
import com.backendwebsite.DatabaseBuilder.Step.FetchMatchDetails.ValidateMatchDetailsStep;
import com.backendwebsite.DatabaseBuilder.Step.FetchPlayers.ValidatePlayersStep;
import com.backendwebsite.DatabaseBuilder.Step.IStep;
import com.backendwebsite.DatabaseBuilder.Step.FetchMatch.*;
import com.backendwebsite.DatabaseBuilder.Step.StepsOrder;
import org.springframework.stereotype.Component;


import java.util.ArrayList;
import java.util.List;

@Component
public class FetchMatchesBuilder implements IBuilder<FetchMatchesContext> {

    private final StepsOrder<FetchMatchesContext> steps;

    public FetchMatchesBuilder(GetMatchesFromCouchDBStep getMatchesFromCouchDBStep, PullMatchesFromRiotStep pullMatchesFromRiotStep,
                               GetPlayerPuuidsFromCouchDB getPlayerPuuidsFromCouchDB,
                               DeduplicateMatchesStep deduplicateMatchesStep, UpsertMatchesStep upsertMatchesStep) {

        List<IStep<FetchMatchesContext>> stepsList = new ArrayList<>();

        stepsList.add(getPlayerPuuidsFromCouchDB);
        stepsList.add(getMatchesFromCouchDBStep);
        stepsList.add(pullMatchesFromRiotStep);
        stepsList.add(deduplicateMatchesStep);
        stepsList.add(upsertMatchesStep);
        this.steps = new StepsOrder<>(stepsList);
    }


    @Override
    public void build(FetchMatchesContext context) {
        getMatchesFromRiot(context);
    }
    public void getMatchesFromRiot(FetchMatchesContext context) {
        Pipeline.executeSteps(steps, context);
    }
}
