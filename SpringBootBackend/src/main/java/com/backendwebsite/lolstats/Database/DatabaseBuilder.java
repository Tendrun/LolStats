package com.backendwebsite.lolstats.Database;

import com.backendwebsite.lolstats.Services.ChampionService;
import com.backendwebsite.lolstats.Services.MatchDataService;
import com.backendwebsite.lolstats.Services.PlayersService;

import java.io.*;

public class DatabaseBuilder {

    PlayersService playersService = new PlayersService();
    ChampionService championService = new ChampionService();


    public DatabaseBuilder() { }

    public void buildDatabase() {
        /*
        playersService.getPlayers("IRON"); // krok 1: pobierz/odśwież graczy z Riot API
        playersService.getPlayers("BRONZE"); // Od rangi Iron do Diamond
        playersService.getPlayers("SILVER");
        playersService.getPlayers("GOLD");
        playersService.getPlayers("PLATINUM");
        playersService.getPlayers("EMERALD");
        playersService.getPlayers("DIAMOND");


        List<LeagueEntryDTO> players = playersService.getAllPlayers(); // krok 2: pobierz wszystkich graczy z bazy

        for (LeagueEntryDTO player : players) {
            String puuid = player.getPuuid();
            if (puuid != null && !puuid.isEmpty()) {
                System.out.println("=== Fetching matches for: " + puuid + " ===");
                getPlayerMatches(puuid, "2"); // krok 3: pobierz mecze i zapisz
            } else {
                System.out.println("Skipping player with missing PUUID: " + player.get_id());
            }
        }

        /*
        for (String match : getAllMatches()){
            getMatchData(match);
        }
        */
        /*
        for (String match : getAllMatches()){
            countChampionDetails(match);
        }
        pushChampionDetails();
        */
    }
}
