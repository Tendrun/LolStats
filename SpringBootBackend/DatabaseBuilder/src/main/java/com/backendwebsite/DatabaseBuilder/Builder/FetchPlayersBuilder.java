package com.backendwebsite.DatabaseBuilder.Builder;

import com.backendwebsite.DatabaseBuilder.Context.BuildPlayerContext;
import com.backendwebsite.DatabaseBuilder.Pipeline.Pipeline;
import com.backendwebsite.DatabaseBuilder.Step.IStep;
import com.backendwebsite.DatabaseBuilder.Step.FetchPlayers.*;
import com.backendwebsite.DatabaseBuilder.Step.StepsOrder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class FetchPlayersBuilder implements IBuilder<BuildPlayerContext, Object> {
    private final StepsOrder<BuildPlayerContext> steps;

    public FetchPlayersBuilder(GetPlayersFromCouchDBStep getPlayersFromCouchDBStep, DeduplicatePlayersStep deduplicatePlayersStep,
                               PullPlayersFromRiotStep pullPlayersFromRiotStep, ValidatePlayersStep validatePlayersStep,
                               UpsertPlayersStep upsertPlayersStep) {

        List<IStep<BuildPlayerContext>> stepsList = new ArrayList<>();

        stepsList.add(getPlayersFromCouchDBStep);
        stepsList.add(pullPlayersFromRiotStep);
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
