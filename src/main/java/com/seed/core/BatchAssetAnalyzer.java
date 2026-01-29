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
    private final AssetValidator<M> assetValidator;

    public BatchAssetAnalyzer(AssetAnalyzer<M, H> analyzer,
                              Performance<M, H> performanceRatingCalculator,
                              AssetValidator<M> assetValidator) {
        this.analyzer = analyzer;
        this.performanceRatingCalculator = performanceRatingCalculator;
        this.assetValidator = assetValidator;
    }

    public List<AnalysisContext<M, H>> analyze(String[] codes, LocalDate startDate, LocalDate endDate) {
        assetValidator.validate(codes);

        List<AnalysisContext<M, H>> contexts = Stream.of(codes)
                .map(metaData -> analyzer.analyze(metaData, startDate, endDate))
                .toList();

        return performanceRatingCalculator.calculate(contexts);
    }
}
