package com.backendwebsite.DatabaseBuilder.Step.Match;

import com.backendwebsite.DatabaseBuilder.Client.RiotApiClient;
import com.backendwebsite.DatabaseBuilder.Context.BuildMatchContext;
import com.backendwebsite.DatabaseBuilder.Context.BuildPlayerContext;
import com.backendwebsite.DatabaseBuilder.DTO.RiotApi.Player.LeagueEntryDTO;
import com.backendwebsite.DatabaseBuilder.Domain.Match.Match;
import com.backendwebsite.DatabaseBuilder.Step.IStep;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class FetchMatchesStep implements IStep<BuildMatchContext> {
    private final RiotApiClient riotApiClient;
    private final ObjectMapper mapper;

    FetchMatchesStep(RiotApiClient riotApiClient, ObjectMapper mapper){
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
                    String match = mapper.treeToValue(row, String.class);
                    context.fetchedMatches.add(match);

                    System.out.println("Get = " + match);
                }
            } catch (Exception e){
                e.printStackTrace();
            }

            System.out.println(urnRiot);
        }
    }
}
