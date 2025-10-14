package com.backendwebsite.DatabaseBuilder.Director;

import com.backendwebsite.DatabaseBuilder.Builder.FetchMatchesBuilder;
import com.backendwebsite.DatabaseBuilder.Context.BuildMatchContext;
import org.springframework.stereotype.Component;

@Component
public class FetchMatchesDirector {
    private final FetchMatchesBuilder fetchMatchesBuilder;
    public FetchMatchesDirector(FetchMatchesBuilder fetchMatchesBuilder) { this.fetchMatchesBuilder = fetchMatchesBuilder; }
    public void startWork(BuildMatchContext context) {
        fetchMatchesBuilder.build(context);
    }

}
