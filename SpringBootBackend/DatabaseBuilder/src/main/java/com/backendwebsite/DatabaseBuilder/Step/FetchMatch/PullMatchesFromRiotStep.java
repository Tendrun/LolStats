package com.backendwebsite.DatabaseBuilder.Step.FetchMatch;

import com.backendwebsite.DatabaseBuilder.Client.RiotApiClient;
import com.backendwebsite.DatabaseBuilder.Context.BuildMatchContext;
import com.backendwebsite.DatabaseBuilder.Domain.Match.PlayerMatches;
import com.backendwebsite.DatabaseBuilder.Step.IStep;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;


@Component
public class PullMatchesFromRiotStep implements IStep<BuildMatchContext> {
    private final RiotApiClient riotApiClient;
    private final ObjectMapper mapper;

    PullMatchesFromRiotStep(RiotApiClient riotApiClient, ObjectMapper mapper){
        this.riotApiClient = riotApiClient;
        this.mapper = mapper;
    }

    @Override
    public void execute(BuildMatchContext context) {
        getMatchesFromRiot(context);
    }

    public void getMatchesFromRiot(BuildMatchContext context) {
        for (String puuid : context.puuids) {
            String urnRiot = "/lol/match/v5/matches/by-puuid/" + puuid + "/ids";
            RiotApiClient.Response response = riotApiClient.sendRequest(urnRiot, context.region.name());

            try {
                for (JsonNode row : response.body()) {
                    PlayerMatches matchId  = mapper.treeToValue(row, PlayerMatches.class);
                    PlayerMatches playerMatches = new PlayerMatches(matchId, puuid, puuid);
                    context.fetchedMatches.computeIfAbsent(puuid, k -> new ArrayList<>()).add(playerMatches);

                    System.out.println("Get = " + playerMatches);
                }
            } catch (Exception e){
                e.printStackTrace();
            }

            System.out.println(urnRiot);
        }
    }
}
