package com.seed.core.report;

import java.time.LocalDate;
import java.util.List;

public record BatchReportContext(
        LocalDate asOf,
        List<ReportContext> assets
) {}
