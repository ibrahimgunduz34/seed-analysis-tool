package org.seed.fund.report;

import org.seed.fund.storage.FundStorage;
import org.springframework.stereotype.Component;

@Component
public class StrategyReportExporter {
    private final FundStorage fundStorage;

    public StrategyReportExporter(FundStorage fundStorage) {
        this.fundStorage = fundStorage;
    }


}
