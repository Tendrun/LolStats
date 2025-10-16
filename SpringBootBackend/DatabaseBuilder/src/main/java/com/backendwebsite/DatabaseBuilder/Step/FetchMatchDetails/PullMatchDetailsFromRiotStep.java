package com.backendwebsite.DatabaseBuilder.Step.FetchMatchDetails;

import com.backendwebsite.DatabaseBuilder.Client.RiotApiClient;
import com.backendwebsite.DatabaseBuilder.Context.FetchMatchDetailsContext;
import com.backendwebsite.DatabaseBuilder.DTO.RiotApi.MatchDetails.MatchDTO;
import com.backendwebsite.DatabaseBuilder.Domain.Match.PlayerMatches;
import com.backendwebsite.DatabaseBuilder.Step.IStep;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class PullMatchDetailsFromRiotStep implements IStep<FetchMatchDetailsContext> {
    private final RiotApiClient riotApiClient;
    private final ObjectMapper mapper;

    PullMatchDetailsFromRiotStep(RiotApiClient riotApiClient, ObjectMapper mapper){
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
                MatchDTO matchDetails = mapper.convertValue(response.body(), new TypeReference<>() {});
                context.fetchedMatches.add(matchDetails);
                System.out.println("Get = " + matchDetails);
            } catch (Exception e){
                e.printStackTrace();
            }
            System.out.println(urnRiot);
        }
    }
}
