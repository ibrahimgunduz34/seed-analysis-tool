package org.seed.fund.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.seed.fund.model.HistoricalData;
import org.seed.fund.model.MetaData;

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
public class HistoricalDataEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private MetaDataEntity metaData;

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

    public static HistoricalDataEntity fromModel(MetaData metaData, HistoricalData historicalData) {
        return new HistoricalDataEntity(
                historicalData.getId(),
                MetaDataEntity.fromModel(metaData),
                historicalData.getNumberOfShares(),
                historicalData.getNumberOfInvestors(),
                historicalData.getTotalValue(),
                historicalData.getPrice(),
                historicalData.getValueDate(),
                historicalData.getCreatedAt()
        );
    }

    public HistoricalData toModel() {
        return new HistoricalData(
                id,
                numberOfShares,
                numberOfInvestors,
                totalValue,
                price,
                valueDate,
                createdAt
        );
    }
}
