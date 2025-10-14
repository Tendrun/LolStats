package com.backendwebsite.DatabaseBuilder.Step.FetchPlayers;

import com.backendwebsite.DatabaseBuilder.Context.BuildPlayerContext;
import com.backendwebsite.DatabaseBuilder.Domain.Player.Player;
import com.backendwebsite.DatabaseBuilder.Step.IStep;
import org.springframework.stereotype.Component;

@Component
public class ValidatePlayersStep implements IStep<BuildPlayerContext> {

    @Override
    public void execute(BuildPlayerContext context) {
        try {
            for (Player player : context.fetchedPlayers) {

                if (player.puuid == null) {
                    // TO DO
                    // set failed request
                    continue;
                }

                if (player._id == null) {
                    player._id = "player:" + context.region + ":" + player.puuid;
                }

                player.region = context.region.name();
                context.validatedPlayers.add(player);
            }
            System.out.println("Request is send to CouchDB");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
