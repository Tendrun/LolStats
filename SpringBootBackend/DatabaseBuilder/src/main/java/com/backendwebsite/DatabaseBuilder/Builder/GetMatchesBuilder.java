package com.backendwebsite.DatabaseBuilder.Builder;

import com.backendwebsite.DatabaseBuilder.Context.BuildMatchContext;
import com.backendwebsite.DatabaseBuilder.Context.BuildPlayerContext;
import com.backendwebsite.DatabaseBuilder.Pipeline.Pipeline;
import com.backendwebsite.DatabaseBuilder.Step.IStep;
import com.backendwebsite.DatabaseBuilder.Step.Match.FetchMatchesStep;
import com.backendwebsite.DatabaseBuilder.Step.Match.GetMatchesFromCouchDBStep;
import com.backendwebsite.DatabaseBuilder.Step.Match.GetPlayersPuuidFromCouchDB;
import com.backendwebsite.DatabaseBuilder.Step.Players.GetPlayersFromCouchDBStep;
import com.backendwebsite.DatabaseBuilder.Step.StepsOrder;
import org.springframework.stereotype.Component;


import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
@Component
public class GetMatchesBuilder implements IBuilder<BuildMatchContext, Object> {

    private final StepsOrder<BuildMatchContext> steps;

    public GetMatchesBuilder(GetMatchesFromCouchDBStep getMatchesFromCouchDBStep, FetchMatchesStep fetchMatchesStep,
                             GetPlayersPuuidFromCouchDB getPlayersPuuidFromCouchDB) {

        List<IStep<BuildMatchContext>> stepsList = new ArrayList<>();

        stepsList.add(getPlayersPuuidFromCouchDB);
        //stepsList.add(getMatchesFromCouchDBStep);
        stepsList.add(fetchMatchesStep);


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
