package com.seed.fund.mapper;

import com.seed.core.AnalysisContext;
import com.seed.fund.model.FundHistoricalData;
import com.seed.fund.model.FundMetaData;
import com.seed.fund.web.dto.AnalysisContextDto;
import com.seed.fund.web.dto.AnalysisStatisticsDto;
import org.springframework.stereotype.Component;

// TODO: Make it generic or fund specific

@Component
public class AnalysisContextMapper {
    private final MetaDataMapper metaDataMapper;

    public AnalysisContextMapper(MetaDataMapper metaDataMapper) {
        this.metaDataMapper = metaDataMapper;
    }

    public AnalysisContextDto toDto(AnalysisContext<FundMetaData, FundHistoricalData> ctx) {
        var statistics = ctx.getStatistics();
        return new AnalysisContextDto(
                metaDataMapper.toDto(ctx.getMetaData()),
                new AnalysisStatisticsDto(
                        statistics.priceChange(),
                        statistics.mean(),
                        statistics.weightOfPositiveDays(),
                        statistics.weightOfNegativeDays(),
                        statistics.avgGain(),
                        statistics.avgLoss(),
                        statistics.mdd(),
                        statistics.stDev(),
                        statistics.sharpeRatio(),
                        statistics.sortino(),
                        statistics.performanceRating()
                )
        );
    }
}
