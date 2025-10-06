package com.backendwebsite.DatabaseBuilder.Controller;

import com.backendwebsite.DatabaseBuilder.Director.MatchDatabaseDirector;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.backendwebsite.DatabaseBuilder.Context.BuildMatchContext;

@RestController
@RequestMapping("api/database")
public class DatabaseController {

    private final MatchDatabaseDirector matchDatabaseDirector;

    public DatabaseController(MatchDatabaseDirector matchDatabaseDirector) { this.matchDatabaseDirector = matchDatabaseDirector; }


    @GetMapping("/getChampions")
    public ResponseEntity getChampions() {

        BuildMatchContext context = new BuildMatchContext("eun1", "BRONZE", "IV",
                "RANKED_SOLO_5x5");

        matchDatabaseDirector.startWork(context);

        return ResponseEntity.ok("");
    }
}
