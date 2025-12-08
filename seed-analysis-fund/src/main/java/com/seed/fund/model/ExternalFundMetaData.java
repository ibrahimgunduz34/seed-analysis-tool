package com.seed.fund.model;

import java.util.Currency;

public record ExternalFundMetaData(
        String code,
        String name,
        String fundType,
        Currency currency
) {}
