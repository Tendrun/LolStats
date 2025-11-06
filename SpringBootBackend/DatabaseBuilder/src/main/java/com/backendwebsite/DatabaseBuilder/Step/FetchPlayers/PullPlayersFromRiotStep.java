package com.backendwebsite.DatabaseBuilder.Step.FetchPlayers;

import com.backendwebsite.DatabaseBuilder.Client.RiotApiClient;
import com.backendwebsite.DatabaseBuilder.Context.FetchPlayersContext;
import com.backendwebsite.DatabaseBuilder.Domain.Player.Player;
import com.backendwebsite.DatabaseBuilder.Step.IStep;
import com.backendwebsite.DatabaseBuilder.Step.Log.StepLog;
import com.backendwebsite.DatabaseBuilder.Step.StepsOrder;
import com.backendwebsite.DatabaseBuilder.Util.LogFormatter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class PullPlayersFromRiotStep implements IStep<FetchPlayersContext> {
    private final RiotApiClient riotApiClient;
    private static final Logger logger = LoggerFactory.getLogger(PullPlayersFromRiotStep.class);
    private final ObjectMapper mapper;

    PullPlayersFromRiotStep(RiotApiClient riotApiClient, ObjectMapper mapper){
        this.riotApiClient = riotApiClient;
        this.mapper = mapper;
    }

    @Override
    public void execute(FetchPlayersContext context) {
        getPlayersFromRiot(context);
    }

    public void getPlayersFromRiot(FetchPlayersContext context) {
        long startTime = System.currentTimeMillis();
        String urnRiot = "/lol/league/v4/entries/" + context.queue + "/" + context.tier + "/" +
                context.division + "?page=" + context.page;

        RiotApiClient.Response response = riotApiClient.sendRequest(urnRiot, context.region.name());

        try {
            JsonNode rows = response.body();
            if (rows != null && rows.isArray()) {
                for (JsonNode row : rows) {
                    if (row == null || row.isNull()) {
                        context.logs.computeIfAbsent(getClass().getSimpleName(), k -> new ArrayList<>())
                                .add(new StepLog(StepsOrder.RequestStatus.FAILED, this.getClass().getSimpleName(),
                                        "Error: Docs empty in row", System.currentTimeMillis() - startTime, ""));
                        logger.debug("Skipping row without data: {}", row);
                        continue;
                    }

                Player player = mapper.treeToValue(row, Player.class);
                context.fetchedPlayers.add(player);
                context.logs.computeIfAbsent(getClass().getSimpleName(), k -> new ArrayList<>())
                                "Fetched player puuid: " + player.puuid,
                                "Fetched player puuid: " + player.puuid,
                logger.info(LogFormatter.formatStepLogWithPuuid(getClass().getSimpleName(), StepsOrder.RequestStatus.SUCCESSFUL, player.puuid, System.currentTimeMillis() - startTime));
                logger.debug("Get = {}", player.puuid);
                }
            } else {
                context.logs.computeIfAbsent(getClass().getSimpleName(), k -> new ArrayList<>())
                        .add(new StepLog(StepsOrder.RequestStatus.FAILED, this.getClass().getSimpleName(),
                logger.warn(LogFormatter.formatStepLog(getClass().getSimpleName(), StepsOrder.RequestStatus.FAILED, "Riot Games response missing body", System.currentTimeMillis() - startTime));
                logger.warn("Error: Riot Games response missing body");
            }
        } catch (Exception e){
            context.logs.computeIfAbsent(getClass().getSimpleName(), k -> new ArrayList<>())
                    .add(new StepLog(StepsOrder.RequestStatus.FAILED, this.getClass().getSimpleName(),
            logger.error(LogFormatter.formatStepLog(getClass().getSimpleName(), StepsOrder.RequestStatus.FAILED, "Exception: " + e.getMessage(), System.currentTimeMillis() - startTime), e);
            logger.error("Exception while Pulling players from Riot Games", e);
        }
    }
}
