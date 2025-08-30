package org.seed.fund.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class FundMetaData {
    private final String code;
    private final String name;
    private final String fundType;
    private final String currency;
}
