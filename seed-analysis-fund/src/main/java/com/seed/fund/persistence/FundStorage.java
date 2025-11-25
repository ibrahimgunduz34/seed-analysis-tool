package com.seed.fund.persistence;

import com.seed.core.exception.NoResourceFoundException;
import com.seed.core.exception.PersistenceAccessException;
import com.seed.core.persistence.AssetStorage;
import com.seed.fund.model.Fund;
import com.seed.fund.persistence.entity.FundHistoricalDataEntity;
import com.seed.fund.persistence.entity.FundMetaDataEntity;
import com.seed.fund.persistence.mapper.FundMapper;
import com.seed.fund.persistence.repository.FundHistoricalDataRepository;
import com.seed.fund.persistence.repository.FundMetaDataRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class FundStorage implements AssetStorage<Fund> {
    private final FundMetaDataRepository metaDataRepository;
    private final FundHistoricalDataRepository historicalDataRepository;
    private final FundMapper fundMapper;

    public FundStorage(FundMetaDataRepository metaDataRepository,
                       FundHistoricalDataRepository historicalDataRepository,
                       FundMapper fundMapper) {
        this.metaDataRepository = metaDataRepository;
        this.historicalDataRepository = historicalDataRepository;
        this.fundMapper = fundMapper;
    }

    @Override
    public Fund getAssetByCode(String code) {
        try {
            FundMetaDataEntity metaDataEntity = metaDataRepository.findByCode(code)
                    .orElseThrow(() -> new NoResourceFoundException("No fund found with the specified code: " + code));
            return fundMapper.mapToFundByMetaDataEntity(metaDataEntity);
        } catch (DataAccessException exception) {
            throw new PersistenceAccessException("Failed to retrieve fund with the specified code: " + code, exception);
        }
    }

    @Override
    public List<Fund> getList() {
        try {
            List<FundMetaDataEntity> metaDataList = metaDataRepository.findAll();
            return fundMapper.mapToFundListByFundMetaDataEntityList(metaDataList);
        } catch (DataAccessException exception) {
            throw new PersistenceAccessException("Failed to retrieve fund meta-data list", exception);
        }
    }

    @Override
    public List<Fund> getListByValueDate(LocalDate valueDate) {
        try {
            List<FundHistoricalDataEntity> historicalDataList = historicalDataRepository.findAllByValueDate(valueDate);
            return fundMapper.mapToFundListByFundHistoricalDataEntityList(historicalDataList);
        } catch (DataAccessException exception) {
            throw new PersistenceAccessException("Failed to retrieve fund historical list", exception);
        }
    }

    @Override
    public List<Fund> getListByDateRange(String code, LocalDate beginDate, LocalDate endDate) {
        try {
            List<FundHistoricalDataEntity> historicalDataList = historicalDataRepository.findAllByDateRange(code, beginDate, endDate);
            return fundMapper.mapToFundListByHistoricalDataEntityGroupingByMetaData(historicalDataList);
        } catch (DataAccessException exception) {
            throw new PersistenceAccessException("Failed to retrieve fund historical list by date range", exception);
        }
    }

    @Override
    public void saveMetaDataList(List<Fund> asset) {

    }

    @Override
    public void saveHistoricalDataList(Fund asset) {

    }
}
