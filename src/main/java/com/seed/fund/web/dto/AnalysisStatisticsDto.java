package com.seed.fund.web.dto;

import java.math.BigDecimal;

public record AnalysisStatisticsDto(
        BigDecimal priceChange,
        BigDecimal mean,
        Double weightOfPositiveDays,
        Double weightOfNegativeDays,
        BigDecimal avgGain,
        BigDecimal avgLoss,
        BigDecimal mdd,
        BigDecimal stDev,
        BigDecimal sharpeRatio,
        BigDecimal sortino,
        Integer performanceRating
) {}
