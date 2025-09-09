package org.seed.stock.storage.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "stock_historical_data", indexes = {
        @Index(columnList = "value_date")
})
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StockHistoricalDataEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private StockMetaDataEntity metaData;

    @Column(nullable = false)
    private LocalDate valueDate;

    @Column(nullable = false, precision = 12, scale = 4)
    private BigDecimal price;

    @Column(nullable = false)
    private LocalDateTime createdAt;
}
