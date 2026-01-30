package com.seed.core.printer.decision;

import com.seed.core.report.BatchReportContext;

public interface Printer {
    void print(BatchReportContext reportContext);
}
