package org.seed.stock.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Currency;

@Getter
@AllArgsConstructor
public class ExternalStockMetaData {
    private final String code;
    private final String name;
    private final String exchange;
    private final Currency currency;
}
