package com.esolrpe.server.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Component
@ConfigurationProperties(prefix="config")
public class Config {
    @Valid
    @NotNull
    private String databaseConfig;

    public String getDatabaseConfig() {
        return databaseConfig;
    }

    public void setDatabaseConfig(String databaseConfig) {
        this.databaseConfig = databaseConfig;
    }
}
