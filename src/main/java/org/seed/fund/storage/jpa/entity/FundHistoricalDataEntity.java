package org.seed.fund.storage.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "fund_historical_data", indexes = {
        @Index(columnList = "value_date")
})
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FundHistoricalDataEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private FundMetaDataEntity metaData;

    @Column(nullable = false)
    private BigDecimal numberOfShares;

    @Column(nullable = false)
    private Integer numberOfInvestors;

    @Column(nullable = false)
    private BigDecimal totalValue;

    @Column(nullable = false, precision = 12, scale = 4)
    private BigDecimal price;

    @Column(nullable = false)
    private LocalDate valueDate;

    @Column(nullable = false)
    private LocalDateTime createdAt;
}
