package com.backendwebsite.DatabaseBuilder.Director;

import com.backendwebsite.DatabaseBuilder.Builder.GetPlayersBuilder;
import com.backendwebsite.DatabaseBuilder.Builder.IBuilder;
import com.backendwebsite.DatabaseBuilder.Factory.CommunicationFactory;

public class DatabaseDirector {

    CommunicationFactory communicationFactory;
    public DatabaseDirector(String region) {
        this.communicationFactory = new CommunicationFactory(region);
    }

    public void startWork(){
        getPlayersData(new GetPlayersBuilder(communicationFactory));

        //pullMatchData(new MatchDataBuilder());
    }

    public void getPlayersData(IBuilder builder) {
        builder.build();
    }
    public void pullMatchData(IBuilder builder) {
        builder.build();
    }
}
