package com.backendwebsite.lolstats;

import Database.DatabaseBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LoLStatsApplication {

    public static void main(String[] args) {
        SpringApplication.run(LoLStatsApplication.class, args);

        //DatabaseBuilder DatabaseBuilder = new DatabaseBuilder();
        //DatabaseBuilder.buildDatabase();
    }

}
