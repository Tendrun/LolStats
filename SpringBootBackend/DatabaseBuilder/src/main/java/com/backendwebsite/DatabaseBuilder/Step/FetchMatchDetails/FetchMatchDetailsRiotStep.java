package com.backendwebsite.DatabaseBuilder.Step.FetchMatchDetails;

import com.backendwebsite.DatabaseBuilder.Client.RiotApiClient;
import com.backendwebsite.DatabaseBuilder.Context.FetchMatchDetailsContext;
import com.backendwebsite.DatabaseBuilder.DTO.RiotApi.MatchDetails.MatchDTO;
import com.backendwebsite.DatabaseBuilder.Step.IStep;
import com.backendwebsite.DatabaseBuilder.Step.Log.StepLog;
import com.backendwebsite.DatabaseBuilder.Step.StepsOrder;
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
        for (String matchId : context.matchIds) {
            String urnRiot = "/lol/match/v5/matches/" + matchId;
            RiotApiClient.Response response = riotApiClient.sendRequest(urnRiot, context.region.name());

            try {
                if (response != null && response.body() != null) {
                    MatchDTO matchDetails = mapper.treeToValue(response.body(), MatchDTO.class);
                    context.fetchedMatchDetails.add(matchDetails);

                    context.logs.computeIfAbsent(getClass().getSimpleName(), k -> new java.util.ArrayList<>())
                            .add(new StepLog(StepsOrder.RequestStatus.SUCCESSFUL,
                                    this.getClass().getSimpleName(),
                                    "Fetched match details for id: " + matchId));

                    logger.debug("Fetched match details for id {}", matchId);
                } else {
                    context.logs.computeIfAbsent(getClass().getSimpleName(), k -> new java.util.ArrayList<>())
                            .add(new StepLog(StepsOrder.RequestStatus.FAILED,
                                    this.getClass().getSimpleName(),
                                    "Empty response body for match id: " + matchId + " (urn: " + urnRiot + ")"));
                    logger.warn("Empty response body for match id {} (urn={})", matchId, urnRiot);
                }
            } catch (Exception e){
                context.logs.computeIfAbsent(getClass().getSimpleName(), k -> new java.util.ArrayList<>())
                        .add(new StepLog(StepsOrder.RequestStatus.FAILED,
                                this.getClass().getSimpleName(),
                                "Exception parsing match details for id: " + matchId + " - " + e.getMessage()));
                logger.error("Exception while parsing match details for id {} (urn={})", matchId, urnRiot, e);
            }

            logger.debug("Request sent to Riot: {}", urnRiot);
        }
    }
}
