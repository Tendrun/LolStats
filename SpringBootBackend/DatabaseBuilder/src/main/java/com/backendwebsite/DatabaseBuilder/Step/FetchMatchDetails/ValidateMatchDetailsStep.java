package com.backendwebsite.DatabaseBuilder.Step.FetchMatchDetails;

import com.backendwebsite.DatabaseBuilder.Context.FetchMatchDetailsContext;
import com.backendwebsite.DatabaseBuilder.DTO.RiotApi.MatchDetails.MatchDTO;
import com.backendwebsite.DatabaseBuilder.Step.IStep;
import org.springframework.stereotype.Component;

@Component
public class ValidateMatchDetailsStep implements IStep<FetchMatchDetailsContext> {

    @Override
    public void execute(FetchMatchDetailsContext context) {
        try {
            for (MatchDTO match : context.fetchedMatchDetails) {

                if (match.metadata.matchId == null) {
                    // TO DO
                    // set failed request
                    continue;
                }

                match._id = "matchDetail:" + context.region + ":" + match.metadata.matchId;

                context.validatedMatchDetails.add(match);
            }
            System.out.println("Request is send to CouchDB");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
