package com.backendwebsite.DatabaseBuilder.Controller;

import com.backendwebsite.DatabaseBuilder.Context.BuildChampionAnalyticsContext;
import com.backendwebsite.DatabaseBuilder.Context.FetchMatchDetailsContext;
import com.backendwebsite.DatabaseBuilder.Context.FetchMatchesContext;
import com.backendwebsite.DatabaseBuilder.DTO.AppApi.AnaliseMatches.GetAnaliseMatchesRequest;
import com.backendwebsite.DatabaseBuilder.DTO.AppApi.Match.GetMatchesRequest;
import com.backendwebsite.DatabaseBuilder.DTO.AppApi.MatchDetails.GetMatchDetailsRequest;
import com.backendwebsite.DatabaseBuilder.DTO.AppApi.Player.GetPlayersRequest;
import com.backendwebsite.DatabaseBuilder.DTO.StepLogSummary;
import com.backendwebsite.DatabaseBuilder.Director.ChampionAnalyticsDirector;
import com.backendwebsite.DatabaseBuilder.Director.FetchMatchDetailsDirector;
import com.backendwebsite.DatabaseBuilder.Director.FetchMatchesDirector;
import com.backendwebsite.DatabaseBuilder.Director.FetchPlayersDirector;
import com.backendwebsite.DatabaseBuilder.Step.Log.StepLog;
import com.backendwebsite.DatabaseBuilder.Step.StepsOrder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.backendwebsite.DatabaseBuilder.Context.FetchPlayersContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private Map<String, StepLogSummary> buildLogResponse(HashMap<String, List<StepLog>> contextLogs) {
        Map<String, StepLogSummary> summary = new HashMap<>();

        contextLogs.forEach((stepName, logs) -> {
            summary.put(stepName, StepLogSummary.fromStepLogs(stepName, logs));
        });

        return summary;
    }


    @PostMapping("/FetchPlayers")
    public ResponseEntity<Map<String, StepLogSummary>> getPlayers(@RequestBody GetPlayersRequest req) {
        FetchPlayersContext context = new FetchPlayersContext(
                req.region(),
                req.tier(),
                req.division(),
                req.queue(),
                req.page());

        fetchPlayersDirector.startWork(context);
        return ResponseEntity.ok(buildLogResponse(context.logs));
    }

    @PostMapping("/FetchMatches")
    public ResponseEntity<Map<String, StepLogSummary>> getMatches(@RequestBody GetMatchesRequest req) {
        FetchMatchesContext context = new FetchMatchesContext(req.region(),
                req.playerLimit(), req.type(), req.tier());

        fetchMatchesDirector.startWork(context);
        return ResponseEntity.ok(buildLogResponse(context.logs));
    }

    @PostMapping("/FetchMatchDetails")
    public ResponseEntity<Map<String, StepLogSummary>> fetchMatchDetails(@RequestBody GetMatchDetailsRequest req) {
        FetchMatchDetailsContext context = new FetchMatchDetailsContext(req.playerMatchLimit(), req.region());

        fetchMatchDetailsDirector.startWork(context);
        return ResponseEntity.ok(buildLogResponse(context.logs));
    }

    @PostMapping("/AnaliseMatches")
    public ResponseEntity<Map<String, StepLogSummary>> analiseMatches(@RequestBody GetAnaliseMatchesRequest req) {
        BuildChampionAnalyticsContext context = new BuildChampionAnalyticsContext(req.limitMatches());

        championAnalyticsDirector.startWork(context);
        return ResponseEntity.ok(buildLogResponse(context.logs));
    }
}
