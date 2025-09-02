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

        // Veri
        double sharpe = ctx.getSharpeRatio() != null ? ctx.getSharpeRatio().doubleValue() : 0.0;
        double priceChange = ctx.getPriceChange() != null ? ctx.getPriceChange().doubleValue() : 0.0;
        double stdev = ctx.getStandardDeviation() != null ? ctx.getStandardDeviation().doubleValue() : 0.0;
        double gain = ctx.getAverageGain() != null ? ctx.getAverageGain().doubleValue() : 0.0;
        double loss = ctx.getAverageLoss() != null ? ctx.getAverageLoss().doubleValue() : 0.0;
        double positiveDays = ctx.getPositiveIncome() != null ? ctx.getPositiveIncome() : 0;
        int totalDays = ctx.getHistoricalDataList() != null ? ctx.getHistoricalDataList().size() : 1;


        // Gün sayısı
        long days = 1;
        if (ctx.getBeginDate() != null && ctx.getEndDate() != null) {
            days = java.time.temporal.ChronoUnit.DAYS.between(ctx.getBeginDate(), ctx.getEndDate());
            if (days == 0) days = 1;
        }

        // === Dinamik eşikler ===
        double periodFactor = 365.0 / days;

        // Fiyat değişimi eşikleri
        double priceStrong = 0.30 / periodFactor;
        double priceGood = 0.15 / periodFactor;
        double priceModerate = 0.05 / periodFactor;

        // Volatilite eşikleri
        double stdevHigh = 0.25 / Math.sqrt(periodFactor);
        double stdevMedium = 0.15 / Math.sqrt(periodFactor);

        // Sharpe eşikleri
        double sharpeHigh = 2.0 * Math.sqrt(days / 252.0);
        double sharpeMedium = 1.0 * Math.sqrt(days / 252.0);

        // === Sürekli artan / sabit getirili fon tespiti ===
        double positiveRatio = (double) positiveDays / totalDays;
        boolean stableGrowth = positiveRatio > 0.95 && stdev < 0.2;

        // === Sharpe yorumu veya sabit getiri ===
        if (stableGrowth) {
            summary.append("istikrarlı/sabit getirili, ");
        } else if (sharpe >= sharpeHigh) {
            summary.append("yüksek risk-düzeltilmiş getiri, ");
        } else if (sharpe >= sharpeMedium) {
            summary.append("orta seviyede risk-düzeltilmiş getiri, ");
        } else {
            summary.append("düşük risk-düzeltilmiş getiri, ");
        }

        // === Fiyat performansı ===
        if (priceChange >= priceStrong) summary.append("çok güçlü getiri, ");
        else if (priceChange >= priceGood) summary.append("güçlü getiri, ");
        else if (priceChange >= priceModerate) summary.append("ılımlı getiri, ");
        else if (priceChange > 0) summary.append("zayıf getiri, ");
        else summary.append("kayıp, ");

        // === Volatilite ===
        if (stdev >= stdevHigh) summary.append("yüksek volatilite, ");
        else if (stdev >= stdevMedium) summary.append("orta volatilite, ");
        else summary.append("düşük volatilite, ");

        // === Kazanç/kayıp dengesi ===
        if (gain > loss) summary.append("kazançlar kayıplardan güçlü.");
        else if (loss > gain) summary.append("kayıplar kazançlardan güçlü.");
        else summary.append("kazançlar ve kayıplar dengeli.");

        ctx.setEvaluationSummary(summary.toString());
        return ctx;
    }
}