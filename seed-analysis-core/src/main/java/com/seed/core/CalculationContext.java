package com.seed.core;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class CalculationContext {
    private final Asset asset;

    private List<BigDecimal> dailyChanges;
    private int numberOfDays;

    private int numberOfPositiveDays;
    private int numberOfNegativeDays;
    private double weightOfPositiveDays;
    private double weightOfNegativeDays;

    private BigDecimal mean;
    private BigDecimal standardDeviation;

    private BigDecimal priceChange;
    private BigDecimal averageGain;
    private BigDecimal averageLoss;
    private BigDecimal sharpeRatio;
    private BigDecimal maxDrawDown;
    private BigDecimal sortinoRatio;
}
