package com.seed.core;

import com.seed.core.model.HistoricalData;
import com.seed.core.model.MetaData;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

public class BatchDecisionAssetAnalyzer<M extends MetaData, H extends HistoricalData> {

    private final DecisionAssetAnalyzer<M, H> analyzer;
    private final AssetValidator<M> assetValidator;

    public BatchDecisionAssetAnalyzer(
            DecisionAssetAnalyzer<M, H> analyzer,
            AssetValidator<M> assetValidator
    ) {
        this.analyzer = analyzer;
        this.assetValidator = assetValidator;
    }

    public List<AnalysisContext<M, H>> analyze(
            String[] codes,
            LocalDate startDate,
            LocalDate endDate
    ) {
        assetValidator.validate(codes);

        return Stream.of(codes)
                .parallel()
                .map(code -> analyzer.analyze(code, startDate, endDate))
                .toList();
    }
}
