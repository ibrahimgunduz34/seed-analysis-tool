package com.seed.core;

import com.seed.core.calculator.*;
import org.springframework.stereotype.Component;

@Component
public class CompositeCalculator implements Calculator {
    private final DailyChangeCalculator dailyChangeCalculator;
    private final PositiveNegativeDaysCalculator positiveNegativeDaysCalculator;
    private final MeanCalculator meanCalculator;
    private final StandardDeviationCalculator standardDeviationCalculator;
    private final PriceChangeCalculator priceChangeCalculator;
    private final GainLossCalculator gainLossCalculator;
    private final SharpeRatioCalculator sharpeRatioCalculator;
    private final MaxDrawDownCalculator maxDrawDownCalculator;
    private final SortinoCalculator sortinoCalculator;

    public CompositeCalculator(DailyChangeCalculator dailyChangeCalculator,
                               PositiveNegativeDaysCalculator positiveNegativeDaysCalculator,
                               MeanCalculator meanCalculator,
                               StandardDeviationCalculator standardDeviationCalculator,
                               PriceChangeCalculator priceChangeCalculator,
                               GainLossCalculator gainLossCalculator,
                               SharpeRatioCalculator sharpeRatioCalculator,
                               MaxDrawDownCalculator maxDrawDownCalculator,
                               SortinoCalculator sortinoCalculator) {
        this.dailyChangeCalculator = dailyChangeCalculator;
        this.positiveNegativeDaysCalculator = positiveNegativeDaysCalculator;
        this.meanCalculator = meanCalculator;
        this.standardDeviationCalculator = standardDeviationCalculator;
        this.priceChangeCalculator = priceChangeCalculator;
        this.gainLossCalculator = gainLossCalculator;
        this.sharpeRatioCalculator = sharpeRatioCalculator;
        this.maxDrawDownCalculator = maxDrawDownCalculator;
        this.sortinoCalculator = sortinoCalculator;
    }

    @Override
    public void calculate(CalculationContext ctx) {
        dailyChangeCalculator.calculate(ctx);
        positiveNegativeDaysCalculator.calculate(ctx);
        meanCalculator.calculate(ctx);
        standardDeviationCalculator.calculate(ctx);
        priceChangeCalculator.calculate(ctx);
        gainLossCalculator.calculate(ctx);
        sharpeRatioCalculator.calculate(ctx);
        maxDrawDownCalculator.calculate(ctx);
        sortinoCalculator.calculate(ctx);
    }
}
