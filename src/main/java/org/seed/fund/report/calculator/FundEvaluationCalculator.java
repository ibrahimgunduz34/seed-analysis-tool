package org.seed.fund.report.calculator;

import org.seed.fund.report.model.ReportContext;
import org.seed.util.BigDecimalMath;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Function;

public class FundEvaluationCalculator implements Function<List<ReportContext>, List<ReportContext>> {

    @Override
    public List<ReportContext> apply(List<ReportContext> ctxList) {
        if (ctxList == null || ctxList.isEmpty()) return ctxList;

        BigDecimal maxVolatility = ctxList.stream().map(ReportContext::getStandardDeviation)
                .max(BigDecimal::compareTo).orElse(BigDecimal.ZERO);
        BigDecimal minMDD = ctxList.stream().map(ReportContext::getMaxDrawdown)
                .min(BigDecimal::compareTo).orElse(BigDecimal.ZERO); // negatif değer
        BigDecimal maxSharpe = ctxList.stream().map(ReportContext::getSharpeRatio)
                .max(BigDecimal::compareTo).orElse(BigDecimal.ZERO);
        BigDecimal maxSortino = ctxList.stream().map(ReportContext::getSortinoRatio)
                .max(BigDecimal::compareTo).orElse(BigDecimal.ZERO);

        for (ReportContext ctx : ctxList) {
            StringBuilder evaluation = new StringBuilder();
            evaluation.append(ctx.getMetaData().getCode()).append(":\n");

            // 1) Risk ve volatilite
            BigDecimal vol = ctx.getStandardDeviation();
            BigDecimal mdd = ctx.getMaxDrawdown();
            String riskLevel = vol.compareTo(maxVolatility.multiply(BigDecimal.valueOf(0.66))) > 0 ? "Yüksek Riskli" :
                    vol.compareTo(maxVolatility.multiply(BigDecimal.valueOf(0.33))) > 0 ? "Orta Riskli" : "Düşük Riskli";
            boolean highDrawdown = mdd.abs().compareTo(BigDecimal.valueOf(0.05)) > 0;
            evaluation.append("- Risk Değerlendirmesi: ").append(riskLevel);
            if(highDrawdown) evaluation.append(", Dikkat: Maksimum kayıp yüksek (").append(BigDecimalMath.convertToPercentage(mdd)).append("%)");
            evaluation.append("\n");

            // 2) Getiri / kayıp
            BigDecimal avgGain = ctx.getAverageGain() != null ? ctx.getAverageGain() : BigDecimal.ZERO;
            BigDecimal avgLoss = ctx.getAverageLoss() != null ? ctx.getAverageLoss() : BigDecimal.ZERO;
            double posWeight = ctx.getWeightOfPositiveDays();
            String gainLossComment;
            if(avgGain.compareTo(avgLoss) > 0 && posWeight > 0.6) gainLossComment = "Getiri güçlü ve istikrarlı";
            else if(avgGain.compareTo(avgLoss) > 0) gainLossComment = "Getiri güçlü ancak dalgalı";
            else gainLossComment = "Kaybın getiriye oranı yüksek";
            evaluation.append("- Getiri/Kayıp Dengesi: ").append(gainLossComment)
                    .append(" (Ortalama Kazanç: ").append(BigDecimalMath.convertToPercentage(avgGain))
                    .append("%, Ortalama Kayıp: ").append(BigDecimalMath.convertToPercentage(avgLoss)).append("%)")
                    .append("\n");

            // 3) Sharpe ve Sortino
            BigDecimal sharpe = ctx.getSharpeRatio() != null ? ctx.getSharpeRatio() : BigDecimal.ZERO;
            BigDecimal sortino = ctx.getSortinoRatio() != null ? ctx.getSortinoRatio() : BigDecimal.ZERO;
            String riskAdjusted;
            if(sharpe.subtract(sortino).abs().compareTo(BigDecimal.valueOf(1.0)) > 0)
                riskAdjusted = "Negatif volatilite baskın, dalgalı performans";
            else riskAdjusted = "Risk-düzeltilmiş performans dengeli";
            evaluation.append("- Risk Düzeltilmiş Performans: Sharpe ").append(sharpe)
                    .append(", Sortino ").append(sortino)
                    .append(" → ").append(riskAdjusted).append("\n");

            // 4) Gün bazlı performans
            evaluation.append("- Gün Bazlı Performans: Pozitif Gün Ağırlığı ")
                    .append(String.format("%.0f%%", posWeight*100))
                    .append(", Negatif Gün Ağırlığı ")
                    .append(String.format("%.0f%%", ctx.getWeightOfNegativeDays()*100)).append("\n");

            // 5) Fonlar arası kıyaslama
            if(vol.compareTo(maxVolatility.multiply(BigDecimal.valueOf(0.8))) > 0)
                evaluation.append("- Diğer Fonlarla Karşılaştırma: Liste içindeki en riskli fonlardan biri.\n");
            else if(sharpe.compareTo(maxSharpe.multiply(BigDecimal.valueOf(0.8))) < 0)
                evaluation.append("- Diğer Fonlarla Karşılaştırma: Sharpe oranı diğer fonlara göre düşük.\n");
            else
                evaluation.append("- Diğer Fonlarla Karşılaştırma: Ortalama veya iyi performans gösteren fon.\n");

            ctx.setEvaluationSummary(evaluation.toString());
        }

        return ctxList;
    }

}
