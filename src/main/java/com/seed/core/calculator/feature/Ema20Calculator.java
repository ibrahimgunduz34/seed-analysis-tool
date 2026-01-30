package com.seed.core.calculator.feature;

import com.seed.core.model.HistoricalData;
import com.seed.core.model.ResultKey;

import java.math.BigDecimal;

public class Ema20Calculator<H extends HistoricalData>
        extends AbstractEmaCalculator<H> {

    public static final ResultKey<BigDecimal> EMA_20 =
            ResultKey.of("EMA.20", BigDecimal.class);

    public Ema20Calculator() {
        super(20, EMA_20);
    }
}
