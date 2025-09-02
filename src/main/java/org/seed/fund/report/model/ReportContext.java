package org.seed.fund.report.model;

import lombok.Getter;
import lombok.Setter;
import org.seed.fund.model.HistoricalData;
import org.seed.fund.model.MetaData;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class ReportContext {
    private final MetaData metaData;
    private final List<HistoricalData> historicalDataList;
    private final LocalDate beginDate;
    private final LocalDate endDate;

    private List<BigDecimal> dailyChanges;
    private Long positiveIncome;
    private Long negativeIncome;
    private double positiveIncomePerformance;
    private double negativeIncomePerformance;
    private BigDecimal dailyStandardDeviation;
    private BigDecimal standardDeviation;
    private BigDecimal priceChange;
    private BigDecimal averageGain;
    private BigDecimal averageLoss;
    private BigDecimal sharpeRatio;
    private String evaluationSummary;
    ;private BigDecimal maxDrawdown;

    public ReportContext(MetaData metaData, List<HistoricalData> historicalDataList, LocalDate beginDate, LocalDate endDate) {
        this.metaData = metaData;
        this.historicalDataList = historicalDataList;
        this.beginDate = beginDate;
        this.endDate = endDate;
    }
}
