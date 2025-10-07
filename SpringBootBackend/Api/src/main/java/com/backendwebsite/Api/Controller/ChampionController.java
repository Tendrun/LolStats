package com.backendwebsite.Api.Controller;

import com.backendwebsite.Api.DTO.ChampionDetails.ChampionDetailsResponse;
import com.backendwebsite.Api.Service.ChampionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/data")
public class ChampionController {

    private final ChampionService championService;

    @Autowired
    public ChampionController(ChampionService championService) {
        this.championService = championService;
    }

    @GetMapping("/getChampions")
    public ResponseEntity<List<ChampionDetailsResponse>> getChampions() {
        List<ChampionDetailsResponse> listOfChampions = championService.getAllChampDetailsFromCouchDB();
        return ResponseEntity.ok(listOfChampions);
    }

    @GetMapping("/getChampion")
    public ResponseEntity<Map<String, Object>> getChampion() {
        Map<String, Object> fullRunes = new LinkedHashMap<>();
        Map<String, Object> response = Map.of("fullRunes", fullRunes);

        fullRunes.put("generalRunes", List.of(
                Map.of("displayName", "Summon Aery", "id", 8214),
                Map.of("displayName", "Manaflow Band", "id", 8226),
                Map.of("displayName", "Transcendence", "id", 8210),
                Map.of("displayName", "Scorch", "id", 8237),
                Map.of("displayName", "Bone Plating", "id", 8473),
                Map.of("displayName", "Revitalize", "id", 8453)
        ));

        fullRunes.put("primaryRuneTree", Map.of(
                "displayName", "Sorcery",
                "id", 8200
        ));

        fullRunes.put("secondaryRuneTree", Map.of(
                "displayName", "Resolve",
                "id", 8400
        ));

        fullRunes.put("statRunes", List.of(
                Map.of("id", 5007, "rawDescription", "perk_tooltip_StatModCooldownReductionScaling"),
                Map.of("id", 5008, "rawDescription", "perk_tooltip_StatModAdaptive"),
                Map.of("id", 5001, "rawDescription", "perk_tooltip_StatModHealthScaling")
        ));


        return ResponseEntity.ok(response);
    }
}
