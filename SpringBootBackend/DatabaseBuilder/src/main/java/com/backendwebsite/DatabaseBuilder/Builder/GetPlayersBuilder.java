package com.backendwebsite.DatabaseBuilder.Builder;

import com.backendwebsite.DatabaseBuilder.Context.BuildPlayerContext;
import com.backendwebsite.DatabaseBuilder.Pipeline.Pipeline;
import com.backendwebsite.DatabaseBuilder.Step.IStep;
import com.backendwebsite.DatabaseBuilder.Step.Players.*;
import com.backendwebsite.DatabaseBuilder.Step.StepsOrder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class GetPlayersBuilder implements IBuilder<BuildPlayerContext, Object> {
    private final StepsOrder<BuildPlayerContext> steps;

    public GetPlayersBuilder(GetPlayersFromCouchDBStep getPlayersFromCouchDBStep, DeduplicatePlayersStep deduplicatePlayersStep,
                             FetchPlayersStep fetchPlayersStep, ValidatePlayersStep validatePlayersStep,
                             UpsertPlayersStep upsertPlayersStep) {

        List<IStep<BuildPlayerContext>> stepsList = new ArrayList<>();

        stepsList.add(getPlayersFromCouchDBStep);
        stepsList.add(fetchPlayersStep);
        stepsList.add(validatePlayersStep);
        stepsList.add(deduplicatePlayersStep);
        stepsList.add(upsertPlayersStep);

        this.steps = new StepsOrder<>(stepsList);
    }

    @Override
    public void build(BuildPlayerContext context) {
        getPlayersFromRiot(context);
    }

    @Override
    public Object getResult() {
        return null;
    }

    public void getPlayersFromRiot(BuildPlayerContext context) {
        Pipeline.executeSteps(steps, context);
    }
}
