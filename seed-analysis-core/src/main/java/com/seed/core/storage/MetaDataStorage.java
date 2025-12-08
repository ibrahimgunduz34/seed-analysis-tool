package com.seed.core.storage;

import com.seed.core.model.MetaData;

import java.util.Optional;

public interface MetaDataStorage<M extends MetaData> {
    Optional<M> getMetaDataByCode(String code);
}
