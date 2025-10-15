package com.backendwebsite.DatabaseBuilder.Controller;

import com.backendwebsite.DatabaseBuilder.Builder.ChampionAnalyticsBuilder;
import com.backendwebsite.DatabaseBuilder.Context.BuildChampionAnalyticsContext;
import com.backendwebsite.DatabaseBuilder.Context.BuildMatchContext;
import com.backendwebsite.DatabaseBuilder.DTO.AppApi.AnaliseMatches.GetAnaliseMatchesRequest;
import com.backendwebsite.DatabaseBuilder.DTO.AppApi.Match.GetMatchesRequest;
import com.backendwebsite.DatabaseBuilder.DTO.AppApi.Player.GetPlayersRequest;
import com.backendwebsite.DatabaseBuilder.Director.ChampionAnalyticsDirector;
import com.backendwebsite.DatabaseBuilder.Director.FetchMatchesDirector;
import com.backendwebsite.DatabaseBuilder.Director.FetchPlayersDirector;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.backendwebsite.DatabaseBuilder.Context.BuildPlayerContext;

@RestController
@RequestMapping("api/database")
public class DatabaseController {

    private final FetchPlayersDirector fetchPlayersDirector;
    private final FetchMatchesDirector fetchMatchesDirector;
    private final ChampionAnalyticsDirector championAnalyticsDirector;

    public DatabaseController(FetchPlayersDirector fetchPlayersDirector, FetchMatchesDirector fetchMatchesDirector,
                              ChampionAnalyticsDirector championAnalyticsDirector) {
        this.fetchPlayersDirector = fetchPlayersDirector;
        this.fetchMatchesDirector = fetchMatchesDirector;
        this.championAnalyticsDirector = championAnalyticsDirector;
    }


    @GetMapping("/FetchPlayers")
    public ResponseEntity getPlayers(@RequestBody GetPlayersRequest req) {

        BuildPlayerContext context = new BuildPlayerContext(
                (BuildPlayerContext.Region.valueOf(req.region())),
                req.tier(),
                req.division(),
                req.queue(),
                req.page());

        fetchPlayersDirector.startWork(context);

        // TO DO
        // failed and success logs

        return ResponseEntity.ok("");
    }

    @GetMapping("/FetchMatches")
    public ResponseEntity getPlayers(@RequestBody GetMatchesRequest req) {

        BuildMatchContext context = new BuildMatchContext(BuildMatchContext.Region.valueOf(req.region()), req.playerLimit());

        fetchMatchesDirector.startWork(context);

        return ResponseEntity.ok("");
    }

    @GetMapping("/AnaliseMatches")
    public ResponseEntity analiseMatches(@RequestBody GetAnaliseMatchesRequest req) {

        BuildChampionAnalyticsContext context = new BuildChampionAnalyticsContext(req.limitMatches());

        championAnalyticsDirector.startWork(context);

        return ResponseEntity.ok("");
    }
}
