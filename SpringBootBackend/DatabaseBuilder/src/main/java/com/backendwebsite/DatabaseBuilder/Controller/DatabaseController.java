package com.backendwebsite.DatabaseBuilder.Controller;

import com.backendwebsite.DatabaseBuilder.DTO.getPlayers.getPlayersRequest;
import com.backendwebsite.DatabaseBuilder.Director.PlayersDirector;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.backendwebsite.DatabaseBuilder.Context.BuildPlayerContext;

@RestController
@RequestMapping("api/database")
public class DatabaseController {

    private final PlayersDirector playersDirector;

    public DatabaseController(PlayersDirector playersDirector) { this.playersDirector = playersDirector; }


    @GetMapping("/getPlayers")
    public ResponseEntity getPlayers(@RequestBody getPlayersRequest req) {

        BuildPlayerContext context = new BuildPlayerContext(
                req.region,
                req.tier,
                req.division,
                req.queue,
                req.page);

        playersDirector.startWork(context);

        return ResponseEntity.ok("");
    }
}
