package com.seed.core.printer;

import com.seed.core.AnalysisContext;
import com.seed.core.model.HistoricalData;
import com.seed.core.model.MetaData;

import java.util.List;

public class CompositePrinter implements Printer {
    private final Printer[] printers;

    public CompositePrinter(Printer... printers) {
        this.printers = printers;
    }

    @Override
    public <M extends MetaData, H extends HistoricalData> void print(List<AnalysisContext<M, H>> contexts) {
        for (Printer printer : printers) {
            printer.print(contexts);
        }
    }
}
