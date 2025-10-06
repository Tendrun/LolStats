package com.backendwebsite.DatabaseBuilder.Director;

import com.backendwebsite.DatabaseBuilder.Builder.GetPlayersBuilder;
import com.backendwebsite.DatabaseBuilder.Context.BuildMatchContext;
import org.springframework.stereotype.Component;

@Component
public class MatchDatabaseDirector {
    private final GetPlayersBuilder playersBuilder;
    public MatchDatabaseDirector(GetPlayersBuilder playersBuilder) { this.playersBuilder = playersBuilder; }
    public void startWork(BuildMatchContext context) {
        playersBuilder.build(context);
    }
}
