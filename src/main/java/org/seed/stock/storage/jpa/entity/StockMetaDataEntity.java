package org.seed.stock.storage.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "stock_meta_data", indexes = {
        @Index(columnList = "code")
})
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StockMetaDataEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 6)
    private String code;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(nullable = false, length = 10)
    private String exchange;

    @Column(nullable = false, length = 4)
    private String currency;

    @Column(nullable = false)
    private LocalDateTime createdAt;
}
