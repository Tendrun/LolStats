package com.backendwebsite.DatabaseBuilder.Director;

import com.backendwebsite.DatabaseBuilder.Builder.FetchPlayersBuilder;
import com.backendwebsite.DatabaseBuilder.Context.FetchPlayersContext;
import org.springframework.stereotype.Component;

@Component
public class FetchPlayersDirector {
    private final FetchPlayersBuilder playersBuilder;
    public FetchPlayersDirector(FetchPlayersBuilder playersBuilder) { this.playersBuilder = playersBuilder; }
    public void startWork(FetchPlayersContext context) {
        playersBuilder.build(context);
    }
}
