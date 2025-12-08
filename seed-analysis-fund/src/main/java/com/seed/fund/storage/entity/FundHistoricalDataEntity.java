package com.seed.fund.storage.entity;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "fund_historical_data", indexes = {
        @Index(columnList = "value_date")
})
@Cacheable
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

    public FundHistoricalDataEntity() {
    }

    public FundHistoricalDataEntity(Long id, FundMetaDataEntity metaData, BigDecimal numberOfShares, Integer numberOfInvestors, BigDecimal totalValue, BigDecimal price, LocalDate valueDate, LocalDateTime createdAt) {
        this.id = id;
        this.metaData = metaData;
        this.numberOfShares = numberOfShares;
        this.numberOfInvestors = numberOfInvestors;
        this.totalValue = totalValue;
        this.price = price;
        this.valueDate = valueDate;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public FundMetaDataEntity getMetaData() {
        return metaData;
    }

    public BigDecimal getNumberOfShares() {
        return numberOfShares;
    }

    public Integer getNumberOfInvestors() {
        return numberOfInvestors;
    }

    public BigDecimal getTotalValue() {
        return totalValue;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public LocalDate getValueDate() {
        return valueDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
