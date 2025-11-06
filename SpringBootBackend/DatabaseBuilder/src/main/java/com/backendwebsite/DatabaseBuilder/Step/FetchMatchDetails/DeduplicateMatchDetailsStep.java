package com.backendwebsite.DatabaseBuilder.Step.FetchMatchDetails;

import com.backendwebsite.DatabaseBuilder.Context.FetchMatchDetailsContext;
import com.backendwebsite.DatabaseBuilder.DTO.RiotApi.MatchDetails.MatchDTO;
import com.backendwebsite.DatabaseBuilder.Step.IStep;
import com.backendwebsite.DatabaseBuilder.Step.Log.StepLog;
import com.backendwebsite.DatabaseBuilder.Step.StepsOrder;
import com.backendwebsite.DatabaseBuilder.Util.LogFormatter;
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

            int deduped = context.validatedMatchDetails.size() - context.finalMatchDetails.size();
            String summary = String.format("Deduplicated: %d kept: %d", deduped, context.finalMatchDetails.size());

            context.logs.computeIfAbsent(getClass().getSimpleName(), k -> new ArrayList<>())
                    .add(new StepLog(StepsOrder.RequestStatus.SUCCESSFUL,
                            this.getClass().getSimpleName(),
                            summary,
                            System.currentTimeMillis() - startTime));

            logger.info(LogFormatter.formatStepLog(getClass().getSimpleName(), StepsOrder.RequestStatus.SUCCESSFUL,
                    summary, System.currentTimeMillis() - startTime));
        } catch (Exception e) {
            context.logs.computeIfAbsent(getClass().getSimpleName(), k -> new ArrayList<>())
                    .add(new StepLog(StepsOrder.RequestStatus.FAILED,
                            this.getClass().getSimpleName(),
                            "Exception: " + e.getMessage(),
                            System.currentTimeMillis() - startTime));
            logger.error(LogFormatter.formatStepLog(getClass().getSimpleName(), StepsOrder.RequestStatus.FAILED,
                    "Exception during deduplication", System.currentTimeMillis() - startTime), e);
        }
    }
}
