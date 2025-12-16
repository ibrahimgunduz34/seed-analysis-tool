package com.seed.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "performance.funds.weight")
public record ReportConfiguration(
        double sharpe,
        double mdd,
        double returnPct
) {}
