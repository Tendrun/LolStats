package com.backendwebsite.DatabaseBuilder.Step.FetchPlayers;

import com.backendwebsite.DatabaseBuilder.Context.FetchPlayersContext;
import com.backendwebsite.DatabaseBuilder.Domain.Player.Player;
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
public class DeduplicatePlayersStep implements IStep<FetchPlayersContext> {
    private static final Logger logger = LoggerFactory.getLogger(DeduplicatePlayersStep.class);

    @Override
    public void execute(FetchPlayersContext context) {
        long startTime = System.currentTimeMillis();
        try {
            Set<String> existingIds = context.existingPlayers.stream()
                    .map(p -> p._id)
                    .collect(Collectors.toSet());

            List<Player> finalPlayers = context.validatedPlayers.stream()
                    .filter(p -> !existingIds.contains(p._id))
                    .toList();

            for (Player player : finalPlayers) {
                long execTime = System.currentTimeMillis() - startTime;
                try {
                    context.logs.computeIfAbsent(getClass().getSimpleName(), k -> new ArrayList<>())
                            .add(new StepLog(StepsOrder.RequestStatus.SUCCESSFUL, getClass().getSimpleName(),
                                    "Player puuid " + player.puuid + " is unique and will be added.",
                                    execTime));

                } catch (Exception exception) {
                    context.logs.computeIfAbsent(getClass().getSimpleName(), k -> new ArrayList<>())
                            .add(new StepLog(StepsOrder.RequestStatus.FAILED, getClass().getSimpleName(),
                                    "Exception processing player: " + exception.getMessage(),
                                    execTime));

                    logger.error(LogFormatter.formatStepLog(getClass().getSimpleName(), StepsOrder.RequestStatus.FAILED,
                            "Exception processing player " + player.puuid, execTime), exception);
                }
            }


            if(finalPlayers.isEmpty()) {
                context.logs.computeIfAbsent(this.getClass().getSimpleName(), k -> new ArrayList<>())
                        .add(new StepLog(StepsOrder.RequestStatus.SUCCESSFUL,
                                getClass().getSimpleName(),
                                "No unique players found to add.",
                                System.currentTimeMillis() - startTime));
            }

            logger.debug("Final deduplicated player IDs: {}", finalPlayers);
            logger.info(LogFormatter.formatSummary(getClass().getSimpleName(), finalPlayers.size(),
                    0, 0, System.currentTimeMillis() - startTime));

            context.finalPlayers = new ArrayList<>(finalPlayers);
        } catch (Exception e) {
            context.logs.computeIfAbsent(getClass().getSimpleName(), k -> new ArrayList<>())
                    .add(new StepLog(StepsOrder.RequestStatus.FAILED, this.getClass().getSimpleName(),
                            "Exception during deduplication: " + e.getMessage(),
                            System.currentTimeMillis() - startTime));

            logger.error(LogFormatter.formatStepLog(getClass().getSimpleName(), StepsOrder.RequestStatus.FAILED,
                    "Exception during deduplication", System.currentTimeMillis() - startTime), e);
        }
    }
}
