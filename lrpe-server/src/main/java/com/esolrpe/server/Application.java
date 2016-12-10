package com.esolrpe.server;

import com.esolrpe.server.config.Config;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(Config.class)
public class Application {
    public static final int APPLICATION_VERSION = 1;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
