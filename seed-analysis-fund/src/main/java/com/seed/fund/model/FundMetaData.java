package com.seed.fund.model;

import com.seed.core.model.MetaData;

import java.util.Currency;

public record FundMetaData(
        Long id,
        String code,
        String name,
        String fundType,
        Currency currency
) implements MetaData {}
