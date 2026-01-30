package com.seed.core.report;

import com.seed.core.AnalysisContext;
import com.seed.core.calculator.decision.DecisionCalculator;
import com.seed.core.model.HistoricalData;
import com.seed.core.model.MetaData;

import java.math.BigDecimal;

import static com.seed.core.calculator.decision.DecisionCalculator.CONFIDENCE;
import static com.seed.core.calculator.decision.DecisionCalculator.TRADE_SIGNAL;
import static com.seed.core.calculator.feature.RollingMddCalculator.MDD_6M;
import static com.seed.core.calculator.feature.RollingReturnCalculator.*;
import static com.seed.core.calculator.feature.RollingSharpeCalculator.SHARPE_3M;
import static com.seed.core.calculator.feature.RollingSortinoCalculator.SORTINO_3M;
import static com.seed.core.calculator.feature.RollingVolatilityCalculator.VOL_21D;
import static com.seed.core.calculator.score.FinalScoreCalculator.FINAL_SCORE;
import static com.seed.core.calculator.score.MomentumScoreCalculator.MOMENTUM_SCORE;
import static com.seed.core.calculator.score.RiskScoreCalculator.RISK_SCORE;
import static com.seed.core.calculator.trend.TrendSignalCalculator.TREND_LABEL;
import static com.seed.core.calculator.trend.TrendSignalCalculator.TREND_SIGNAL;

public class ReportContextBuilder {

    public static  <M extends MetaData, H extends HistoricalData> ReportContext build(AnalysisContext<M, H> ctx) {
        return new ReportContext(
                ctx.getMetaData().code(),
                ctx.getMetaData().name(),

                ctx.get(FINAL_SCORE).orElse(0),
                ctx.get(MOMENTUM_SCORE).orElse(0),
                ctx.get(RISK_SCORE).orElse(0),

                ctx.get(TREND_SIGNAL).orElse(0),
                ctx.get(TREND_LABEL).orElse(""),

                ctx.get(TRADE_SIGNAL).orElse(DecisionCalculator.Decision.HOLD),
                ctx.get(CONFIDENCE).orElse(0),

                ctx.get(RET_1M).orElse(BigDecimal.ZERO),
                ctx.get(RET_3M).orElse(BigDecimal.ZERO),
                ctx.get(RET_6M).orElse(BigDecimal.ZERO),
                ctx.get(RET_12M).orElse(BigDecimal.ZERO),

                ctx.get(VOL_21D).orElse(BigDecimal.ZERO),
                ctx.get(MDD_6M).orElse(BigDecimal.ZERO),
                ctx.get(SHARPE_3M).orElse(BigDecimal.ZERO),
                ctx.get(SORTINO_3M).orElse(BigDecimal.ZERO)
        );
    }
}
