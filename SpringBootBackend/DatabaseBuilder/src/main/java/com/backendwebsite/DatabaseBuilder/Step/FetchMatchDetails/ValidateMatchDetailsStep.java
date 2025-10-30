package com.backendwebsite.DatabaseBuilder.Step.FetchMatchDetails;

import com.backendwebsite.DatabaseBuilder.Context.FetchMatchDetailsContext;
import com.backendwebsite.DatabaseBuilder.DTO.RiotApi.MatchDetails.MatchDTO;
import com.backendwebsite.DatabaseBuilder.Step.IStep;
import com.backendwebsite.DatabaseBuilder.Step.Log.StepLog;
import com.backendwebsite.DatabaseBuilder.Step.StepsOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ValidateMatchDetailsStep implements IStep<FetchMatchDetailsContext> {
    private static final Logger logger = LoggerFactory.getLogger(ValidateMatchDetailsStep.class);

    @Override
    public void execute(FetchMatchDetailsContext context) {
        try {
            for (MatchDTO match : context.fetchedMatchDetails) {

                if (match == null || match.metadata == null || match.metadata.matchId == null) {
                    // add failed log and skip
                    context.logs.computeIfAbsent(getClass().getSimpleName(), k -> new java.util.ArrayList<>())
                            .add(new StepLog(StepsOrder.RequestStatus.FAILED, this.getClass().getSimpleName(),
                                    "Invalid match details - missing metadata.matchId"));
                    logger.warn("Skipping invalid match details due to missing metadata.matchId");
                    continue;
                }

                match._id = "matchDetail:" + context.region + ":" + match.metadata.matchId;

                context.validatedMatchDetails.add(match);
            }

            context.logs.computeIfAbsent(getClass().getSimpleName(), k -> new java.util.ArrayList<>())
                    .add(new StepLog(StepsOrder.RequestStatus.SUCCESSFUL, this.getClass().getSimpleName(),
                            "Validation completed. Validated count: " + context.validatedMatchDetails.size()));

            logger.info("Validation completed. Validated count: {}", context.validatedMatchDetails.size());
        } catch (Exception e) {
            context.logs.computeIfAbsent(getClass().getSimpleName(), k -> new java.util.ArrayList<>())
                    .add(new StepLog(StepsOrder.RequestStatus.FAILED, this.getClass().getSimpleName(),
                            "Exception: "
                            + e.getMessage()));
            logger.error("Exception while validating match details", e);
        }
    }
}
