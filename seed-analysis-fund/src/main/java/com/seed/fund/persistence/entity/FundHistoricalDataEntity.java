package com.seed.fund.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "fund_historical_data")
public class FundHistoricalDataEntity extends AuditableEntity {
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
}
