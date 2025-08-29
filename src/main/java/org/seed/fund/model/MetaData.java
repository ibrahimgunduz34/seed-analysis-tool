package org.seed.fund.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Currency;

@AllArgsConstructor
@Getter
public class MetaData {
    private final Long id;
    private final String code;
    private final String name;
    private final String fundType;
    private final Currency currency;
    private final LocalDateTime createdAt;

    public static MetaData create(String code, String name, String fundType, Currency currency) {
        return new MetaData(
                null,
                code,
                name,
                fundType,
                currency,
                LocalDateTime.now()
        );
    }
}
