package com.backendwebsite.Api.Config;

import com.backendwebsite.DatabaseBuilder.Client.CouchDBClient;
import com.backendwebsite.DatabaseBuilder.Factory.CommunicationFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {
        "com.backendwebsite.DatabaseBuilder.Client",
        "com.backendwebsite.DatabaseBuilder.Factory"
})
public class AppConfiguration {
    @Bean
    public CouchDBClient couchDBClient() {
        return new CouchDBClient(new ObjectMapper(), new CommunicationFactory());
    }
}
