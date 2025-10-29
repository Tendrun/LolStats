package com.backendwebsite.DatabaseBuilder.Step.FetchPlayers;

import com.backendwebsite.DatabaseBuilder.Client.RiotApiClient;
import com.backendwebsite.DatabaseBuilder.Context.FetchPlayersContext;
import com.backendwebsite.DatabaseBuilder.Domain.Player.Player;
import com.backendwebsite.DatabaseBuilder.Step.IStep;
import com.backendwebsite.DatabaseBuilder.Step.Log.StepLog;
import com.backendwebsite.DatabaseBuilder.Step.StepsOrder;
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
                                        "Error: Docs empty in row"));
                        logger.debug("Skipping row without data: {}", row);
                        continue;
                    }

                Player player = mapper.treeToValue(row, Player.class);
                context.fetchedPlayers.add(player);
                context.logs.computeIfAbsent(getClass().getSimpleName(), k -> new ArrayList<>())
                        .add(new StepLog(StepsOrder.RequestStatus.SUCCESSFUL, this.getClass().getSimpleName(),
                                "Fetched player ID: " + player._id));
                logger.debug("Get = {}", player._id);
                }
            } else {
                context.logs.computeIfAbsent(getClass().getSimpleName(), k -> new ArrayList<>())
                        .add(new StepLog(StepsOrder.RequestStatus.FAILED, this.getClass().getSimpleName(),
                                "Error: Riot Games response missing body" + " Exception: " + response.body()));
                logger.warn("Error: Riot Games response missing body");
            }
        } catch (Exception e){
            context.logs.computeIfAbsent(getClass().getSimpleName(), k -> new ArrayList<>())
                    .add(new StepLog(StepsOrder.RequestStatus.FAILED, this.getClass().getSimpleName(),
                            "Exception: " + e.getMessage()));
            logger.error("Exception while Pulling players from Riot Games", e);
        }
    }
}
