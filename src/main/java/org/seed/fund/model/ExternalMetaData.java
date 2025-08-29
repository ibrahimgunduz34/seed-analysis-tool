package org.seed.fund.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Currency;

@AllArgsConstructor
@Getter
public class ExternalMetaData {
    private final String code;
    private final String name;
    private final String fundType;
    private final Currency currency;
}
