package com.backendwebsite.lolstats.CouchDB;

import org.lightcouch.CouchDbClient;
import org.lightcouch.CouchDbProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CouchDbConfig {

    @Bean
    public CouchDbClient couchDbClient() {
        CouchDbProperties properties = new CouchDbProperties()
                .setDbName("championdetails")
                .setCreateDbIfNotExist(true)
                .setProtocol("http")
                .setHost("localhost")
                .setPort(5984)
                .setUsername("admin")
                .setPassword("admin");
        return new CouchDbClient(properties);
    }
}

