package org.seed.fund.storage.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "fund_meta_data", indexes = {
        @Index(columnList = "code")
})
@Getter
@AllArgsConstructor
@NoArgsConstructor
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
    private LocalDateTime createdAt;
}
