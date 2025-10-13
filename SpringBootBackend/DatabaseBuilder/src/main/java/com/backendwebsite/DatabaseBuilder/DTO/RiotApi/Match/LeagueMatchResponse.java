package com.backendwebsite.DatabaseBuilder.DTO.RiotApi.Match;

import java.util.ArrayList;
import java.util.List;

public class LeagueMatchResponse {
    final List<String> matches;

    public LeagueMatchResponse(List<String> matches){
        this.matches = matches;
    }
}
