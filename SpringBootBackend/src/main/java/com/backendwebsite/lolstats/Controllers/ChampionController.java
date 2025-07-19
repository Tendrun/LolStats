package com.backendwebsite.lolstats.Controllers;

import com.backendwebsite.lolstats.Constants.ChampionStatsMap.ChampionDetails;
import com.backendwebsite.lolstats.Services.ChampionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/data")
public class ChampionController {

    private final ChampionService championService;

    @Autowired
    public ChampionController(ChampionService championService) {
        this.championService = championService;
    }

    @GetMapping("/getChampionsFromCouchDB")
    public ResponseEntity<List<ChampionDetails>> getChampionsFromCouchDB() {
        System.out.println("I m here");
        List<ChampionDetails> listOfChampions = championService.getAllChampDetailsFromCouchDB();
        return ResponseEntity.ok(listOfChampions);
    }

    @GetMapping("/updateChampionsFromRiot")
    public ResponseEntity<List<ChampionDetails>> updateChampionsFromRiot() {
        System.out.println("I m here");
        List<ChampionDetails> listOfChampions = championService.getAllChampDetailsFromCouchDB();
        return ResponseEntity.ok(listOfChampions);
    }
}
