package com.seed.core.printer.decision;

import com.seed.core.report.BatchReportContext;
import com.seed.core.report.ReportContext;
import de.vandermeer.asciitable.AT_Context;
import de.vandermeer.asciitable.AsciiTable;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class DecisionTable implements Printer {
    @Override
    public void print(BatchReportContext reportContext) {
        AT_Context atContext = new AT_Context();
        atContext.setWidth(160);

        AsciiTable asciiTable = new AsciiTable(atContext);

        asciiTable.addRule();
        asciiTable.addRow(
                "Fon", "Final", "Mom", "Risk", "Trend",
                "Karar", "Güven",
                "1A", "3A", "6A", "12A",
                "Vol", "MDD", "Sharpe", "Sortino"
        );
        asciiTable.addRule();

        for (ReportContext r : reportContext.assets()) {
            asciiTable.addRow(
                    r.code(),
                    r.finalScore(),
                    r.momentumScore(),
                    r.riskScore(),
                    formatTrend(r.trendSignal(), r.trendLabel()),
                    formatDecision(r.decision()),
                    r.confidence(),

                    pct(r.ret1m()),
                    pct(r.ret3m()),
                    pct(r.ret6m()),
                    pct(r.ret12m()),

                    pct(r.vol21()),
                    pct(r.mdd6m()),
                    round(r.sharpe3m()),
                    round(r.sortino3m())
            );
            asciiTable.addRule();
        }
        System.out.println(asciiTable.render());
    }

    private String pct(BigDecimal v) {
        return v == null ? "-" : v.multiply(BigDecimal.valueOf(100))
                .setScale(2, RoundingMode.HALF_UP) + "%";
    }

    private String round(BigDecimal v) {
        return v == null ? "-" : v.setScale(2, RoundingMode.HALF_UP).toString();
    }

    private String formatTrend(int signal, String label) {
        return switch (signal) {
            case 2 -> "↑↑ " + label;
            case 1 -> "↑  " + label;
            case 0 -> "→  " + label;
            case -1 -> "↓  " + label;
            case -2 -> "↓↓ " + label;
            default -> String.valueOf(signal);
        };
    }

    private String formatDecision(Object decision) {
        return decision == null ? "-" : decision.toString();
    }
}
