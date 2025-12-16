package com.seed.core.printer;

import com.seed.core.AnalysisContext;
import com.seed.core.model.HistoricalData;
import com.seed.core.model.MetaData;

import java.util.List;
import java.util.Optional;

public class ReportHeader implements Printer {
    @Override
    public <M extends MetaData, H extends HistoricalData> void print(List<AnalysisContext<M, H>> analysisContexts) {
        Optional<AnalysisContext<M, H>> context = analysisContexts.stream().findFirst();

        context.ifPresent(ctx -> {
            System.out.println("Analysis Period: " + ctx.getStartDate() + " to " + ctx.getEndDate());
            System.out.println("=".repeat(160));
            System.out.println();
        });
    }
}
