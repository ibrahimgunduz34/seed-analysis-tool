package com.seed.core;

import java.math.BigDecimal;

public record AnalysisStatistics(
        BigDecimal priceChange,
        BigDecimal mean,
        Double weightOfPositiveDays,
        Double weightOfNegativeDays,
        BigDecimal avgGain,
        BigDecimal avgLoss,
        BigDecimal mdd,
        BigDecimal stDev,
        BigDecimal sharpeRatio,
        BigDecimal sortino
) {
}
