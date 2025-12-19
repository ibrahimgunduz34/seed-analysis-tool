package com.seed.core;

import com.seed.core.model.HistoricalData;
import com.seed.core.model.MetaData;
import com.seed.core.model.ResultKey;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.seed.core.calculator.GainLoss.AVERAGE_GAIN;
import static com.seed.core.calculator.GainLoss.AVERAGE_LOSS;
import static com.seed.core.calculator.Mdd.MDD;
import static com.seed.core.calculator.Mean.MEAN;
import static com.seed.core.calculator.Performance.PERFORMANCE_RATING;
import static com.seed.core.calculator.PeriodPriceChange.PERIOD_PRICE_CHANGE;
import static com.seed.core.calculator.PositiveNegativeDays.WEIGHT_OF_NEGATIVE_DAYS;
import static com.seed.core.calculator.PositiveNegativeDays.WEIGHT_OF_POSITIVE_DAYS;
import static com.seed.core.calculator.SharpeRatio.SHARPE_RATIO;
import static com.seed.core.calculator.Sortino.SORTINO;
import static com.seed.core.calculator.StDev.ST_DEV;

public class AnalysisContext<M extends MetaData, H extends HistoricalData> {
    private final HashMap<ResultKey<?>, Object> results = new HashMap<>();
    private final M metaData;
    private final List<H> historicalData;
    private final LocalDate startDate;
    private final LocalDate endDate;

    public AnalysisContext(M metaData, List<H> historicalData, LocalDate startDate, LocalDate endDate) {
        this.metaData = metaData;
        this.historicalData = historicalData;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @SuppressWarnings("unchecked")
    public <K> Optional<K> get(ResultKey<K> key) {
        return Optional.ofNullable((K) results.get(key));
    }

    public <K> boolean putIfAbsent(ResultKey<K> key, K value) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(value);
        if (!key.type().isInstance(value)) {
            throw new IllegalArgumentException("Wrong type");
        }

        return results.putIfAbsent(key, value) == null;
    }

    public M getMetaData() {
        return metaData;
    }

    public List<H> getHistoricalData() {
        return historicalData;
    }

    public AnalysisStatistics getStatistics() {
        return new AnalysisStatistics(
                get(PERIOD_PRICE_CHANGE).orElse(BigDecimal.valueOf(0)),
                get(MEAN).orElse(BigDecimal.valueOf(0)),
                get(WEIGHT_OF_POSITIVE_DAYS).orElse(0.0),
                get(WEIGHT_OF_NEGATIVE_DAYS).orElse(0.0),
                get(AVERAGE_GAIN).orElse(BigDecimal.valueOf(0)),
                get(AVERAGE_LOSS).orElse(BigDecimal.valueOf(0)),
                get(MDD).orElse(BigDecimal.valueOf(0)),
                get(ST_DEV).orElse(BigDecimal.valueOf(0)),
                get(SHARPE_RATIO).orElse(BigDecimal.valueOf(0)),
                get(SORTINO).orElse(BigDecimal.valueOf(0)),
                get(PERFORMANCE_RATING).orElse(0)
        );
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }
}
