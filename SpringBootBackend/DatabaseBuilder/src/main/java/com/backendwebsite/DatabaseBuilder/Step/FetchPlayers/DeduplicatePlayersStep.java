package com.backendwebsite.DatabaseBuilder.Step.FetchPlayers;

import com.backendwebsite.DatabaseBuilder.Context.BuildPlayerContext;
import com.backendwebsite.DatabaseBuilder.Domain.Player.Player;
import com.backendwebsite.DatabaseBuilder.Step.IStep;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class DeduplicatePlayersStep implements IStep<BuildPlayerContext> {
    @Override
    public void execute(BuildPlayerContext context) {
        Set<String> existingIds = context.existingPlayers.stream()
                .map(p -> p._id)
                .collect(Collectors.toSet());

        List<Player> finalPlayers = context.validatedPlayers.stream()
                .filter(p -> !existingIds.contains(p._id))
                .toList();

        context.finalPlayers = new ArrayList<>(finalPlayers);
    }
}
