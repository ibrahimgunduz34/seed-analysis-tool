package org.seed.fund.jpa;

import jakarta.transaction.Transactional;
import org.seed.exception.NotFoundException;
import org.seed.fund.FundStorage;
import org.seed.fund.jpa.entity.HistoricalDataEntity;
import org.seed.fund.jpa.entity.MetaDataEntity;
import org.seed.fund.jpa.repository.HistoricalDataRepository;
import org.seed.fund.jpa.repository.MetaDataRepository;
import org.seed.fund.model.Fund;
import org.seed.fund.model.HistoricalData;
import org.seed.fund.model.MetaData;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class FundStorageImpl implements FundStorage {
    private final MetaDataRepository metaDataRepository;
    private final HistoricalDataRepository historicalDataRepository;

    public FundStorageImpl(MetaDataRepository metaDataRepository, HistoricalDataRepository historicalDataRepository) {
        this.metaDataRepository = metaDataRepository;
        this.historicalDataRepository = historicalDataRepository;
    }

    public Fund getFundByCode(String code) {
        MetaData metaData = metaDataRepository
                .findOneByCode(code)
                .orElseThrow(() -> new NotFoundException("No fund found with the specified code")).toModel();

        HistoricalData historicalData = historicalDataRepository.findOneByMetaDataCode(code, PageRequest.of(0, 1))
                .stream().findFirst()
                .map(HistoricalDataEntity::toModel)
                .orElse(null);

        return Fund.create(metaData, historicalData);
    }

    @Override
    public List<MetaData> getMetaDataList() {
        return metaDataRepository.findAll()
                .stream()
                .map(MetaDataEntity::toModel)
                .toList();
    }

    public List<HistoricalData> getHistoricalDataByDateRange(Fund fund, LocalDate beginDate, LocalDate endDate) {
        MetaDataEntity metaDataEntity = MetaDataEntity.fromModel(fund.getMetaData());
        return historicalDataRepository.findAllByMetaDataAndValueDateBetween(metaDataEntity,beginDate, endDate)
                .stream()
                .map(HistoricalDataEntity::toModel)
                .toList();
    }

    public List<Fund> getFundsByValueDate(LocalDate valueDate) {
        return historicalDataRepository.findAllByValueDate(valueDate)
                .stream()
                .map(item -> new Fund(
                        item.getMetaData().getId(),
                        item.getMetaData().toModel(),
                        item.toModel()
                ))
                .toList();
    }

    public MetaData save(MetaData metaData) {
        MetaDataEntity metaDataEntity = MetaDataEntity.fromModel(metaData);
        return metaDataRepository.save(metaDataEntity).toModel();
    }

    @Transactional
    @Override
    public void saveAll(List<MetaData> metaDataList) {
        List<MetaDataEntity> entities = metaDataList.stream()
                .map(MetaDataEntity::fromModel)
                .toList();

        metaDataRepository.saveAll(entities);
    }

    @Override
    public HistoricalData save(MetaData metaData, HistoricalData historicalData) {
        HistoricalDataEntity historicalDataEntity = HistoricalDataEntity.fromModel(metaData, historicalData);
        return historicalDataRepository.save(historicalDataEntity).toModel();
    }

    @Transactional
    @Override
    public void saveAll(MetaData metaData, List<HistoricalData> historicalDataList) {
        List<HistoricalDataEntity> entities = historicalDataList.stream()
                .map(item -> HistoricalDataEntity.fromModel(metaData, item))
                .toList();

        historicalDataRepository.saveAll(entities);
    }
}
