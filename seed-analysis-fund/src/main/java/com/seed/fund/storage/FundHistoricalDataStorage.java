package com.seed.fund.storage;

import com.seed.core.model.HistoricalData;
import com.seed.core.storage.HistoricalDataStorage;
import com.seed.fund.model.FundCandle;
import com.seed.fund.model.FundMetaData;
import com.seed.fund.storage.entity.FundHistoricalDataEntity;
import com.seed.fund.storage.mapper.HistoricalDataMapper;
import com.seed.fund.storage.repository.FundHistoricalDataRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class FundHistoricalDataStorage implements HistoricalDataStorage<FundMetaData, FundCandle> {
    private final FundHistoricalDataRepository historicalDataRepository;
    private final HistoricalDataMapper historicalDataMapper;

    public FundHistoricalDataStorage(FundHistoricalDataRepository historicalDataRepository, HistoricalDataMapper historicalDataMapper) {
        this.historicalDataRepository = historicalDataRepository;
        this.historicalDataMapper = historicalDataMapper;
    }

    @Override
    public HistoricalData<FundCandle> getHistoricalDataByDateRange(FundMetaData metaData, LocalDate startDate, LocalDate endDate) {
        List<FundHistoricalDataEntity> historicalDataList = historicalDataRepository.findAllByDateRange(metaData.code(), startDate, endDate);
        return historicalDataMapper.toModel(historicalDataList);
    }
}
