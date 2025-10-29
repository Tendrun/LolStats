package com.backendwebsite.DatabaseBuilder.Step.FetchPlayers;

import com.backendwebsite.DatabaseBuilder.Context.FetchPlayersContext;
import com.backendwebsite.DatabaseBuilder.Domain.Player.Player;
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
public class DeduplicatePlayersStep implements IStep<FetchPlayersContext> {
    private static final Logger logger = LoggerFactory.getLogger(DeduplicatePlayersStep.class);

    @Override
    public void execute(FetchPlayersContext context) {
        Set<String> existingIds = context.existingPlayers.stream()
                .map(p -> p._id)
                .collect(Collectors.toSet());

        List<Player> finalPlayers = context.validatedPlayers.stream()
                .filter(p -> !existingIds.contains(p._id))
                .toList();

        finalPlayers.forEach(player ->
                context.logs.computeIfAbsent(this.getClass().getSimpleName(), k -> new ArrayList<>())
                        .add(new StepLog(StepsOrder.RequestStatus.SUCCESSFUL,
                                this.getClass().getSimpleName(),
                                "Player ID " + player._id + " is unique and will be added."))
        );

        if(finalPlayers.isEmpty()) {
            context.logs.computeIfAbsent(this.getClass().getSimpleName(), k -> new ArrayList<>())
                    .add(new StepLog(StepsOrder.RequestStatus.SUCCESSFUL,
                            this.getClass().getSimpleName(),
                            "No unique players found to add."));
        }

        logger.debug("Final deduplicated player IDs: {}", finalPlayers);

        context.finalPlayers = new ArrayList<>(finalPlayers);
    }
}
