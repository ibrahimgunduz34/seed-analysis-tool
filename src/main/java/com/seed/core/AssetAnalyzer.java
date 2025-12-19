package com.seed.core;

import com.seed.core.exception.IllegalAccessException;
import com.seed.core.exception.NoResourceFoundException;
import com.seed.core.model.HistoricalData;
import com.seed.core.model.MetaData;
import com.seed.core.storage.HistoricalDataStorage;
import com.seed.core.storage.MetaDataStorage;

import java.time.LocalDate;
import java.util.List;

public class AssetAnalyzer<M extends MetaData, H extends HistoricalData> {
    private final CalculatorOrchestrator<H> calculatorOrchestrator;
    private final HistoricalDataStorage<M, H> historicalDataStorage;
    private final MetaDataStorage<M> metaDataStorage;

    public AssetAnalyzer(CalculatorOrchestrator<H> calculatorOrchestrator,
                         MetaDataStorage<M> metaDataStorage, HistoricalDataStorage<M, H> historicalDataStorage) {
        this.calculatorOrchestrator = calculatorOrchestrator;
        this.metaDataStorage = metaDataStorage;
        this.historicalDataStorage = historicalDataStorage;
    }

    public AnalysisContext<M, H> analyze(String code, LocalDate startDate, LocalDate endDate) {
        M metaData = metaDataStorage.getMetaDataByCode(code)
                .orElseThrow(() -> new NoResourceFoundException("No asset found: " + code));

        return analyze(metaData, startDate, endDate);
    }

    public AnalysisContext<M, H> analyze(M metaData, LocalDate startDate, LocalDate endDate) {
        if (endDate.isBefore(startDate)) {
            throw new IllegalAccessException("End date should be after start date");
        }

        List<H> historicalData = historicalDataStorage.getHistoricalDataByDateRange(metaData, startDate, endDate);

        if (historicalData.isEmpty()) {
            throw new NoResourceFoundException("No historical data found with the specified data range for: " + metaData.code());
        }

        AnalysisContext<M, H> ctx = new AnalysisContext<>(metaData, historicalData, startDate, endDate);
        calculatorOrchestrator.run(ctx);
        return ctx;
    }
}
