package org.seed.stock.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Currency;

@Getter
@AllArgsConstructor
public class StockMetaData {
    private final Long id;
    private final String code;
    private final String name;
    private final String exchange;
    private final Currency currency;
    private final LocalDateTime createdAt;
}
