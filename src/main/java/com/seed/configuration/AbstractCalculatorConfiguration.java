package com.seed.configuration;

import com.seed.core.BatchAssetAnalyzer;
import com.seed.core.calculator.Performance;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.seed.core.AssetAnalyzer;
import com.seed.core.CalculatorOrchestrator;
import com.seed.core.calculator.DailyPriceChange;
import com.seed.core.calculator.GainLoss;
import com.seed.core.calculator.Mdd;
import com.seed.core.calculator.Mean;
import com.seed.core.calculator.PeriodPriceChange;
import com.seed.core.calculator.PositiveNegativeDays;
import com.seed.core.calculator.SharpeRatio;
import com.seed.core.calculator.Sortino;
import com.seed.core.calculator.StDev;
import com.seed.core.model.HistoricalData;
import com.seed.core.model.MetaData;
import com.seed.core.storage.HistoricalDataStorage;
import com.seed.core.storage.MetaDataStorage;

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

    @Bean
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

    @Bean
    public Performance<M, H> performanceBatchCalculator(ReportConfiguration configuration) {
        return new Performance<>(configuration);
    }

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Bean
    public AssetAnalyzer<M, H> assetAnalyzer(CalculatorOrchestrator<H> calculatorOrchestrator,
                                             MetaDataStorage<M> metaDataStorage,
                                             HistoricalDataStorage<M, H> historicalData) {
        return new AssetAnalyzer<>(
                calculatorOrchestrator,
                metaDataStorage,
                historicalData
        );
    }

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Bean
    public BatchAssetAnalyzer<M, H> batchAssetAnalyzer(AssetAnalyzer<M, H> analyzer,
                                                       Performance<M, H> performanceCalculator,
                                                       MetaDataStorage<M> metaDataStorage) {
        return new BatchAssetAnalyzer<>(analyzer, performanceCalculator, metaDataStorage);
    }
}
