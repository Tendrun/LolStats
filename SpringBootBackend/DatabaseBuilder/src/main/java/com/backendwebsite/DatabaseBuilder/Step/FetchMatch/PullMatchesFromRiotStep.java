package com.backendwebsite.DatabaseBuilder.Step.FetchMatch;

import com.backendwebsite.DatabaseBuilder.Client.RiotApiClient;
import com.backendwebsite.DatabaseBuilder.Context.FetchMatchesContext;
import com.backendwebsite.DatabaseBuilder.Domain.Match.PlayerMatches;
import com.backendwebsite.DatabaseBuilder.Step.IStep;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class PullMatchesFromRiotStep implements IStep<FetchMatchesContext> {
    private final RiotApiClient riotApiClient;
    private final ObjectMapper mapper;

    PullMatchesFromRiotStep(RiotApiClient riotApiClient, ObjectMapper mapper){
        this.riotApiClient = riotApiClient;
        this.mapper = mapper;
    }

    @Override
    public void execute(FetchMatchesContext context) {
        getMatchesFromRiot(context);
    }

    public void getMatchesFromRiot(FetchMatchesContext context) {
        for (String puuid : context.puuids) {
            String urnRiot = "/lol/match/v5/matches/by-puuid/" + puuid + "/ids" + "?type=" + context.type  +
                    "&start=0&count=20";
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
