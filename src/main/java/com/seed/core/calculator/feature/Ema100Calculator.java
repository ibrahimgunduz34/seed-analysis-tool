package com.seed.core.calculator.feature;

import com.seed.core.model.HistoricalData;
import com.seed.core.model.ResultKey;

import java.math.BigDecimal;

public class Ema100Calculator<H extends HistoricalData>
        extends AbstractEmaCalculator<H> {

    public static final ResultKey<BigDecimal> EMA_100 =
            ResultKey.of("EMA.100", BigDecimal.class);

    public Ema100Calculator() {
        super(100, EMA_100);
    }
}
