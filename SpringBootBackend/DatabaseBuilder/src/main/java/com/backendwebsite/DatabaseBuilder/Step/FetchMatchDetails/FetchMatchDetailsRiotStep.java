package com.backendwebsite.DatabaseBuilder.Step.FetchMatchDetails;

import com.backendwebsite.DatabaseBuilder.Client.RiotApiClient;
import com.backendwebsite.DatabaseBuilder.Context.FetchMatchDetailsContext;
import com.backendwebsite.DatabaseBuilder.DTO.RiotApi.MatchDetails.MatchDTO;
import com.backendwebsite.DatabaseBuilder.Step.IStep;
import com.backendwebsite.DatabaseBuilder.Step.Log.StepLog;
import com.backendwebsite.DatabaseBuilder.Step.StepsOrder;
import com.backendwebsite.DatabaseBuilder.Util.LogFormatter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Component
public class FetchMatchDetailsRiotStep implements IStep<FetchMatchDetailsContext> {
    private final RiotApiClient riotApiClient;
    private final ObjectMapper mapper;
    private static final Logger logger = LoggerFactory.getLogger(FetchMatchDetailsRiotStep.class);

    FetchMatchDetailsRiotStep(RiotApiClient riotApiClient, ObjectMapper mapper){
        this.riotApiClient = riotApiClient;
        this.mapper = mapper;
    }

    @Override
    public void execute(FetchMatchDetailsContext context) {
        getMatchesFromRiot(context);
    }

    public void getMatchesFromRiot(FetchMatchDetailsContext context) {
        int fetchedCount = 0;
        int failedCount = 0;
        long overallStartTime = System.currentTimeMillis();

        for (String matchId : context.matchIds) {
            long stepStartTime = System.currentTimeMillis();
            String urnRiot = "/lol/match/v5/matches/" + matchId;
            RiotApiClient.Response response = riotApiClient.sendRequest(urnRiot, context.region.name());

            try {
                if (response != null && response.body() != null) {
                    MatchDTO matchDetails = mapper.treeToValue(response.body(), MatchDTO.class);
                    context.fetchedMatchDetails.add(matchDetails);

                    context.logs.computeIfAbsent(getClass().getSimpleName(), k -> new java.util.ArrayList<>())
                            .add(new StepLog(StepsOrder.RequestStatus.SUCCESSFUL,
                                    this.getClass().getSimpleName(),
                                    "Fetched match details for id: " + matchId,
                                    System.currentTimeMillis() - stepStartTime));
                    fetchedCount++;
                } else {
                    context.logs.computeIfAbsent(getClass().getSimpleName(), k -> new java.util.ArrayList<>())
                            .add(new StepLog(StepsOrder.RequestStatus.FAILED,
                                    this.getClass().getSimpleName(),
                                    "Empty response body for match id: " + matchId + " (urn: " + urnRiot + ")",
                                    System.currentTimeMillis() - stepStartTime));
                    failedCount++;
                }
            } catch (Exception e) {
                context.logs.computeIfAbsent(getClass().getSimpleName(), k -> new java.util.ArrayList<>())
                        .add(new StepLog(StepsOrder.RequestStatus.FAILED,
                                this.getClass().getSimpleName(),
                                "Exception parsing match details for id: " + matchId + " - " + e.getMessage(),
                                System.currentTimeMillis() - stepStartTime));
                logger.error("Exception while parsing match details for id {} (urn={})", matchId, urnRiot, e);
                failedCount++;
            }
        }

        String summary = String.format("Fetched %d match details (failed: %d)", fetchedCount, failedCount);
        logger.info(LogFormatter.formatStepLog(getClass().getSimpleName(), StepsOrder.RequestStatus.SUCCESSFUL,
                summary, System.currentTimeMillis() - overallStartTime));
    }
}
