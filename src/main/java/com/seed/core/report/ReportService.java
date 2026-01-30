package com.seed.core.report;

import com.seed.core.AnalysisContext;
import com.seed.core.BatchDecisionAssetAnalyzer;
import com.seed.core.BatchSnapshotAssetAnalyzer;
import com.seed.core.model.HistoricalData;
import com.seed.core.model.MetaData;

import java.time.LocalDate;
import java.util.List;

public class ReportService<M extends MetaData, H extends HistoricalData> {
    private final BatchSnapshotAssetAnalyzer<M, H> snapshotAnalyzer;
    private final BatchDecisionAssetAnalyzer<M, H> decisionAnalyzer;

    public ReportService(BatchSnapshotAssetAnalyzer<M, H> snapshotAnalyzer,
                         BatchDecisionAssetAnalyzer<M, H> decisionAnalyzer) {
        this.snapshotAnalyzer = snapshotAnalyzer;
        this.decisionAnalyzer = decisionAnalyzer;
    }

    public List<List<AnalysisContext<M, H>>> generateReportContext(String[] codes, LocalDate endDate) {
        return List.of(
                snapshotAnalyzer.analyze(codes, endDate.minusMonths(1), endDate),
                snapshotAnalyzer.analyze(codes, endDate.minusMonths(3), endDate),
                snapshotAnalyzer.analyze(codes, endDate.minusMonths(6), endDate),
                snapshotAnalyzer.analyze(codes, endDate.minusMonths(12), endDate)
        );
    }

    public BatchReportContext generateDecisionReportContext(String[] codes, LocalDate endDate) {
        List<ReportContext> reportContexts = decisionAnalyzer.analyze(codes, endDate.minusMonths(12), endDate)
                .stream()
                .map(ReportContextBuilder::build)
                .toList();
        return new BatchReportContext(endDate, reportContexts);
    }
}
