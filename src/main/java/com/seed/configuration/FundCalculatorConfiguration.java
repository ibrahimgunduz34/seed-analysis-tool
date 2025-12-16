package com.seed.configuration;

import org.springframework.context.annotation.Configuration;
import com.seed.fund.model.FundHistoricalData;
import com.seed.fund.model.FundMetaData;

@Configuration
public class FundCalculatorConfiguration extends AbstractCalculatorConfiguration<FundMetaData, FundHistoricalData> {
}
