package com.backendwebsite.DatabaseBuilder.Step.FetchMatch;

import com.backendwebsite.DatabaseBuilder.Client.RiotApiClient;
import com.backendwebsite.DatabaseBuilder.Context.BuildMatchContext;
import com.backendwebsite.DatabaseBuilder.Domain.Match.PlayerMatches;
import com.backendwebsite.DatabaseBuilder.Step.IStep;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


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
                List<String> matchIds = mapper.convertValue(response.body(), new TypeReference<>() {});
                PlayerMatches playerMatches = new PlayerMatches(matchIds, puuid, puuid, "");
                context.fetchedMatches.put(puuid, playerMatches);
                System.out.println("Get = " + playerMatches);
            } catch (Exception e){
                e.printStackTrace();
            }
            System.out.println(urnRiot);
        }
    }
}
