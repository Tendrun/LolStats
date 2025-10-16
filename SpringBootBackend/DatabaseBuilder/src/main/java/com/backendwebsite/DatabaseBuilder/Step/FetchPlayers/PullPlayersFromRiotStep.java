package com.backendwebsite.DatabaseBuilder.Step.FetchPlayers;

import com.backendwebsite.DatabaseBuilder.Client.RiotApiClient;
import com.backendwebsite.DatabaseBuilder.Context.FetchPlayersContext;
import com.backendwebsite.DatabaseBuilder.Domain.Player.Player;
import com.backendwebsite.DatabaseBuilder.Step.IStep;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class PullPlayersFromRiotStep implements IStep<FetchPlayersContext> {
    private final RiotApiClient riotApiClient;
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
            for (JsonNode row : response.body()) {
                Player player = mapper.treeToValue(row, Player.class);
                context.fetchedPlayers.add(player);

                System.out.println("Get = " + player.puuid);
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        System.out.println(urnRiot);
    }
}
