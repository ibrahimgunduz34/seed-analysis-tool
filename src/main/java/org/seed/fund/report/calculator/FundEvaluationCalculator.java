package org.seed.fund.report.calculator;

import org.seed.fund.report.model.ReportContext;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Function;

public class FundEvaluationCalculator implements Function<List<ReportContext>, List<ReportContext>> {

    @Override
    public List<ReportContext> apply(List<ReportContext> ctxList) {
        if (ctxList == null || ctxList.isEmpty()) return ctxList;

        // Karşılaştırmalarda referans için min/max değerler
        double maxSharpe = ctxList.stream().mapToDouble(c -> safeDouble(c.getSharpeRatio())).max().orElse(0);
        double maxSortino = ctxList.stream().mapToDouble(c -> safeDouble(c.getSortinoRatio())).max().orElse(0);
        double maxReturn = ctxList.stream().mapToDouble(c -> safeDouble(c.getPriceChange())).max().orElse(0);
        double minVolatility = ctxList.stream().mapToDouble(c -> safeDouble(c.getStandardDeviation())).min().orElse(0);
        double maxVolatility = ctxList.stream().mapToDouble(c -> safeDouble(c.getStandardDeviation())).max().orElse(0);
        double minMdd = ctxList.stream().mapToDouble(c -> safeDouble(c.getMaxDrawdown())).min().orElse(0);
        double maxMdd = ctxList.stream().mapToDouble(c -> safeDouble(c.getMaxDrawdown())).max().orElse(0);

        for (ReportContext ctx : ctxList) {
            StringBuilder evaluation = new StringBuilder();

            // Fon adı başa ekleniyor
            evaluation.append(ctx.getMetaData().getCode()).append(":\n");

            double sharpe = safeDouble(ctx.getSharpeRatio());
            double sortino = safeDouble(ctx.getSortinoRatio());
            double priceChange = safeDouble(ctx.getPriceChange());
            double volatility = safeDouble(ctx.getStandardDeviation());
            double mdd = safeDouble(ctx.getMaxDrawdown());

            // Risk-düzeltilmiş getiri
            if (sharpe >= maxSharpe * 0.8) {
                evaluation.append("- yüksek risk-düzeltilmiş getiri\n");
            } else {
                evaluation.append("- sınırlı risk-düzeltilmiş getiri\n");
            }

            // Downside risk-düzeltilmiş getiri (Sortino)
            if (sortino >= maxSortino * 0.8) {
                evaluation.append("- çok yüksek downside risk-düzeltilmiş getiri\n");
            } else if (sortino >= maxSortino * 0.5) {
                evaluation.append("- yüksek downside risk-düzeltilmiş getiri\n");
            } else {
                evaluation.append("- sınırlı downside risk-düzeltilmiş getiri\n");
            }

            // Getiri
            if (priceChange >= maxReturn * 0.8) {
                evaluation.append("- çok güçlü getiri\n");
            } else if (priceChange >= maxReturn * 0.5) {
                evaluation.append("- güçlü getiri\n");
            } else {
                evaluation.append("- ılımlı getiri\n");
            }

            // Volatilite (min–max bandına göre)
            if (volatility <= (minVolatility + (maxVolatility - minVolatility) * 0.33)) {
                evaluation.append("- düşük volatilite\n");
            } else if (volatility <= (minVolatility + (maxVolatility - minVolatility) * 0.66)) {
                evaluation.append("- orta volatilite\n");
            } else {
                evaluation.append("- yüksek volatilite\n");
            }

            // Max Drawdown yorumu
            double mddPct = mdd * 100;
            if (mddPct > -5) {
                evaluation.append("- düşük düzeyde gerileme riski görülmüş\n");
            } else if (mddPct > -15) {
                evaluation.append("- orta düzeyde gerileme riski görülmüş\n");
            } else {
                evaluation.append("- çok yüksek gerileme riski yaşanmış\n");
            }
            evaluation.append(String.format("- dönem içinde maksimum %.2f%% kayıp\n", mddPct));

            // Kazanç / kayıp dengesi
            if (ctx.getAverageGain() != null && ctx.getAverageLoss() != null) {
                if (ctx.getAverageGain().doubleValue() > ctx.getAverageLoss().doubleValue()) {
                    evaluation.append("- kazançlar kayıplardan güçlü\n");
                } else {
                    evaluation.append("- kayıplar kazançlardan baskın\n");
                }
            }

            // Yatırımcıya öneri (Sharpe + MDD + Volatilite)
            if (sharpe >= 2.0 && mddPct > -10 && volatility * 100 <= 15) {
                evaluation.append("- Düşük riskli ve yüksek getirili, dengeli portföylerde tercih edilebilir\n");
            } else if (sharpe >= 1.5 && mddPct > -20) {
                evaluation.append("- Yüksek getiri potansiyeli var, fakat volatiliteye dikkat edilmeli\n");
            } else {
                evaluation.append("- Yüksek dalgalanma ve risk mevcut, yalnızca yüksek risk toleransı olan yatırımcılar için\n");
            }

            ctx.setEvaluationSummary(evaluation.toString());
        }

        return ctxList;
    }

    private double safeDouble(BigDecimal value) {
        return value != null ? value.doubleValue() : 0.0;
    }
}
