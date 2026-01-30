package com.seed.core;

import com.seed.core.calculator.Performance;
import com.seed.core.model.HistoricalData;
import com.seed.core.model.MetaData;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

public class BatchSnapshotAssetAnalyzer<M extends MetaData, H extends HistoricalData> {

    private final AssetAnalyzer<M, H> analyzer;
    private final Performance<M, H> performanceRatingCalculator;
    private final AssetValidator<M> assetValidator;

    public BatchSnapshotAssetAnalyzer(
            AssetAnalyzer<M, H> analyzer,
            Performance<M, H> performanceRatingCalculator,
            AssetValidator<M> assetValidator
    ) {
        this.analyzer = analyzer;
        this.performanceRatingCalculator = performanceRatingCalculator;
        this.assetValidator = assetValidator;
    }

    public List<AnalysisContext<M, H>> analyze(
            String[] codes,
            LocalDate startDate,
            LocalDate endDate
    ) {
        assetValidator.validate(codes);

        List<AnalysisContext<M, H>> contexts =
                Stream.of(codes)
                        .parallel() // ⚡ performans
                        .map(code -> analyzer.analyze(code, startDate, endDate))
                        .toList();

        return performanceRatingCalculator.calculate(contexts);
    }
}
