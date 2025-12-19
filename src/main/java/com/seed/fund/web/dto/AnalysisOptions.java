package com.seed.fund.web.dto;

public record AnalysisOptions(
        double sharpe,
        double mdd,
        double returnPct
) {}
