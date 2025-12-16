package com.seed.fund.storage.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "fund_meta_data", indexes = {
        @Index(columnList = "code")
})
@Cacheable
@EntityListeners(AuditingEntityListener.class)
public class FundMetaDataEntity {
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
    @CreatedDate
    private LocalDateTime createdAt;

    public FundMetaDataEntity() {
    }

    public FundMetaDataEntity(Long id, String code, String name, String fundType, String currency) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.fundType = fundType;
        this.currency = currency;
    }

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getFundType() {
        return fundType;
    }

    public String getCurrency() {
        return currency;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
