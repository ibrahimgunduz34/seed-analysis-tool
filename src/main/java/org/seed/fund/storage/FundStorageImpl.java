package org.seed.fund.storage;

import jakarta.transaction.Transactional;
import org.seed.exception.NotFoundException;
import org.seed.fund.mapper.FundMapper;
import org.seed.fund.mapper.FundHistoricalDataMapper;
import org.seed.fund.mapper.FundMetaDataMapper;
import org.seed.fund.model.Fund;
import org.seed.fund.model.FundHistoricalData;
import org.seed.fund.model.FundMetaData;
import org.seed.fund.storage.jpa.entity.FundHistoricalDataEntity;
import org.seed.fund.storage.jpa.entity.FundMetaDataEntity;
import org.seed.fund.storage.jpa.repository.FundHistoricalDataRepository;
import org.seed.fund.storage.jpa.repository.FundMetaDataRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class FundStorageImpl implements FundStorage {
    private final FundMetaDataRepository fundMetaDataRepository;
    private final FundHistoricalDataRepository fundHistoricalDataRepository;
    private final FundMetaDataMapper fundMetaDataMapper;
    private final FundHistoricalDataMapper fundHistoricalDataMapper;
    private final FundMapper fundMapper;

    public FundStorageImpl(FundMetaDataRepository fundMetaDataRepository, FundHistoricalDataRepository fundHistoricalDataRepository, FundMetaDataMapper fundMetaDataMapper, FundHistoricalDataMapper fundHistoricalDataMapper, FundMapper fundMapper) {
        this.fundMetaDataRepository = fundMetaDataRepository;
        this.fundHistoricalDataRepository = fundHistoricalDataRepository;
        this.fundMetaDataMapper = fundMetaDataMapper;
        this.fundHistoricalDataMapper = fundHistoricalDataMapper;
        this.fundMapper = fundMapper;
    }

    @Override
    public Fund getFundByCode(String code) {
        FundMetaDataEntity fundMetaDataEntity = fundMetaDataRepository
                .findOneByCode(code)
                .orElseThrow(() -> new NotFoundException("No fund found with the specified code: " + code));

        FundMetaData fundMetaData = fundMetaDataMapper.toModel(fundMetaDataEntity);

        FundHistoricalData fundHistoricalData = fundHistoricalDataRepository.findOneByMetaDataCode(code, PageRequest.of(0, 1))
                .stream().findFirst()
                .map(fundHistoricalDataMapper::toModel)
                .orElse(null);

        return Fund.create(fundMetaData, fundHistoricalData);
    }

    @Override
    public List<FundMetaData> getMetaDataList() {
        return fundMetaDataRepository.findAll()
                .stream()
                .map(fundMetaDataMapper::toModel)
                .toList();
    }

    @Override
    public List<Fund> getFundsByValueDate(LocalDate valueDate) {
        return fundHistoricalDataRepository.findAllByValueDate(valueDate)
                .stream()
                .map(fundMapper::toModel)
                .toList();
    }

    @Override
    public List<FundHistoricalData> getHistoricalDataByDateRange(String code, LocalDate beginDate, LocalDate endDate) {
        return fundHistoricalDataRepository.findAllByDateRange(code, beginDate, endDate)
                .stream()
                .map(fundHistoricalDataMapper::toModel)
                .toList();
    }

    @Transactional
    @Override
    public void saveAll(List<FundMetaData> fundMetaDataList) {
        List<FundMetaDataEntity> entities = fundMetaDataList.stream()
                .map(fundMetaDataMapper::toEntity)
                .toList();

        fundMetaDataRepository.saveAll(entities);
    }

    @Override
    public List<FundHistoricalData> saveAll(Map<String, List<FundHistoricalData>> models) {
        Map<String, FundMetaData> metaDataMap = getMetaDataList().stream()
                .collect(Collectors.toMap(FundMetaData::getCode, Function.identity()));

        List<FundHistoricalDataEntity> entities = models.entrySet().stream()
                .flatMap(
                        entry -> entry.getValue().stream()
                                .map(item -> fundHistoricalDataMapper.toEntity(metaDataMap.get(entry.getKey()), item))
                )
                .toList();


        return fundHistoricalDataMapper.toModel(fundHistoricalDataRepository.saveAll(entities));
    }
}
