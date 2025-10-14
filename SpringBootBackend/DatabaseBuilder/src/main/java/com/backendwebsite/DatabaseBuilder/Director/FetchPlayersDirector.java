package com.backendwebsite.DatabaseBuilder.Director;

import com.backendwebsite.DatabaseBuilder.Builder.FetchPlayersBuilder;
import com.backendwebsite.DatabaseBuilder.Context.BuildPlayerContext;
import org.springframework.stereotype.Component;

@Component
public class FetchPlayersDirector {
    private final FetchPlayersBuilder playersBuilder;
    public FetchPlayersDirector(FetchPlayersBuilder playersBuilder) { this.playersBuilder = playersBuilder; }
    public void startWork(BuildPlayerContext context) {
        playersBuilder.build(context);
    }
}
