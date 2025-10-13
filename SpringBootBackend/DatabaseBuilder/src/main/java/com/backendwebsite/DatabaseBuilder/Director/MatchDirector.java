package com.backendwebsite.DatabaseBuilder.Director;

import com.backendwebsite.DatabaseBuilder.Builder.GetMatchesBuilder;
import com.backendwebsite.DatabaseBuilder.Builder.GetPlayersBuilder;
import com.backendwebsite.DatabaseBuilder.Context.BuildMatchContext;
import com.backendwebsite.DatabaseBuilder.Context.BuildPlayerContext;
import org.springframework.stereotype.Component;

@Component
public class MatchDirector {
    private final GetMatchesBuilder getMatchesBuilder;
    public MatchDirector(GetMatchesBuilder getMatchesBuilder) { this.getMatchesBuilder = getMatchesBuilder; }
    public void startWork(BuildMatchContext context) {
        getMatchesBuilder.build(context);
    }

}
