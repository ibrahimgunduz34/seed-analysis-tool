package com.seed.fund.storage;

import com.seed.fund.storage.entity.FundMetaDataEntity;
import org.springframework.stereotype.Repository;
import com.seed.core.storage.MetaDataStorage;
import com.seed.fund.mapper.MetaDataMapper;
import com.seed.fund.model.FundMetaData;
import com.seed.fund.storage.repository.FundMetaDataRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class FundMetaDataStorage implements MetaDataStorage<FundMetaData> {
    private final FundMetaDataRepository metaDataRepository;
    private final MetaDataMapper metaDataMapper;

    public FundMetaDataStorage(FundMetaDataRepository metaDataRepository, MetaDataMapper metaDataMapper) {
        this.metaDataRepository = metaDataRepository;
        this.metaDataMapper = metaDataMapper;
    }

    @Override
    public Optional<FundMetaData> getMetaDataByCode(String code) {
        return metaDataRepository.findOneByCode(code)
                .map(metaDataMapper::toModel);
    }

    @Override
    public List<FundMetaData> getAllMetaData() {
        return metaDataRepository.findAll().stream().map(metaDataMapper::toModel).toList();
    }

    @Override
    public List<FundMetaData> getAllMetaDataByValueDate(LocalDate valueDate) {
        return metaDataRepository.findAllByValueDate(valueDate)
                .stream()
                .map(metaDataMapper::toModel)
                .toList();
    }

    @Override
    public void saveMetaData(List<FundMetaData> metaDataList) {
        List<FundMetaDataEntity> entities = metaDataList.stream()
                .map(metaDataMapper::toEntity)
                .toList();
        metaDataRepository.saveAll(entities);
    }
}
