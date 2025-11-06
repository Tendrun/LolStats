package com.backendwebsite.DatabaseBuilder.Step.FetchMatchDetails;

import com.backendwebsite.DatabaseBuilder.Context.FetchMatchDetailsContext;
import com.backendwebsite.DatabaseBuilder.DTO.RiotApi.MatchDetails.MatchDTO;
import com.backendwebsite.DatabaseBuilder.Step.IStep;
import com.backendwebsite.DatabaseBuilder.Step.Log.StepLog;
import com.backendwebsite.DatabaseBuilder.Step.StepsOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class DeduplicateMatchDetailsStep implements IStep<FetchMatchDetailsContext> {
    private static final Logger logger = LoggerFactory.getLogger(DeduplicateMatchDetailsStep.class);

    @Override
    public void execute(FetchMatchDetailsContext context) {
        long startTime = System.currentTimeMillis();
        try {
            Set<String> existingIds = context.existingMatchDetails.stream()
                    .map(p -> p.metadata.matchId)
                    .collect(Collectors.toSet());

            List<MatchDTO> finalMatchDetails = context.validatedMatchDetails.stream()
                    .filter(p -> !existingIds.contains(p.metadata.matchId))
                    .toList();

            context.finalMatchDetails = new ArrayList<>(finalMatchDetails);

            // add success log to context (logs is a Map<String, List<StepLog>>)
            context.logs.computeIfAbsent(getClass().getSimpleName(), k -> new ArrayList<>())
                    .add(new StepLog(StepsOrder.RequestStatus.SUCCESSFUL,
                            this.getClass().getSimpleName(),
                            "Deduplication completed. Final match details count: " + context.finalMatchDetails.size(), System.currentTimeMillis() - startTime, ""));

            logger.info("Deduplication completed. Final match details count: {}", context.finalMatchDetails.size());
        } catch (Exception e) {
            // add failed log to context
            context.logs.computeIfAbsent(getClass().getSimpleName(), k -> new ArrayList<>())
                    .add(new StepLog(StepsOrder.RequestStatus.FAILED,
                            this.getClass().getSimpleName(),
                            "Exception during deduplication: " + e.getMessage(), System.currentTimeMillis() - startTime, ""));
            logger.error("Exception during deduplication", e);
        }
    }
}
