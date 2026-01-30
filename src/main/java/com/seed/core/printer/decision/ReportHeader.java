package com.seed.core.printer.decision;

import com.seed.core.report.BatchReportContext;

import java.time.LocalDateTime;

public class ReportHeader implements Printer {
    @Override
    public void print(BatchReportContext reportContext) {
        System.out.println("Report Generated At: " + LocalDateTime.now());
        System.out.println("Report As Of Date: " + reportContext.asOf());
    }
}
