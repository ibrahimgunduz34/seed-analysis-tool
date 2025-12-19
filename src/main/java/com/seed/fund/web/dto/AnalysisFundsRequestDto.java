package com.seed.fund.web.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public record AnalysisFundsRequestDto(
        List<String> codes,
        LocalDate startDate,
        LocalDate endDate,
        Optional<AnalysisOptions> options
) {}
