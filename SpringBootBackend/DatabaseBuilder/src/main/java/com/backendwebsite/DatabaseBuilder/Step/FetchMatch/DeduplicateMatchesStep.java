package com.backendwebsite.DatabaseBuilder.Step.FetchMatch;

import com.backendwebsite.DatabaseBuilder.Context.FetchMatchesContext;
import com.backendwebsite.DatabaseBuilder.Domain.Match.PlayerMatches;
import com.backendwebsite.DatabaseBuilder.Step.IStep;
import com.backendwebsite.DatabaseBuilder.Step.Log.StepLog;
import com.backendwebsite.DatabaseBuilder.Step.StepsOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class DeduplicateMatchesStep implements IStep<FetchMatchesContext> {
    private static final Logger logger = LoggerFactory.getLogger(DeduplicateMatchesStep.class);

    @Override
    public void execute(FetchMatchesContext context) {
        long startTime = System.currentTimeMillis();
        try {
            deduplicateMatches(context);

            context.logs.computeIfAbsent(getClass().getSimpleName(), k -> new ArrayList<>())
                    .add(new StepLog(StepsOrder.RequestStatus.SUCCESSFUL, this.getClass().getSimpleName(),
                            "Deduplication completed. Final player matches: " + context.finalPlayerMatches.size(), System.currentTimeMillis() - startTime, ""));
            logger.info("Deduplication completed. Final player matches: {}", context.finalPlayerMatches.size());
        } catch (Exception e) {
            context.logs.computeIfAbsent(getClass().getSimpleName(), k -> new ArrayList<>())
                    .add(new StepLog(StepsOrder.RequestStatus.FAILED, this.getClass().getSimpleName(),
                            "Exception during deduplication: " + e.getMessage(), System.currentTimeMillis() - startTime, ""));
            logger.error("Exception during deduplication", e);
        }
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
            String _id = "playerMatches:" + context.region + ":" + puuid;
            PlayerMatches playerMatches = new PlayerMatches(mergedMatchIds.stream().toList(), puuid, _id, rev);
            context.finalPlayerMatches.add(playerMatches);

            logger.debug("Merged {} matchIds for puuid {} (existing size: {}, fetched size: {})",
                    mergedMatchIds.size(), puuid, existingList.size(), fetchedList.size());
        }
    }
}
