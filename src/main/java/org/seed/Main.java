package org.seed;

import org.seed.config.FundPerformanceWeightConfig;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(FundPerformanceWeightConfig.class)
@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplicationBuilder(Main.class)
                .bannerMode(Banner.Mode.LOG)
                .logStartupInfo(true)

                .build();
        app.run(args);
    }
}
