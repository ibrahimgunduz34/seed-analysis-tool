package com.seed.common;

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
import com.seed.core.model.Candle;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
abstract public class AbstractCalculatorConfiguration<C extends Candle> {
    @Bean
    public DailyPriceChange<C> dailyPriceChange() {
        return new DailyPriceChange<>();
    }

    @Bean
    public GainLoss<C> gainLoss() {
        return new GainLoss<>();
    }

    @Bean
    public Mdd<C> mdd() {
        return new Mdd<>();
    }

    @Bean
    public Mean<C> mean() {
        return new Mean<>();
    }

    @Bean
    public PeriodPriceChange<C> periodPriceChange() {
        return new PeriodPriceChange<>();
    }

    @Bean
    public PositiveNegativeDays<C> positiveNegativeDays() {
        return new PositiveNegativeDays<>();
    }

    @Bean
    public SharpeRatio<C> sharpeRatio() {
        return new SharpeRatio<>();
    }

    @Bean
    public Sortino<C> sortino() {
        return new Sortino<>();
    }

    @Bean
    public StDev<C> stDev() {
        return new StDev<>();
    }

    @Bean
    public CalculatorOrchestrator<C> calculatorOrchestrator(
            DailyPriceChange<C> dailyPriceChange,
            PositiveNegativeDays<C> positiveNegativeDays,
            GainLoss<C> gainLoss,
            Mean<C> mean,
            Mdd<C> mdd,
            PeriodPriceChange<C> periodPriceChange,
            StDev<C> stDev,
            SharpeRatio<C> sharpeRatio,
            Sortino<C> sortino
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
}
