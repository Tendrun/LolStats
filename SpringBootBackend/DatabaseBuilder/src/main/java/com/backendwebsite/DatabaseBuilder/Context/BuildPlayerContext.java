package com.backendwebsite.DatabaseBuilder.Context;

import com.backendwebsite.DatabaseBuilder.DTO.getPlayers.LeagueEntryDTO;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;

public class BuildPlayerContext implements IContext {
    public final String region, tier, division, queue, page;
    public List<LeagueEntryDTO> fetchedPlayers, existingPlayers;

    public BuildPlayerContext(String region, String tier, String division, String queue, String page) {
        this.region = region; this.tier = tier; this.division = division; this.queue = queue; this.page = page;
    }
}
