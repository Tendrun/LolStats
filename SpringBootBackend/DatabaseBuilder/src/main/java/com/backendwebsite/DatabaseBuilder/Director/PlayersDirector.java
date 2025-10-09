package com.backendwebsite.DatabaseBuilder.Director;

import com.backendwebsite.DatabaseBuilder.Builder.GetPlayersBuilder;
import com.backendwebsite.DatabaseBuilder.Context.BuildPlayerContext;
import org.springframework.stereotype.Component;

@Component
public class PlayersDirector {
    private final GetPlayersBuilder playersBuilder;
    public PlayersDirector(GetPlayersBuilder playersBuilder) { this.playersBuilder = playersBuilder; }
    public void startWork(BuildPlayerContext context) {
        playersBuilder.build(context);
    }
}
