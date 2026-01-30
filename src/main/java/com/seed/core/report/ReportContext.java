package com.seed.core.report;

import com.seed.core.calculator.decision.DecisionCalculator;

import java.math.BigDecimal;

public record ReportContext(
        String code,
        String name,

        int finalScore,
        int momentumScore,
        int riskScore,

        int trendSignal,
        String trendLabel,

        DecisionCalculator.Decision decision,
        int confidence,

        BigDecimal ret1m,
        BigDecimal ret3m,
        BigDecimal ret6m,
        BigDecimal ret12m,

        BigDecimal vol21,
        BigDecimal mdd6m,
        BigDecimal sharpe3m,
        BigDecimal sortino3m
) {}
