package com.backendwebsite.DatabaseBuilder.Step.FetchMatch;

import com.backendwebsite.DatabaseBuilder.Context.FetchMatchesContext;
import com.backendwebsite.DatabaseBuilder.Domain.Match.PlayerMatches;
import com.backendwebsite.DatabaseBuilder.Step.IStep;
import com.backendwebsite.DatabaseBuilder.Step.Log.StepLog;
import com.backendwebsite.DatabaseBuilder.Step.StepsOrder;
import com.backendwebsite.DatabaseBuilder.Util.LogFormatter;
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

            String msg = "Deduplication completed. Final player matches: " + context.finalPlayerMatches.size();
            context.logs.computeIfAbsent(getClass().getSimpleName(), k -> new ArrayList<>())
                    .add(new StepLog(StepsOrder.RequestStatus.SUCCESSFUL, this.getClass().getSimpleName(),
                            msg,
                            System.currentTimeMillis() - startTime));
            logger.info(LogFormatter.formatStepLog(getClass().getSimpleName(), StepsOrder.RequestStatus.SUCCESSFUL, msg,
                    System.currentTimeMillis() - startTime));
        } catch (Exception e) {
            String msg = "Exception during deduplication: " + e.getMessage();
            context.logs.computeIfAbsent(getClass().getSimpleName(), k -> new ArrayList<>())
                    .add(new StepLog(StepsOrder.RequestStatus.FAILED, this.getClass().getSimpleName(),
                            msg,
                            System.currentTimeMillis() - startTime));
            logger.error(LogFormatter.formatStepLog(getClass().getSimpleName(), StepsOrder.RequestStatus.FAILED, msg,
                    System.currentTimeMillis() - startTime), e);
        }
    }

    public void deduplicateMatches(FetchMatchesContext context) {
        for (Map.Entry<String, PlayerMatches> entry : context.fetchedMatches.entrySet()) {
            Set<String> mergedMatchIds = new LinkedHashSet<>();

            // TO DO
            // you can check if matches are up-to-date then skip player
            // Also there should be a mechanics that checks if old matches are missing
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
