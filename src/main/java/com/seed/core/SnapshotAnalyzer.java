package com.seed.core;

import com.seed.core.exception.NoResourceFoundException;
import com.seed.core.model.HistoricalData;
import com.seed.core.model.MetaData;
import com.seed.core.storage.HistoricalDataStorage;
import com.seed.core.storage.MetaDataStorage;
import org.springframework.beans.factory.annotation.Qualifier;

import java.time.LocalDate;
import java.util.List;

public class SnapshotAnalyzer<M extends MetaData, H extends HistoricalData> {

    private final CalculatorOrchestrator<H> snapshotStage;
    private final HistoricalDataStorage<M, H> historicalDataStorage;
    private final MetaDataStorage<M> metaDataStorage;

    public SnapshotAnalyzer(
            @Qualifier("snapshotStage") CalculatorOrchestrator<H> snapshotStage,
            MetaDataStorage<M> metaDataStorage,
            HistoricalDataStorage<M, H> historicalDataStorage
    ) {
        this.snapshotStage = snapshotStage;
        this.metaDataStorage = metaDataStorage;
        this.historicalDataStorage = historicalDataStorage;
    }

    public AnalysisContext<M, H> analyze(
            String code,
            LocalDate startDate,
            LocalDate endDate
    ) {
        M metaData = metaDataStorage.getMetaDataByCode(code)
                .orElseThrow(() -> new NoResourceFoundException("No asset found: " + code));

        List<H> historicalData =
                historicalDataStorage.getHistoricalDataByDateRange(
                        metaData, startDate, endDate);

        AnalysisContext<M, H> ctx =
                new AnalysisContext<>(metaData, historicalData, startDate, endDate);

        snapshotStage.run(ctx);

        return ctx;
    }
}
