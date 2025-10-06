package com.backendwebsite.DatabaseBuilder.Director;

import com.backendwebsite.DatabaseBuilder.Builder.GetPlayersBuilder;
import com.backendwebsite.DatabaseBuilder.Context.BuildMatchContext;
import org.springframework.stereotype.Component;

@Component
public class DatabaseDirector {
    private final GetPlayersBuilder playersBuilder;
    public DatabaseDirector(GetPlayersBuilder playersBuilder) { this.playersBuilder = playersBuilder; }
    public void startWork(BuildMatchContext context) {
        playersBuilder.build(context);
    }
}
