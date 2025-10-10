package com.backendwebsite.DatabaseBuilder.Step.Players;

import com.backendwebsite.DatabaseBuilder.Context.BuildPlayerContext;
import com.backendwebsite.DatabaseBuilder.Context.IContext;
import com.backendwebsite.DatabaseBuilder.DTO.getPlayers.LeagueEntryDTO;
import com.backendwebsite.DatabaseBuilder.Step.IStep;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ValidatePlayersStep implements IStep<BuildPlayerContext> {

    @Override
    public void execute(BuildPlayerContext context) {
        try {
            for (LeagueEntryDTO player : context.fetchedPlayers) {

                if (player.leagueId == null) {
                    // TO DO
                    // set failed request
                    continue;
                }

                if (player._id == null) {
                    player._id = player.leagueId;
                }

                context.validatedPlayers.add(player);
            }
            System.out.println("All matches uploaded to CouchDB");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
