package com.wintam;

import com.wintam.config.DotenvConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WintamApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(WintamApplication.class);
        app.addInitializers(new DotenvConfig());
        app.run(args);
    }
}