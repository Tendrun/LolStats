package com.backendwebsite.DatabaseBuilder.Step.FetchMatch;

import com.backendwebsite.DatabaseBuilder.Context.FetchMatchesContext;
import com.backendwebsite.DatabaseBuilder.Domain.Match.PlayerMatches;
import com.backendwebsite.DatabaseBuilder.Step.IStep;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class DeduplicateMatchesStep implements IStep<FetchMatchesContext> {
    private final ObjectMapper mapper;

    public DeduplicateMatchesStep(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void execute(FetchMatchesContext context) {
        deduplicateMatches(context);
    }

    public void deduplicateMatches(FetchMatchesContext context) {
        for (Map.Entry<String, PlayerMatches> entry : context.fetchedMatches.entrySet()) {
            Set<String> mergedMatchIds = new LinkedHashSet<>();

            // key here is also player's puuid
            String key = entry.getKey();

            List<String> fetchedList = entry.getValue().matchIds();
            PlayerMatches exisitngPlayerMatches = context.existingMatches.getOrDefault(key,
                    new PlayerMatches(Collections.emptyList(), null, null, ""));
            List<String> existingList = exisitngPlayerMatches.matchIds();
            String rev = exisitngPlayerMatches._rev();

            mergedMatchIds.addAll(existingList);
            mergedMatchIds.addAll(fetchedList);

            String puuid = entry.getValue().puuid();
            PlayerMatches playerMatches = new PlayerMatches(mergedMatchIds.stream().toList(), puuid, puuid, rev);
            context.finalPlayerMatches.add(playerMatches);
        }
    }
}
