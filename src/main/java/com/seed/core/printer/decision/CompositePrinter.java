package com.seed.core.printer.decision;

import com.seed.core.report.BatchReportContext;

public class CompositePrinter implements Printer {
    private final Printer[] printers;

    public CompositePrinter(Printer... printers) {
        this.printers = printers;
    }

    @Override
    public void print(BatchReportContext reportContext) {
        for (Printer printer : printers) {
            printer.print(reportContext);
        }
    }
}
