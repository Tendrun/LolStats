package com.backendwebsite.DatabaseBuilder.Step.ChampionAnalytics;

import com.backendwebsite.DatabaseBuilder.Constant.ChampionStatsMap;
import com.backendwebsite.DatabaseBuilder.Context.BuildChampionAnalyticsContext;
import com.backendwebsite.DatabaseBuilder.DTO.RiotApi.MatchDetails.MatchDTO;
import com.backendwebsite.DatabaseBuilder.Step.IStep;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CalculateChampStatsStep implements IStep<BuildChampionAnalyticsContext> {
    @Override
    public void execute(BuildChampionAnalyticsContext context) {
        for (MatchDTO match : context.matchDetails) {
            // Pick Rate
            match.info.participants.stream()
                    .forEach(p ->
                        context.championStatsMap.CHAMPION_MAP.stream()
                            .filter(c -> c.Id == p.championId)
                            .findFirst()
                            .ifPresent(champ -> champ.totalMatchesPicked++)
            );


            // Find banned champion ids
            List<Integer> ids = match.info.teams.stream()
                    .flatMap(team -> team.bans.stream())
                    .map(ban -> ban.championId)
                    .toList();

            List<ChampionStatsMap.ChampionDetails> bannedChampions = context.championStatsMap.CHAMPION_MAP.stream()
                    .filter(champ -> ids.contains(champ.Id)) // jeśli id championa jest na liście banów
                    .toList(); // zwraca listę championów


            // Get banned champion
            context.championStatsMap.ALL_BAN_COUNT++;


            // Estimate Ban

            // Win Rate

        }
    }


}
