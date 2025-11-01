package com.backendwebsite.DatabaseBuilder.DTO.AppApi.Match;

import com.backendwebsite.DatabaseBuilder.Context.FetchMatchesContext;

public record GetMatchesRequest(Region region, int playerLimit, Type type,
                                Tier tier) {
    public enum Region {
        americas,
        asia,
        europe,
        sea
    }
    public enum Type {
        ranked,
        normal,
        tourney,
        tutorial
    }
    public enum Tier {
        IRON,
        BRONZE,
        SILVER,
        GOLD,
        PLATINUM,
        EMERALD,
        DIAMOND
    }
}
