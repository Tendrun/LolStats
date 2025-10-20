package com.backendwebsite.DatabaseBuilder.Builder;

import com.backendwebsite.DatabaseBuilder.Context.FetchMatchDetailsContext;
import com.backendwebsite.DatabaseBuilder.Pipeline.Pipeline;
import com.backendwebsite.DatabaseBuilder.Step.FetchMatchDetails.*;
import com.backendwebsite.DatabaseBuilder.Step.IStep;
import com.backendwebsite.DatabaseBuilder.Step.StepsOrder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class FetchMatchDetailsBuilder implements IBuilder<FetchMatchDetailsContext> {
    private final StepsOrder<FetchMatchDetailsContext> steps;

    public FetchMatchDetailsBuilder(FetchMatchDetailsRiotStep fetchMatchDetailsRiotStep,
                                    GetPlayerMatchIdsFromCouchDBStep getPlayerMatchIdsFromCouchDBStep,
                                    UpsertMatchDetailsStep upsertMatchDetailsStep,
                                    DeduplicateMatchDetailsStep deduplicateMatchDetailsStep,
                                    ValidateMatchDetailsStep validateMatchDetailsStep,
                                    GetMatchDetailsCouchDBStep getMatchDetailsCouchDBStep) {

        List<IStep<FetchMatchDetailsContext>> stepsList = new ArrayList<>();

        stepsList.add(getPlayerMatchIdsFromCouchDBStep);
        stepsList.add(fetchMatchDetailsRiotStep);
        stepsList.add(getMatchDetailsCouchDBStep);
        stepsList.add(validateMatchDetailsStep);
        stepsList.add(deduplicateMatchDetailsStep);
        stepsList.add(upsertMatchDetailsStep);
        this.steps = new StepsOrder<>(stepsList);
    }

    @Override
    public void build(FetchMatchDetailsContext context) {
        getMatchesDetailsFromRiot(context);
    }
    public void getMatchesDetailsFromRiot(FetchMatchDetailsContext context) {
        Pipeline.executeSteps(steps, context);
    }
}
