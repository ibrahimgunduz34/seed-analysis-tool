package org.seed.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

@ConfigurationProperties(prefix = "performance.funds.weight")
@Getter
public class FundPerformanceWeightConfig {
    private final double sharpe;
    private final double mdd;
    private final double returnPct;

    @ConstructorBinding
    public FundPerformanceWeightConfig(double sharpe, double mdd, double returnPct) {
        this.sharpe = sharpe;
        this.mdd = mdd;
        this.returnPct = returnPct;
    }
}
