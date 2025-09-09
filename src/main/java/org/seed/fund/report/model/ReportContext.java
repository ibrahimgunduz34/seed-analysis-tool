package org.seed.fund.report.model;

import lombok.Getter;
import lombok.Setter;
import org.seed.fund.model.FundHistoricalData;
import org.seed.fund.model.FundMetaData;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class ReportContext {
    private final FundMetaData fundMetaData;
    private final List<FundHistoricalData> fundHistoricalDataList;
    private final LocalDate beginDate;
    private final LocalDate endDate;

    private List<BigDecimal> dailyChanges;
    private int numberOfDays;

    private int numberOfPositiveDays;
    private int numberOfNegativeDays;
    private double weightOfPositiveDays;
    private double weightOfNegativeDays;

    private BigDecimal standardDeviation;
    private BigDecimal mean;

    private BigDecimal priceChange;
    private BigDecimal averageGain;
    private BigDecimal averageLoss;
    private BigDecimal sharpeRatio;
    private String evaluationSummary;
    private BigDecimal maxDrawdown;
    private BigDecimal sortinoRatio;

    public ReportContext(FundMetaData fundMetaData, List<FundHistoricalData> fundHistoricalDataList, LocalDate beginDate, LocalDate endDate) {
        this.fundMetaData = fundMetaData;
        this.fundHistoricalDataList = fundHistoricalDataList;
        this.beginDate = beginDate;
        this.endDate = endDate;
    }
}
