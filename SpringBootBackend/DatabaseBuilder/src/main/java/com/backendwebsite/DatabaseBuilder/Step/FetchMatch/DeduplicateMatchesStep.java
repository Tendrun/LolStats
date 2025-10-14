package com.backendwebsite.DatabaseBuilder.Step.FetchMatch;

import com.backendwebsite.DatabaseBuilder.Context.BuildMatchContext;
import com.backendwebsite.DatabaseBuilder.Domain.Match.PlayerMatches;
import com.backendwebsite.DatabaseBuilder.Step.IStep;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class DeduplicateMatchesStep implements IStep<BuildMatchContext> {
    @Override
    public void execute(BuildMatchContext context) {
        deduplicateMatches(context);
    }

    public void deduplicateMatches(BuildMatchContext context) {
        Set<PlayerMatches> mergedSet = new LinkedHashSet<>();

        for (Map.Entry<String, List<PlayerMatches>> entry : context.fetchedMatches.entrySet()) {
            String key = entry.getKey();
            List<PlayerMatches> fetchedList = entry.getValue();
            List<PlayerMatches> existingList = context.existingMatches.getOrDefault(key, List.of());

            mergedSet.addAll(existingList);
            mergedSet.addAll(fetchedList);
        }

    }
}
