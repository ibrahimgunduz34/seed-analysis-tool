package com.seed.core;

import com.seed.core.exception.IllegalAccessException;
import com.seed.core.exception.NoResourceFoundException;
import com.seed.core.model.Candle;
import com.seed.core.model.HistoricalData;
import com.seed.core.model.MetaData;
import com.seed.core.storage.HistoricalDataStorage;
import com.seed.core.storage.MetaDataStorage;

import java.time.LocalDate;

public class AssetAnalyzer<M extends MetaData, C extends Candle> {
    private final CalculatorOrchestrator<C> calculatorOrchestrator;
    private final HistoricalDataStorage<M, C> historicalDataStorage;
    private final MetaDataStorage<M> metaDataStorage;

    public AssetAnalyzer(CalculatorOrchestrator<C> calculatorOrchestrator,
                         HistoricalDataStorage<M, C> historicalDataStorage,
                         MetaDataStorage<M> metaDataStorage) {
        this.calculatorOrchestrator = calculatorOrchestrator;
        this.historicalDataStorage = historicalDataStorage;
        this.metaDataStorage = metaDataStorage;
    }

    public AnalysisContext<M, C> analyze(String code, LocalDate startDate, LocalDate endDate) {
        M metaData = metaDataStorage.getMetaDataByCode(code)
                .orElseThrow(() -> new NoResourceFoundException("No asset found: " + code));

        if (endDate.isBefore(startDate)) {
            throw new IllegalAccessException("End date should be after start date");
        }

        HistoricalData<C> historicalData = historicalDataStorage.getHistoricalDataByDateRange(metaData, startDate, endDate);

        if (historicalData.candles().isEmpty()) {
            throw new NoResourceFoundException("No historical data found with the specified data range for: " + code);
        }

        AnalysisContext<M, C> ctx = new AnalysisContext<>(metaData, historicalData);
        calculatorOrchestrator.run(ctx);
        return ctx;
    }
}
