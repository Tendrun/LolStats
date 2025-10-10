package com.backendwebsite.DatabaseBuilder.Builder;

import com.backendwebsite.DatabaseBuilder.Context.BuildPlayerContext;
import com.backendwebsite.DatabaseBuilder.Pipeline.Pipeline;
import com.backendwebsite.DatabaseBuilder.Step.IStep;
import com.backendwebsite.DatabaseBuilder.Step.Players.*;
import com.backendwebsite.DatabaseBuilder.Step.StepsOrder;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class GetPlayersBuilder implements IBuilder<BuildPlayerContext, Object> {

    private final StepsOrder<BuildPlayerContext> steps;

    public GetPlayersBuilder(GetPlayersFromCouchDBStep getPlayersFromCouchDBStep, DeduplicatePlayersStep deduplicatePlayersStep,
                             FetchPlayersStep fetchPlayersStep, ValidatePlayersStep validatePlayersStep,
                             UpsertPlayersStep upsertPlayersStep) {

        Map<Integer, IStep<BuildPlayerContext>> map = new LinkedHashMap<>();

        map.put(1, getPlayersFromCouchDBStep);
        map.put(2, fetchPlayersStep);
        map.put(3, validatePlayersStep);
        map.put(4, deduplicatePlayersStep);
        map.put(5, upsertPlayersStep);


        this.steps = new StepsOrder<>(map);
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
