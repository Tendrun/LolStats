package com.backendwebsite.DatabaseBuilder.Controller;

import com.backendwebsite.DatabaseBuilder.Director.DatabaseDirector;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.backendwebsite.DatabaseBuilder.Context.BuildMatchContext;

@RestController
@RequestMapping("api/database")
public class DatabaseController {

    private final DatabaseDirector databaseDirector;

    public DatabaseController(DatabaseDirector databaseDirector) { this.databaseDirector = databaseDirector; }


    @GetMapping("/getChampions")
    public ResponseEntity getChampions() {

        BuildMatchContext context = new BuildMatchContext("eun1", "I", "BRONZE",
                "RANKED_SOLO_5x5");

        databaseDirector.startWork(context);

        return ResponseEntity.ok("");
    }
}
