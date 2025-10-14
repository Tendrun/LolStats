package com.backendwebsite.DatabaseBuilder.Builder;

import com.backendwebsite.DatabaseBuilder.Context.BuildMatchContext;
import com.backendwebsite.DatabaseBuilder.Pipeline.Pipeline;
import com.backendwebsite.DatabaseBuilder.Step.IStep;
import com.backendwebsite.DatabaseBuilder.Step.FetchMatch.*;
import com.backendwebsite.DatabaseBuilder.Step.StepsOrder;
import org.springframework.stereotype.Component;


import java.util.ArrayList;
import java.util.List;

@Component
public class FetchMatchesBuilder implements IBuilder<BuildMatchContext, Object> {

    private final StepsOrder<BuildMatchContext> steps;

    public FetchMatchesBuilder(GetMatchesFromCouchDBStep getMatchesFromCouchDBStep, PullMatchesFromRiotStep pullMatchesFromRiotStep,
                               GetPlayersPuuidFromCouchDB getPlayersPuuidFromCouchDB,
                               DeduplicateMatchesStep deduplicateMatchesStep, UpsertMatchesStep upsertMatchesStep) {

        List<IStep<BuildMatchContext>> stepsList = new ArrayList<>();

        stepsList.add(getPlayersPuuidFromCouchDB);
        stepsList.add(getMatchesFromCouchDBStep);
        stepsList.add(pullMatchesFromRiotStep);
        stepsList.add(deduplicateMatchesStep);
        stepsList.add(upsertMatchesStep);



        /*
        stepsList.add(getPlayersFromCouchDBStep);
        stepsList.add(fetchPlayersStep);
        stepsList.add(validatePlayersStep);
        stepsList.add(deduplicatePlayersStep);
        stepsList.add(upsertPlayersStep);
         */

        this.steps = new StepsOrder<>(stepsList);
    }


    @Override
    public void build(BuildMatchContext context) {
        getMatchesFromRiot(context);
    }

    @Override
    public Object getResult() {
        return null;
    }

    public void getMatchesFromRiot(BuildMatchContext context) {
        Pipeline.executeSteps(steps, context);
    }
}
