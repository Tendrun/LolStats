package com.backendwebsite.DatabaseBuilder.Step.Players;

import com.backendwebsite.DatabaseBuilder.Client.RiotApiClient;
import com.backendwebsite.DatabaseBuilder.Context.BuildPlayerContext;
import com.backendwebsite.DatabaseBuilder.DTO.getPlayers.LeagueEntryDTO;
import com.backendwebsite.DatabaseBuilder.Step.IStep;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class FetchPlayersStep implements IStep<BuildPlayerContext> {
    private final RiotApiClient riotApiClient;
    private final ObjectMapper mapper;

    FetchPlayersStep(RiotApiClient riotApiClient, ObjectMapper mapper){
        this.riotApiClient = riotApiClient;
        this.mapper = mapper;
    }

    @Override
    public void execute(BuildPlayerContext context) {
        getPlayersFromRiot(context);
    }

    public void getPlayersFromRiot(BuildPlayerContext context) {
        String urnRiot = "/lol/league/v4/entries/" + context.queue + "/" + context.tier + "/" +
                context.division + "?page=" + context.page;

        RiotApiClient.Response response = riotApiClient.sendRequest(urnRiot, context.region);

        try {
            for (JsonNode row : response.body().get("rows")) {
                JsonNode doc = row.get("doc");
                LeagueEntryDTO player = mapper.treeToValue(doc, LeagueEntryDTO.class);
                context.fetchedPlayers.add(player);

                System.out.println("Get = " + player.get_id());
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        System.out.println(urnRiot);
    }


}
