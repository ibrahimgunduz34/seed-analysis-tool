package com.seed.core.printer;

import com.seed.core.AnalysisContext;
import com.seed.core.AnalysisStatistics;
import com.seed.core.model.HistoricalData;
import com.seed.core.model.MetaData;
import de.vandermeer.asciitable.AsciiTable;

import java.util.List;

public class InfoTable implements Printer {
    @Override
    public <M extends MetaData, H extends HistoricalData> void print(List<AnalysisContext<M, H>> contexts) {
        AsciiTable asciiTable = new AsciiTable();
        asciiTable.addRule();
        asciiTable.addRow(
                "Code",
                "Price Change %",
                "Mean Return %",
                "(+) Days %",
                "(-) Days %",
                "Avg. Gain %",
                "Avg Loss %",
                "MDD",
                "St. Dev",
                "Sharpe Ratio",
                "Sortino Ratio");
        asciiTable.addRule();
        for (AnalysisContext<?, ?> ctx : contexts) {
            AnalysisStatistics statistics = ctx.getStatistics();

            asciiTable.addRow(
                    ctx.getMetaData().code(),
                    String.format("%.2f", statistics.priceChange()),
                    String.format("%.2f", statistics.mean()),
                    String.format("%.2f", statistics.weightOfPositiveDays()),
                    String.format("%.2f", statistics.weightOfNegativeDays()),
                    String.format("%.2f", statistics.avgGain()),
                    String.format("%.2f", statistics.avgLoss()),
                    String.format("%.2f", statistics.mdd()),
                    String.format("%.2f", statistics.stDev()),
                    String.format("%.2f", statistics.sharpeRatio()),
                    String.format("%.2f", statistics.sortino())
            );
            asciiTable.addRule();
        }
        System.out.println(asciiTable.render());
    }
}
