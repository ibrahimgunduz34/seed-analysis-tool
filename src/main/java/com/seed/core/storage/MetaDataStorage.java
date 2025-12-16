package com.seed.core.storage;


import com.seed.core.model.MetaData;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MetaDataStorage<M extends MetaData> {
    Optional<M> getMetaDataByCode(String code);
    List<M> getAllMetaData();
    List<M> getAllMetaDataByValueDate(LocalDate valueDate);
    void saveMetaData(List<M> metaDataList);
}
