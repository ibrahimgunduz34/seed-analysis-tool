package com.seed.core;

import com.seed.core.model.HistoricalData;

public class PipelineOrchestrator<C extends HistoricalData> {
    private final CalculatorOrchestrator<C> featureStage;
    private final CalculatorOrchestrator<C> trendStage;
    private final CalculatorOrchestrator<C> scoreStage;
    private final CalculatorOrchestrator<C> decisionStage;

    public PipelineOrchestrator(
            CalculatorOrchestrator<C> featureStage,
            CalculatorOrchestrator<C> trendStage,
            CalculatorOrchestrator<C> scoreStage,
            CalculatorOrchestrator<C> decisionStage
    ) {
        this.featureStage = featureStage;
        this.trendStage = trendStage;
        this.scoreStage = scoreStage;
        this.decisionStage = decisionStage;
    }

    public void run(AnalysisContext<?, C> ctx) {
        featureStage.run(ctx);
        trendStage.run(ctx);
        scoreStage.run(ctx);
        decisionStage.run(ctx);
    }
}
