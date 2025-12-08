package com.seed.fund.storage;

import com.seed.core.storage.MetaDataStorage;
import com.seed.fund.model.FundMetaData;
import com.seed.fund.storage.mapper.MetaDataMapper;
import com.seed.fund.storage.repository.FundMetaDataRepository;
import org.springframework.stereotype.Repository;

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
}
