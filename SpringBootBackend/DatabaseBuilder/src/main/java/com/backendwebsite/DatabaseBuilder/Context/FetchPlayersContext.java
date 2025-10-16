package com.backendwebsite.DatabaseBuilder.Context;

import com.backendwebsite.DatabaseBuilder.Domain.Player.Player;
import com.backendwebsite.DatabaseBuilder.Step.Log.StepLog;

import java.util.ArrayList;
import java.util.List;

public class FetchPlayersContext implements IContext {

    public enum Region {
        eun1,
    }

    public final Region region;
    public final String tier, division, queue, page;
    public List<Player> fetchedPlayers = new ArrayList<>();
    public List<Player> validatedPlayers = new ArrayList<>();
    public List<Player> existingPlayers = new ArrayList<>();
    public List<Player> finalPlayers = new ArrayList<>();
    public List<StepLog> logs = new ArrayList<>();

    public FetchPlayersContext(Region region, String tier, String division, String queue, String page) {
        this.region = region; this.tier = tier; this.division = division; this.queue = queue; this.page = page;
    }
}
