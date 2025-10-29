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

@Component
public class ValidatePlayersStep implements IStep<FetchPlayersContext> {
    private static final Logger logger = LoggerFactory.getLogger(ValidatePlayersStep.class);


    @Override
    public void execute(FetchPlayersContext context) {
        try {
            for (Player player : context.fetchedPlayers) {

                if (player.puuid == null) {
                    context.logs.computeIfAbsent(getClass().getSimpleName(), k -> new ArrayList<>())
                            .add(new StepLog(StepsOrder.RequestStatus.FAILED, this.getClass().getSimpleName(),
                                    "Error: player puuid is null"));
                    logger.error("Skipping player with null puuid: {}", player);
                    continue;
                }

                if (player._id == null) {
                    player._id = "player:" + context.region + ":" + player.puuid;
                }

                player.region = context.region.name();
                context.validatedPlayers.add(player);

                context.logs.computeIfAbsent(getClass().getSimpleName(), k -> new ArrayList<>())
                        .add(new StepLog(StepsOrder.RequestStatus.SUCCESSFUL, this.getClass().getSimpleName(),
                                "Validated player ID: " + player._id));
                logger.debug("Validated player ID: {}", player._id);
            }
        } catch (Exception e) {
            context.logs.computeIfAbsent(getClass().getSimpleName(), k -> new ArrayList<>())
                    .add(new StepLog(StepsOrder.RequestStatus.FAILED, this.getClass().getSimpleName(),
                            "Exception during player validation: " + e.getMessage()));
        }
    }
}
