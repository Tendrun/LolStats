package com.backendwebsite.DatabaseBuilder.Step.ChampionAnalytics;

import com.backendwebsite.DatabaseBuilder.Constant.ChampionStatsMap.ChampionDetails;
import com.backendwebsite.DatabaseBuilder.Context.BuildChampionAnalyticsContext;
import com.backendwebsite.DatabaseBuilder.DTO.RiotApi.MatchDetails.MatchDTO;
import com.backendwebsite.DatabaseBuilder.Step.IStep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CalculateChampStatsStep implements IStep<BuildChampionAnalyticsContext> {
    private static final Logger logger = LoggerFactory.getLogger(CalculateChampStatsStep.class);

    @Override
    public void execute(BuildChampionAnalyticsContext context) {
        long startTime = System.currentTimeMillis();
        try {
            // Count match Details
            for (MatchDTO match : context.matchDetails) {
                // Pick champion counting
                match.info.participants
                    .forEach(p ->
                        context.championStatsMap.CHAMPION_MAP.stream()
                            .filter(c -> c.championId == p.championId)
                            .findFirst()
                            .ifPresent(champ -> {
                                champ.totalMatchesPicked++;
                                context.championStatsMap.ALL_MATCHES_PLAYED++;
                            }
                    )
                );

                // Ban champion counting
                match.info.teams.stream()
                    .flatMap(team -> team.bans.stream())
                    .forEach(ban ->
                        context.championStatsMap.CHAMPION_MAP.stream()
                        .filter(c -> c.championId == ban.championId)
                        .findFirst()
                        .ifPresent(champBan -> {
                            champBan.bannedMatches++;
                            context.championStatsMap.ALL_BAN_COUNT++;
                        }
                    )
                );

                // Win/Lose counting
                match.info.participants
                    .forEach(p -> context.championStatsMap.CHAMPION_MAP.stream()
                        .filter(c -> p.championId == c.championId && p.win)
                        .findFirst()
                        .ifPresent(champWon -> champWon.wonMatches++
                    )
                );
            }

            for (ChampionDetails championDetails : context.championStatsMap.CHAMPION_MAP) {
                if(championDetails.totalMatchesPicked != 0)
                    championDetails.setWinRate((float) championDetails.wonMatches / championDetails.totalMatchesPicked);
                championDetails.setBanRate((float) championDetails.bannedMatches / context.championStatsMap.ALL_BAN_COUNT);
                championDetails.setPickRate((float) championDetails.totalMatchesPicked /
                        context.championStatsMap.ALL_MATCHES_PLAYED);
            }

            context.logs.computeIfAbsent(getClass().getSimpleName(), k -> new java.util.ArrayList<>())
                    .add(new com.backendwebsite.DatabaseBuilder.Step.Log.StepLog(
                    com.backendwebsite.DatabaseBuilder.Step.StepsOrder.RequestStatus.SUCCESSFUL,
                    this.getClass().getSimpleName(),
                    "Champion stats calculation completed. Processed matches: " + context.matchDetails.size(),
                    System.currentTimeMillis() - startTime,
                    ""
            ));
            logger.info("Champion stats calculation completed. Processed matches: {}", context.matchDetails.size());
        } catch (Exception e) {
            context.logs.computeIfAbsent(getClass().getSimpleName(), k -> new java.util.ArrayList<>())
                    .add(new com.backendwebsite.DatabaseBuilder.Step.Log.StepLog(
                    com.backendwebsite.DatabaseBuilder.Step.StepsOrder.RequestStatus.FAILED,
                    this.getClass().getSimpleName(),
                    "Exception during champion stats calculation: " + e.getMessage(),
                    System.currentTimeMillis() - startTime,
                    ""
            ));
            logger.error("Exception during champion stats calculation", e);
        }
    }
}
