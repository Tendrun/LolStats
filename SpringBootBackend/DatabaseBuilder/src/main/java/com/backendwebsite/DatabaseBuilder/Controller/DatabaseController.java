package com.backendwebsite.DatabaseBuilder.Controller;

import com.backendwebsite.DatabaseBuilder.Context.BuildMatchContext;
import com.backendwebsite.DatabaseBuilder.DTO.AppApi.Match.GetMatchesRequest;
import com.backendwebsite.DatabaseBuilder.DTO.AppApi.Player.GetPlayersRequest;
import com.backendwebsite.DatabaseBuilder.Director.MatchDirector;
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
    private final MatchDirector matchDirector;

    public DatabaseController(PlayersDirector playersDirector, MatchDirector matchDirector) {
        this.playersDirector = playersDirector;
        this.matchDirector = matchDirector;
    }


    @GetMapping("/getPlayers")
    public ResponseEntity getPlayers(@RequestBody GetPlayersRequest req) {

        BuildPlayerContext context = new BuildPlayerContext(
                (BuildPlayerContext.Region.valueOf(req.region())),
                req.tier(),
                req.division(),
                req.queue(),
                req.page());

        playersDirector.startWork(context);

        // TO DO
        // failed and success logs

        return ResponseEntity.ok("");
    }

    @GetMapping("/getMatches")
    public ResponseEntity getPlayers(@RequestBody GetMatchesRequest req) {

        BuildMatchContext context = new BuildMatchContext(BuildMatchContext.Region.valueOf(req.region()));

        matchDirector.startWork(context);

        return ResponseEntity.ok("");
    }
}
