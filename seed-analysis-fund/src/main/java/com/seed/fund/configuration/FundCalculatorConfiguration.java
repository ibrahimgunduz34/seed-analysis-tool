package com.seed.fund.configuration;

import com.seed.common.AbstractCalculatorConfiguration;
import com.seed.fund.model.FundHistoricalData;
import com.seed.fund.model.FundMetaData;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FundCalculatorConfiguration extends AbstractCalculatorConfiguration<FundMetaData, FundHistoricalData> {
}
