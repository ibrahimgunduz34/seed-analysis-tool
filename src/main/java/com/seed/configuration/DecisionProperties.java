package com.seed.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "calculator.decision")
public record DecisionProperties(
        Thresholds thresholds,
        Confidence confidence
) {

    public record Thresholds(
            Buy buy,
            Sell sell
    ) {
        public record Buy(
                int minFinalScore,
                int minTrend,
                int minRisk,
                double maxMdd
        ) {}

        public record Sell(
                int maxFinalScore,
                int maxTrend,
                double minMdd
        ) {}
    }

    public record Confidence(
            Weights weights,
            Multipliers multipliers,
            Floors floors
    ) {
        public record Weights(
                double trend,
                double momentum,
                double risk,
                double drawdown
        ) {}

        public record Multipliers(
                double buy,
                double sell,
                double hold
        ) {}

        public record Floors(
                int buy,
                int sell,
                int hold
        ) {}
    }
}
