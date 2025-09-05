package org.seed.fund.report.calculator;

import org.seed.fund.report.model.ReportContext;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.function.Function;

@Component
public class FundEvaluationCalculator implements Function<ReportContext, ReportContext> {

    @Override
    public ReportContext apply(ReportContext ctx) {
        StringBuilder summary = new StringBuilder();
        summary.append(ctx.getMetaData().getCode()).append(": ");

        // Tüm metrikleri güvenli şekilde al ve gerekli yüzdelik dönüşümler
        BigDecimal sharpe = safe(ctx.getSharpeRatio());
        BigDecimal sortino = safe(ctx.getSortinoRatio()).multiply(BigDecimal.valueOf(100)); // yüzde
        BigDecimal priceChange = safe(ctx.getPriceChange()).multiply(BigDecimal.valueOf(100));
        BigDecimal stdev = safe(ctx.getStandardDeviation()).multiply(BigDecimal.valueOf(100));
        BigDecimal gain = safe(ctx.getAverageGain()).multiply(BigDecimal.valueOf(100));
        BigDecimal loss = safe(ctx.getAverageLoss()).multiply(BigDecimal.valueOf(100));
        BigDecimal maxDrawdown = safe(ctx.getMaxDrawdown()).multiply(BigDecimal.valueOf(100));

        // Dinamik risk skoru: MDD + volatilite ağırlıklı
        BigDecimal riskScore = maxDrawdown.abs().multiply(BigDecimal.valueOf(0.7))
                .add(stdev.multiply(BigDecimal.valueOf(0.3)));

        // Yorumlar
        summary.append(sharpeComment(sharpe));
        summary.append(sortinoComment(sortino));
        summary.append(priceChangeComment(priceChange));
        summary.append(volatilityComment(stdev));
        summary.append(riskComment(riskScore));
        summary.append(String.format("dönem içinde maksimum %.2f%% kayıp, ", maxDrawdown));
        summary.append(gainLossBalanceComment(gain, loss));

        ctx.setEvaluationSummary(summary.toString());
        return ctx;
    }

    private BigDecimal safe(BigDecimal value) {
        return value != null ? value : BigDecimal.ZERO;
    }

    // Sharpe yorumları (oran)
    private String sharpeComment(BigDecimal sharpe) {
        if (sharpe.compareTo(BigDecimal.valueOf(2.0)) >= 0) return "yüksek risk-düzeltilmiş getiri, ";
        if (sharpe.compareTo(BigDecimal.valueOf(1.0)) >= 0) return "orta seviyede risk-düzeltilmiş getiri, ";
        return "düşük risk-düzeltilmiş getiri, ";
    }

    // Sortino yorumları (yüzde çarpılmış)
    private String sortinoComment(BigDecimal sortino) {
        if (sortino.compareTo(BigDecimal.valueOf(200.0)) >= 0) return "yüksek downside risk-düzeltilmiş getiri, ";
        if (sortino.compareTo(BigDecimal.valueOf(100.0)) >= 0) return "orta seviyede downside risk-düzeltilmiş getiri, ";
        return "düşük downside risk-düzeltilmiş getiri, ";
    }

    private String priceChangeComment(BigDecimal priceChange) {
        if (priceChange.compareTo(BigDecimal.valueOf(15.0)) >= 0) return "çok güçlü getiri, ";
        if (priceChange.compareTo(BigDecimal.valueOf(5.0)) >= 0) return "güçlü getiri, ";
        if (priceChange.compareTo(BigDecimal.ZERO) > 0) return "ılımlı getiri, ";
        return "kayıp, ";
    }

    private String volatilityComment(BigDecimal stdev) {
        if (stdev.compareTo(BigDecimal.valueOf(25.0)) >= 0) return "yüksek volatilite, ";
        if (stdev.compareTo(BigDecimal.valueOf(15.0)) >= 0) return "orta volatilite, ";
        return "düşük volatilite, ";
    }

    private String riskComment(BigDecimal riskScore) {
        if (riskScore.compareTo(BigDecimal.valueOf(25.0)) >= 0) return "yüksek risk, ";
        if (riskScore.compareTo(BigDecimal.valueOf(15.0)) >= 0) return "orta düzeyde risk, ";
        return "düşük risk, ";
    }

    private String gainLossBalanceComment(BigDecimal gain, BigDecimal loss) {
        if (gain.compareTo(loss) > 0) return "kazançlar kayıplardan güçlü.";
        if (loss.compareTo(gain) > 0) return "kayıplar kazançlardan güçlü.";
        return "kazançlar ve kayıplar dengeli.";
    }
}
