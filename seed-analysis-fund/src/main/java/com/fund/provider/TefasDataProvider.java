package com.fund.provider;

import com.seed.core.DataProvider;

import java.time.LocalDate;

public class TefasDataProvider implements DataProvider {
    @Override
    public Object exportMetaData() {
        return null;
    }

    @Override
    public Object exportHistoricalData(LocalDate valueDate) {
        return null;
    }
}
