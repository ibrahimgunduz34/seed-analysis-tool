package com.seed.core;

import com.seed.core.calculator.Performance;
import com.seed.core.exception.NoResourceFoundException;
import com.seed.core.model.HistoricalData;
import com.seed.core.model.MetaData;
import com.seed.core.storage.MetaDataStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class BatchAssetAnalyzer<M extends MetaData, H extends HistoricalData> {
    private final AssetAnalyzer<M, H> analyzer;
    private final Performance<M, H> performanceRatingCalculator;
    private final MetaDataStorage<M> metaDataStorage;

    public BatchAssetAnalyzer(AssetAnalyzer<M, H> analyzer,
                              Performance<M, H> performanceRatingCalculator,
                              MetaDataStorage<M> metaDataStorage) {
        this.analyzer = analyzer;
        this.performanceRatingCalculator = performanceRatingCalculator;
        this.metaDataStorage = metaDataStorage;
    }

    public List<AnalysisContext<M, H>> analyze(String[] codes, LocalDate startDate, LocalDate endDate) {
        List<M> validatedCodes = new ArrayList<>();
        Stream.of(codes)
                .forEach(code -> {
                    Optional<M> metaData = metaDataStorage.getMetaDataByCode(code);
                    metaData.ifPresentOrElse(validatedCodes::add, () -> {
                        throw new NoResourceFoundException("No resource found for code " + code);
                    });
                });

        List<AnalysisContext<M, H>> contexts = validatedCodes.stream()
                .map(metaData -> analyzer.analyze(metaData, startDate, endDate))
                .toList();

        return performanceRatingCalculator.calculate(contexts);
    }
}
