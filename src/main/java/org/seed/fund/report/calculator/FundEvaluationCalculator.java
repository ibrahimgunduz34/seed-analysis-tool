package org.seed.fund.report.calculator;

import org.seed.fund.report.model.ReportContext;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class FundEvaluationCalculator implements Function<ReportContext, ReportContext> {

    @Override
    public ReportContext apply(ReportContext ctx) {
        StringBuilder summary = new StringBuilder();
        summary.append(ctx.getMetaData().getCode()).append(": ");

        // Ham veriler
        double sharpe = ctx.getSharpeRatio() != null ? ctx.getSharpeRatio().doubleValue() : 0.0;
        double priceChange = ctx.getPriceChange() != null ? ctx.getPriceChange().doubleValue() : 0.0;
        double stdev = ctx.getStandardDeviation() != null ? ctx.getStandardDeviation().doubleValue() : 0.0;
        double gain = ctx.getAverageGain() != null ? ctx.getAverageGain().doubleValue() : 0.0;
        double loss = ctx.getAverageLoss() != null ? ctx.getAverageLoss().doubleValue() : 0.0;
        double positiveDays = ctx.getPositiveIncome() != null ? ctx.getPositiveIncome() : 0;
        int totalDays = ctx.getHistoricalDataList() != null ? ctx.getHistoricalDataList().size() : 1;
        double maxDrawdown = ctx.getMaxDrawdown() != null ? ctx.getMaxDrawdown().doubleValue() : 0.0;

        // Dinamik risk skoru (MDD ve volatilite kombinasyonu)
        double riskScore = Math.abs(maxDrawdown) * 0.7 + stdev * 0.3; // ağırlıkları istediğin gibi ayarlayabilirsin

        // Sharpe yorumu
        if (sharpe >= 2.0) summary.append("yüksek risk-düzeltilmiş getiri, ");
        else if (sharpe >= 1.0) summary.append("orta seviyede risk-düzeltilmiş getiri, ");
        else summary.append("düşük risk-düzeltilmiş getiri, ");

        // Fiyat performansı
        if (priceChange >= 0.15) summary.append("çok güçlü getiri, ");
        else if (priceChange >= 0.05) summary.append("güçlü getiri, ");
        else if (priceChange > 0) summary.append("ılımlı getiri, ");
        else summary.append("kayıp, ");

        // Volatilite
        if (stdev >= 0.25) summary.append("yüksek volatilite, ");
        else if (stdev >= 0.15) summary.append("orta volatilite, ");
        else summary.append("düşük volatilite, ");

        // Risk skoru üzerinden MDD yorumu
        if (riskScore >= 0.25) summary.append("yüksek risk, ");
        else if (riskScore >= 0.15) summary.append("orta düzeyde risk, ");
        else summary.append("düşük risk, ");

        summary.append(String.format("dönem içinde maksimum %.2f%% kayıp, ", maxDrawdown * 100));

        // Kazanç/kayıp dengesi
        if (gain > loss) summary.append("kazançlar kayıplardan güçlü.");
        else if (loss > gain) summary.append("kayıplar kazançlardan güçlü.");
        else summary.append("kazançlar ve kayıplar dengeli.");

        ctx.setEvaluationSummary(summary.toString());
        return ctx;
    }
}
