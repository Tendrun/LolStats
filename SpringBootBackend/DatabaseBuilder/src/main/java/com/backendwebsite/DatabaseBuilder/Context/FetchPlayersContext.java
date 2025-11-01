package com.backendwebsite.DatabaseBuilder.Context;

import com.backendwebsite.DatabaseBuilder.DTO.AppApi.Player.GetPlayersRequest;
import com.backendwebsite.DatabaseBuilder.Domain.Player.Player;
import com.backendwebsite.DatabaseBuilder.Step.Log.StepLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FetchPlayersContext implements IContext {


    public final GetPlayersRequest.Region region;
    public final GetPlayersRequest.Tier tier;
    public final GetPlayersRequest.Division division;
    public final GetPlayersRequest.Queue queue;
    public final String page;
    public List<Player> fetchedPlayers = new ArrayList<>();
    public List<Player> validatedPlayers = new ArrayList<>();
    public List<Player> existingPlayers = new ArrayList<>();
    public List<Player> finalPlayers = new ArrayList<>();
    public HashMap<String, List<StepLog>> logs = new HashMap<>();


    public FetchPlayersContext(GetPlayersRequest.Region region, GetPlayersRequest.Tier tier,
                               GetPlayersRequest.Division division, GetPlayersRequest.Queue queue, String page) {
        this.region = region; this.tier = tier; this.division = division; this.queue = queue; this.page = page;
    }
}
