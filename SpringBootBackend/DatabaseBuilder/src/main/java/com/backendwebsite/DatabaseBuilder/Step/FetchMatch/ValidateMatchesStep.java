package com.backendwebsite.DatabaseBuilder.Step.FetchMatch;

import com.backendwebsite.DatabaseBuilder.Context.BuildMatchContext;
import com.backendwebsite.DatabaseBuilder.Step.IStep;
import org.springframework.stereotype.Component;

@Component
public class ValidateMatchesStep implements IStep<BuildMatchContext> {

    @Override
    public void execute(BuildMatchContext context) {
        /*
        try {
            for (List<Match> matches : context.fetchedMatches.values()) {
                for (Match match : matches) {

                }
            }
            System.out.println("Request is send to CouchDB");
        } catch (Exception e) {
            e.printStackTrace();
        }
         */
    }
}
