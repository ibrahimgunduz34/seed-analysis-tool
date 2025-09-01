package org.seed.fund.report.model;

import lombok.Getter;
import lombok.Setter;
import org.seed.fund.model.HistoricalData;
import org.seed.fund.model.MetaData;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class ReportContext {
    private final MetaData metaData;
    private final List<HistoricalData> historicalDataList;

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

    public ReportContext(MetaData metaData, List<HistoricalData> historicalDataList) {
        this.metaData = metaData;
        this.historicalDataList = historicalDataList;
    }
}
