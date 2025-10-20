package com.backendwebsite.DatabaseBuilder.Controller;

import com.backendwebsite.DatabaseBuilder.Context.BuildChampionAnalyticsContext;
import com.backendwebsite.DatabaseBuilder.Context.FetchMatchDetailsContext;
import com.backendwebsite.DatabaseBuilder.Context.FetchMatchesContext;
import com.backendwebsite.DatabaseBuilder.DTO.AppApi.AnaliseMatches.GetAnaliseMatchesRequest;
import com.backendwebsite.DatabaseBuilder.DTO.AppApi.Match.GetMatchesRequest;
import com.backendwebsite.DatabaseBuilder.DTO.AppApi.MatchDetails.GetMatchDetailsRequest;
import com.backendwebsite.DatabaseBuilder.DTO.AppApi.Player.GetPlayersRequest;
import com.backendwebsite.DatabaseBuilder.Director.ChampionAnalyticsDirector;
import com.backendwebsite.DatabaseBuilder.Director.FetchMatchDetailsDirector;
import com.backendwebsite.DatabaseBuilder.Director.FetchMatchesDirector;
import com.backendwebsite.DatabaseBuilder.Director.FetchPlayersDirector;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.backendwebsite.DatabaseBuilder.Context.FetchPlayersContext;

@RestController
@RequestMapping("api/database")
public class DatabaseController {

    private final FetchPlayersDirector fetchPlayersDirector;
    private final FetchMatchesDirector fetchMatchesDirector;
    private final ChampionAnalyticsDirector championAnalyticsDirector;
    private final FetchMatchDetailsDirector fetchMatchDetailsDirector;


    public DatabaseController(FetchPlayersDirector fetchPlayersDirector, FetchMatchesDirector fetchMatchesDirector,
                              ChampionAnalyticsDirector championAnalyticsDirector,
                              FetchMatchDetailsDirector fetchMatchDetailsDirector) {
        this.fetchPlayersDirector = fetchPlayersDirector;
        this.fetchMatchesDirector = fetchMatchesDirector;
        this.championAnalyticsDirector = championAnalyticsDirector;
        this.fetchMatchDetailsDirector = fetchMatchDetailsDirector;
    }


    @GetMapping("/FetchPlayers")
    public ResponseEntity getPlayers(@RequestBody GetPlayersRequest req) {

        FetchPlayersContext context = new FetchPlayersContext(
                (FetchPlayersContext.Region.valueOf(req.region())),
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

        FetchMatchesContext context = new FetchMatchesContext(FetchMatchesContext.Region.valueOf(req.region()),
                req.playerLimit(), req.type(), req.tier());

        fetchMatchesDirector.startWork(context);

        return ResponseEntity.ok("");
    }

    @GetMapping("/FetchMatchDetails")
    public ResponseEntity FetchMatchDetails(@RequestBody GetMatchDetailsRequest req) {

        FetchMatchDetailsContext context = new FetchMatchDetailsContext(req.playerMatchLimit(), req.region());

        fetchMatchDetailsDirector.startWork(context);

        return ResponseEntity.ok(context.logs);
    }

    @GetMapping("/AnaliseMatches")
    public ResponseEntity analiseMatches(@RequestBody GetAnaliseMatchesRequest req) {

        BuildChampionAnalyticsContext context = new BuildChampionAnalyticsContext(req.limitMatches());

        championAnalyticsDirector.startWork(context);

        return ResponseEntity.ok(context.logs);
    }
}
