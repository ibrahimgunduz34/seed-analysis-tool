package org.seed.fund.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.seed.fund.model.MetaData;

import java.time.LocalDateTime;
import java.util.Currency;

@Entity
@Table(name = "fund_meta_data", indexes = {
        @Index(columnList = "code")
})
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MetaDataEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 4)
    private String code;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(nullable = false, length = 50)
    private String fundType;

    @Column(nullable = false, length = 4)
    private String currency;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public static MetaDataEntity fromModel(MetaData model) {
        return new MetaDataEntity(
                model.getId(),
                model.getCode(),
                model.getName(),
                model.getFundType(),
                model.getCurrency().toString(),
                model.getCreatedAt()
        );
    }

    public MetaData toModel() {
        return new MetaData(
                id,
                code,
                name,
                fundType,
                Currency.getInstance(currency),
                createdAt
        );
    }
}
