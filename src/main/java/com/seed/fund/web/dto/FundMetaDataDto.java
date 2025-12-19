package com.seed.fund.web.dto;

public record FundMetaDataDto(
        String code,
        String name,
        String fundType,
        String currency
) {}
