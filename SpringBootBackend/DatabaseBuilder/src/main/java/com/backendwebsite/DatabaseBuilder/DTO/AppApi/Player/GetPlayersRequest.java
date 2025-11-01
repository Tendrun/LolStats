package com.backendwebsite.DatabaseBuilder.DTO.AppApi.Player;

public record GetPlayersRequest(Region region, Tier tier, Division division, Queue queue, String page) {
    public enum Division {
        I,
        II,
        III,
        IV
    }
    public enum Tier {
        IRON,
        BRONZE,
        SILVER,
        GOLD,
        PLATINUM,
        EMERALD,
        DIAMOND,
        MASTER,
        GRANDMASTER,
        CHALLENGER
    }
    public enum Queue {
        RANKED_SOLO_5x5,
        RANKED_FLEX_SR,
        RANKED_FLEX_TT
    }
    public enum Region {
        BR1,
        EUN1,
        EUW1,
        JP1,
        KR,
        LA1,
        LA2,
        ME1,
        NA1,
        OC1,
        RU,
        SG2,
        TR1,
        TW2,
        VN2
    }
}

