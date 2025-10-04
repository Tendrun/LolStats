package com.backendwebsite.DatabaseBuilder.Controller;

import com.backendwebsite.DatabaseBuilder.Director.DatabaseDirector;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/database")
public class DatabaseController {

    @GetMapping("/getChampions")
    public ResponseEntity getChampions() {

        DatabaseDirector databaseDirector = new DatabaseDirector("eun1");
        databaseDirector.startWork();

        return ResponseEntity.ok("");
    }
}
