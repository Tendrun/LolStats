package com.backendwebsite.DatabaseBuilder.Builder;

import com.backendwebsite.DatabaseBuilder.Context.BuildMatchContext;
import com.backendwebsite.DatabaseBuilder.Pipeline.Pipeline;
import com.backendwebsite.DatabaseBuilder.Step.IStep;
import com.backendwebsite.DatabaseBuilder.Step.Players.DeduplicatePlayersStep;
import com.backendwebsite.DatabaseBuilder.Step.Players.FetchPlayersStep;
import com.backendwebsite.DatabaseBuilder.Step.Players.UpsertPlayersStep;
import com.backendwebsite.DatabaseBuilder.Step.Players.ValidatePlayersStep;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GetPlayersBuilder implements IBuilder<BuildMatchContext> {

    private final List<IStep<BuildMatchContext>> steps;

    public GetPlayersBuilder(DeduplicatePlayersStep deduplicatePlayersStep, FetchPlayersStep fetchPlayersStep,
                             ValidatePlayersStep validatePlayersStep, UpsertPlayersStep upsertPlayersStep) {
        this.steps = List.of(fetchPlayersStep, validatePlayersStep ,deduplicatePlayersStep, upsertPlayersStep);
    }

    @Override
    public void build(BuildMatchContext context) {
        getPlayersFromRiot(context);
    }

    @Override
    public Object getResult() {
        return null;
    }

    public void getPlayersFromRiot(BuildMatchContext context) {
        Pipeline.executeSteps(steps, context);
    }
}
