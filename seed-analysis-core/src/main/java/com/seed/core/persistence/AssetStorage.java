package com.seed.core.persistence;

import com.seed.core.Asset;

import java.time.LocalDate;
import java.util.List;

public interface AssetStorage<T extends Asset> {
    T getAssetByCode(String code);
    List<T> getList();
    List<T> getListByValueDate(LocalDate valueDate);
    List<T> getListByDateRange(String code, LocalDate beginDate, LocalDate endDate);
    void saveMetaDataList(List<T> asset);
    void saveHistoricalDataList(T asset);
}
