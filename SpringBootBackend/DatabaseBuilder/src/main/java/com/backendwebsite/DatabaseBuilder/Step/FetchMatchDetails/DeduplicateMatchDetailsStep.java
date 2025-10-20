package com.backendwebsite.DatabaseBuilder.Step.FetchMatchDetails;

import com.backendwebsite.DatabaseBuilder.Context.FetchMatchDetailsContext;
import com.backendwebsite.DatabaseBuilder.Context.FetchPlayersContext;
import com.backendwebsite.DatabaseBuilder.DTO.RiotApi.MatchDetails.MatchDTO;
import com.backendwebsite.DatabaseBuilder.Domain.Player.Player;
import com.backendwebsite.DatabaseBuilder.Step.IStep;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class DeduplicateMatchDetailsStep implements IStep<FetchMatchDetailsContext> {
    @Override
    public void execute(FetchMatchDetailsContext context) {
        Set<String> existingIds = context.existingMatchDetails.stream()
                .map(p -> p.metadata.matchId)
                .collect(Collectors.toSet());

        List<MatchDTO> finalMatchDetails = context.validatedMatchDetails.stream()
                .filter(p -> !existingIds.contains(p.metadata.matchId))
                .toList();

        context.finalMatchDetails = new ArrayList<>(finalMatchDetails);
    }
}
