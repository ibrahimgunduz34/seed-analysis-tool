package org.seed;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplicationBuilder(Main.class)
                .bannerMode(Banner.Mode.OFF)
                .logStartupInfo(false)

                .build();
        app.run(args);
    }
}