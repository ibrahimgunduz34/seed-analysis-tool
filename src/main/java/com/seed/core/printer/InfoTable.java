package com.seed.core.printer;

import com.seed.core.AnalysisContext;
import com.seed.core.AnalysisStatistics;
import com.seed.core.model.HistoricalData;
import com.seed.core.model.MetaData;
import com.seed.core.util.BigDecimalMath;
import de.vandermeer.asciitable.AT_Context;
import de.vandermeer.asciitable.AsciiTable;

import java.math.BigDecimal;
import java.util.List;

public class InfoTable implements Printer {
    @Override
    public <M extends MetaData, H extends HistoricalData> void print(List<AnalysisContext<M, H>> contexts) {
        AT_Context atContext = new AT_Context();
        atContext.setWidth(160);

        AsciiTable asciiTable = new AsciiTable(atContext);

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
                "Sortino Ratio"
        );
        asciiTable.addRule();
        for (AnalysisContext<?, ?> ctx : contexts) {
            AnalysisStatistics statistics = ctx.getStatistics();

            asciiTable.addRow(
                    ctx.getMetaData().code(),
                    String.format("%.2f", statistics.priceChange().multiply(BigDecimal.valueOf(100))),
                    String.format("%.2f", statistics.mean().multiply(BigDecimal.valueOf(100))),
                    String.format("%.2f", statistics.weightOfPositiveDays() * 100),
                    String.format("%.2f", statistics.weightOfNegativeDays() * 100),
                    String.format("%.2f", statistics.avgGain().multiply(BigDecimal.valueOf(100))),
                    String.format("%.2f", statistics.avgLoss().multiply(BigDecimal.valueOf(100))),
                    String.format("%.2f", statistics.mdd().multiply(BigDecimal.valueOf(100))),
                    String.format("%.2f", BigDecimalMath.convertToAnnual(statistics.stDev())),
                    String.format("%.2f", BigDecimalMath.convertToAnnual(statistics.sharpeRatio())),
                    String.format("%.2f", BigDecimalMath.convertToAnnual(statistics.sortino()))
            );
            asciiTable.addRule();
        }
        System.out.println(asciiTable.render());
    }
}
