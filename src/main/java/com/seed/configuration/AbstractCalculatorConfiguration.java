package com.seed.configuration;

import com.seed.core.BatchDecisionAssetAnalyzer;
import com.seed.core.DecisionAssetAnalyzer;
import com.seed.core.AssetValidator;
import com.seed.core.BatchSnapshotAssetAnalyzer;
import com.seed.core.CalculatorOrchestrator;
import com.seed.core.PipelineOrchestrator;
import com.seed.core.SnapshotAnalyzer;
import com.seed.core.calculator.base.DailyPriceChange;
import com.seed.core.calculator.GainLoss;
import com.seed.core.calculator.Mdd;
import com.seed.core.calculator.Mean;
import com.seed.core.calculator.Performance;
import com.seed.core.calculator.PeriodPriceChange;
import com.seed.core.calculator.PositiveNegativeDays;
import com.seed.core.calculator.SharpeRatio;
import com.seed.core.calculator.Sortino;
import com.seed.core.calculator.StDev;
import com.seed.core.calculator.decision.DecisionCalculator;
import com.seed.core.calculator.feature.Ema100Calculator;
import com.seed.core.calculator.feature.Ema20Calculator;
import com.seed.core.calculator.feature.Ema50Calculator;
import com.seed.core.calculator.feature.RollingMddCalculator;
import com.seed.core.calculator.feature.RollingReturnCalculator;
import com.seed.core.calculator.feature.RollingSharpeCalculator;
import com.seed.core.calculator.feature.RollingSortinoCalculator;
import com.seed.core.calculator.feature.RollingVolatilityCalculator;
import com.seed.core.calculator.score.FinalScoreCalculator;
import com.seed.core.calculator.score.MomentumScoreCalculator;
import com.seed.core.calculator.score.RiskScoreCalculator;
import com.seed.core.calculator.trend.TrendSignalCalculator;
import com.seed.core.model.HistoricalData;
import com.seed.core.model.MetaData;
import com.seed.core.report.ReportService;
import com.seed.core.storage.HistoricalDataStorage;
import com.seed.core.storage.MetaDataStorage;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public abstract class AbstractCalculatorConfiguration<M extends MetaData, H extends HistoricalData> {
    @Bean
    public DailyPriceChange<H> dailyPriceChange() {
        return new DailyPriceChange<>();
    }

    @Bean
    public GainLoss<H> gainLoss() {
        return new GainLoss<>();
    }

    @Bean
    public Mdd<H> mdd() {
        return new Mdd<>();
    }

    @Bean
    public Mean<H> mean() {
        return new Mean<>();
    }

    @Bean
    public PeriodPriceChange<H> periodPriceChange() {
        return new PeriodPriceChange<>();
    }

    @Bean
    public PositiveNegativeDays<H> positiveNegativeDays() {
        return new PositiveNegativeDays<>();
    }

    @Bean
    public SharpeRatio<H> sharpeRatio() {
        return new SharpeRatio<>();
    }

    @Bean
    public Sortino<H> sortino() {
        return new Sortino<>();
    }

    @Bean
    public StDev<H> stDev() {
        return new StDev<>();
    }

    public Performance<M, H> performance(ReportConfiguration reportConfiguration) {
        return new Performance<>(reportConfiguration);
    }

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Bean
    public AssetValidator<M> assetValidator(MetaDataStorage<M> metaDataStorage) {
        return new AssetValidator<>(metaDataStorage);
    }

    @Bean("featureStage")
    public CalculatorOrchestrator<H> featureStage() {
        return new CalculatorOrchestrator<>(List.of(
                new DailyPriceChange<>(),
                new RollingReturnCalculator<>(),
                new RollingVolatilityCalculator<>(),
                new RollingMddCalculator<>(),
                new RollingSharpeCalculator<>(),
                new RollingSortinoCalculator<>(),
                new Ema20Calculator<>(),
                new Ema50Calculator<>(),
                new Ema100Calculator<>()
        ));
    }

    @Bean("snapshotStage")
    public CalculatorOrchestrator<H> calculatorOrchestrator(
            DailyPriceChange<H> dailyPriceChange,
            PositiveNegativeDays<H> positiveNegativeDays,
            GainLoss<H> gainLoss,
            Mean<H> mean,
            Mdd<H> mdd,
            PeriodPriceChange<H> periodPriceChange,
            StDev<H> stDev,
            SharpeRatio<H> sharpeRatio,
            Sortino<H> sortino
    ) {
        return new CalculatorOrchestrator<>(List.of(
                dailyPriceChange,
                positiveNegativeDays,
                gainLoss,
                mean,
                mdd,
                periodPriceChange,
                stDev,
                sharpeRatio,
                sortino
        ));
    }


    @Bean("trendStage")
    public CalculatorOrchestrator<H> trendStage() {
        return new CalculatorOrchestrator<>(List.of(
                new TrendSignalCalculator<>()
        ));
    }

    @Bean("scoreStage")
    public CalculatorOrchestrator<H> scoreStage() {
        return new CalculatorOrchestrator<>(List.of(
                new MomentumScoreCalculator<>(),
                new RiskScoreCalculator<>(),
                new FinalScoreCalculator<>()
        ));
    }

    @Bean("decisionStage")
    public CalculatorOrchestrator<H> decisionStage(DecisionProperties properties) {
        return new CalculatorOrchestrator<>(List.of(
                new DecisionCalculator<>(properties)
        ));
    }

    @Bean
    public PipelineOrchestrator<H> pipelineOrchestrator(
            @Qualifier("featureStage")  CalculatorOrchestrator<H> featureStage,
            @Qualifier("trendStage")    CalculatorOrchestrator<H> trendStage,
            @Qualifier("scoreStage")    CalculatorOrchestrator<H> scoreStage,
            @Qualifier("decisionStage") CalculatorOrchestrator<H> decisionStage
    ) {
        return new PipelineOrchestrator<>(
                featureStage,
                trendStage,
                scoreStage,
                decisionStage
        );
    }

    @Bean
    public Performance<M, H> performanceBatchCalculator(ReportConfiguration configuration) {
        return new Performance<>(configuration);
    }

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Bean
    public DecisionAssetAnalyzer<M, H> assetAnalyzer(
            PipelineOrchestrator<H> pipelineOrchestrator,
            MetaDataStorage<M> metaDataStorage,
            HistoricalDataStorage<M, H> historicalData) {

        return new DecisionAssetAnalyzer<>(
                pipelineOrchestrator,
                metaDataStorage,
                historicalData
        );
    }

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Bean
    public SnapshotAnalyzer<M, H> snapshotAnalyzer(
            @Qualifier("snapshotStage") CalculatorOrchestrator<H> snapshotStage,
            MetaDataStorage<M> metaDataStorage,
            HistoricalDataStorage<M, H> historicalDataStorage) {

        return new SnapshotAnalyzer<>(
                snapshotStage,
                metaDataStorage,
                historicalDataStorage
        );
    }

    @Bean
    public BatchSnapshotAssetAnalyzer<M, H> batchAssetAnalyzer(SnapshotAnalyzer<M, H> analyzer,
                                                               Performance<M, H> performanceCalculator,
                                                               AssetValidator<M> assetValidator) {
        return new BatchSnapshotAssetAnalyzer<>(analyzer, performanceCalculator, assetValidator);
    }

    @Bean
    public ReportService<M, H> reportService(
            BatchSnapshotAssetAnalyzer<M, H> batchAssetAnalyzer,
            BatchDecisionAssetAnalyzer<M, H> batchDecisionAssetAnalyzer
    ) {
        return new ReportService<>(batchAssetAnalyzer, batchDecisionAssetAnalyzer);
    }

    @Bean
    public BatchDecisionAssetAnalyzer<M, H> batchDecisionAssetAnalyzer(
            DecisionAssetAnalyzer<M, H> analyzer,
            AssetValidator<M> assetValidator
    ) {
        return new BatchDecisionAssetAnalyzer<>(analyzer, assetValidator);
    }
}
