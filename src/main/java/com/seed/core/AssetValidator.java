package com.seed.core;

import com.seed.core.exception.NoResourceFoundException;
import com.seed.core.model.MetaData;
import com.seed.core.storage.MetaDataStorage;

public class AssetValidator<M extends MetaData> {
    private final MetaDataStorage<M> metaDataStorage;

    public AssetValidator(MetaDataStorage<M> metaDataStorage) {
        this.metaDataStorage = metaDataStorage;
    }

    public void validate(String[] codes) throws NoResourceFoundException {
        for (String code : codes) {
            if (metaDataStorage.getMetaDataByCode(code).isEmpty()) {
                throw new NoResourceFoundException("No resource found for code " + code);
            }
        }
    }
}
