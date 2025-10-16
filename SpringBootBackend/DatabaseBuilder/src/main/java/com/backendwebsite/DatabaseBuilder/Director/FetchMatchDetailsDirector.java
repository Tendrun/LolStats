package com.backendwebsite.DatabaseBuilder.Director;

import com.backendwebsite.DatabaseBuilder.Builder.FetchMatchDetailsBuilder;
import com.backendwebsite.DatabaseBuilder.Context.FetchMatchDetailsContext;
import org.springframework.stereotype.Component;

@Component
public class FetchMatchDetailsDirector {
    private final FetchMatchDetailsBuilder fetchMatchDetailsBuilder;

    public FetchMatchDetailsDirector(FetchMatchDetailsBuilder fetchMatchDetailsBuilder) {
        this.fetchMatchDetailsBuilder = fetchMatchDetailsBuilder;
    }
    public void startWork(FetchMatchDetailsContext context) {
        fetchMatchDetailsBuilder.build(context);
    }
}
