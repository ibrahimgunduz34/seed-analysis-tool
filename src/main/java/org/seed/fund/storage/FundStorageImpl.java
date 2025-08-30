package org.seed.fund.storage;

import jakarta.transaction.Transactional;
import org.seed.exception.NotFoundException;
import org.seed.fund.storage.mapper.FundMapper;
import org.seed.fund.storage.jpa.entity.HistoricalDataEntity;
import org.seed.fund.storage.jpa.entity.MetaDataEntity;
import org.seed.fund.storage.jpa.repository.HistoricalDataRepository;
import org.seed.fund.storage.jpa.repository.MetaDataRepository;
import org.seed.fund.model.Fund;
import org.seed.fund.model.HistoricalData;
import org.seed.fund.model.MetaData;
import org.seed.fund.storage.mapper.HistoricalDataMapper;
import org.seed.fund.storage.mapper.MetaDataMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class FundStorageImpl implements FundStorage {
    private final MetaDataRepository metaDataRepository;
    private final HistoricalDataRepository historicalDataRepository;
    private final MetaDataMapper metaDataMapper;
    private final HistoricalDataMapper historicalDataMapper;
    private final FundMapper fundMapper;

    public FundStorageImpl(MetaDataRepository metaDataRepository, HistoricalDataRepository historicalDataRepository, MetaDataMapper metaDataMapper, HistoricalDataMapper historicalDataMapper, FundMapper fundMapper) {
        this.metaDataRepository = metaDataRepository;
        this.historicalDataRepository = historicalDataRepository;
        this.metaDataMapper = metaDataMapper;
        this.historicalDataMapper = historicalDataMapper;
        this.fundMapper = fundMapper;
    }

    public Fund getFundByCode(String code) {
        MetaDataEntity metaDataEntity = metaDataRepository
                .findOneByCode(code)
                .orElseThrow(() -> new NotFoundException("No fund found with the specified code"));

        MetaData metaData = metaDataMapper.toModel(metaDataEntity);

        HistoricalData historicalData = historicalDataRepository.findOneByMetaDataCode(code, PageRequest.of(0, 1))
                .stream().findFirst()
                .map(historicalDataMapper::toModel)
                .orElse(null);

        return Fund.create(metaData, historicalData);
    }

    @Override
    public List<MetaData> getMetaDataList() {
        return metaDataRepository.findAll()
                .stream()
                .map(metaDataMapper::toModel)
                .toList();
    }

    public List<HistoricalData> getHistoricalDataByDateRange(Fund fund, LocalDate beginDate, LocalDate endDate) {
        MetaDataEntity metaDataEntity = metaDataMapper.toEntity(fund.getMetaData());
        return historicalDataRepository.findAllByMetaDataAndValueDateBetween(metaDataEntity, beginDate, endDate)
                .stream()
                .map(historicalDataMapper::toModel)
                .toList();
    }

    public List<Fund> getFundsByValueDate(LocalDate valueDate) {
        return historicalDataRepository.findAllByValueDate(valueDate)
                .stream()
                .map(fundMapper::toModel)
                .toList();
    }

    @Override
    public List<HistoricalData> getHistoricalDataByDateRange(String code, LocalDate beginDate, LocalDate endDate) {
        return historicalDataRepository.findAllByDateRange(code, beginDate, endDate)
                .stream()
                .map(historicalDataMapper::toModel)
                .toList();
    }

    public MetaData save(MetaData metaData) {
        MetaDataEntity metaDataEntity = metaDataMapper.toEntity(metaData);
        MetaDataEntity createdMetaData = metaDataRepository.save(metaDataEntity);
        return metaDataMapper.toModel(createdMetaData);
    }

    @Transactional
    @Override
    public void saveAll(List<MetaData> metaDataList) {
        List<MetaDataEntity> entities = metaDataList.stream()
                .map(metaDataMapper::toEntity)
                .toList();

        metaDataRepository.saveAll(entities);
    }

    @Override
    public HistoricalData save(MetaData metaData, HistoricalData historicalData) {
        HistoricalDataEntity historicalDataEntity = historicalDataMapper.toEntity(metaData, historicalData);
        HistoricalDataEntity entity = historicalDataRepository.save(historicalDataEntity);
        return historicalDataMapper.toModel(entity);
    }

    @Transactional
    @Override
    public void saveAll(MetaData metaData, List<HistoricalData> historicalDataList) {
        List<HistoricalDataEntity> entities = historicalDataList.stream()
                .map(item -> historicalDataMapper.toEntity(metaData, item))
                .toList();

        historicalDataRepository.saveAll(entities);
    }

}
