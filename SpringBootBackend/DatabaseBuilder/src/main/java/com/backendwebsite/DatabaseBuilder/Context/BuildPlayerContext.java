package com.backendwebsite.DatabaseBuilder.Context;

import com.backendwebsite.DatabaseBuilder.DTO.getPlayers.LeagueEntryDTO;
import com.backendwebsite.DatabaseBuilder.Step.IStep;
import com.backendwebsite.DatabaseBuilder.Step.Log.StepLog;
import com.backendwebsite.DatabaseBuilder.Step.StepsOrder;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BuildPlayerContext implements IContext {
    public final String region, tier, division, queue, page;
    public List<LeagueEntryDTO> fetchedPlayers = new ArrayList<>();
    public List<LeagueEntryDTO> validatedPlayers = new ArrayList<>();
    public List<LeagueEntryDTO> existingPlayers = new ArrayList<>();
    public List<LeagueEntryDTO> finalPlayers = new ArrayList<>();
    public List<StepLog> logs = new ArrayList<>();

    public BuildPlayerContext(String region, String tier, String division, String queue, String page) {
        this.region = region; this.tier = tier; this.division = division; this.queue = queue; this.page = page;
    }
}
