package com.seed.core;

import com.seed.core.exception.NoResourceFoundException;
import com.seed.core.model.HistoricalData;
import com.seed.core.model.MetaData;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
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
                .map(code -> safeAnalyze(code, startDate, endDate))
                .filter(Objects::nonNull)
                .toList();
    }

    private AnalysisContext<M, H> safeAnalyze(
            String code,
            LocalDate startDate,
            LocalDate endDate
    ) {
        try {
            return analyzer.analyze(code, startDate, endDate);
        } catch (NoResourceFoundException e) {
            return null;
        }
    }

}
