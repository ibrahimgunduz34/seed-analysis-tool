package com.seed.fund.web.dto;

public record AnalysisContextDto(
        FundMetaDataDto metaData,
        AnalysisStatisticsDto statistics
) {}
