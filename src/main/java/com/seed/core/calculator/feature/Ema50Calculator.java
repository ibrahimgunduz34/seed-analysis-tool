package com.seed.core.calculator.feature;

import com.seed.core.model.HistoricalData;
import com.seed.core.model.ResultKey;

import java.math.BigDecimal;

public class Ema50Calculator<H extends HistoricalData>
        extends AbstractEmaCalculator<H> {

    public static final ResultKey<BigDecimal> EMA_50 =
            ResultKey.of("EMA.50", BigDecimal.class);

    public Ema50Calculator() {
        super(50, EMA_50);
    }
}
