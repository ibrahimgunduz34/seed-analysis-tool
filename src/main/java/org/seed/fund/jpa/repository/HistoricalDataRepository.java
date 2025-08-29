package org.seed.fund.jpa.repository;

import org.seed.fund.jpa.entity.HistoricalDataEntity;
import org.seed.fund.jpa.entity.MetaDataEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface HistoricalDataRepository extends JpaRepository<HistoricalDataEntity, Long> {
    List<HistoricalDataEntity> findAllByMetaDataAndValueDateBetween(
            MetaDataEntity metaData,
            LocalDate startDate,
            LocalDate endDate
    );

    @Query("SELECT h FROM HistoricalDataEntity h " +
            "JOIN FETCH h.metaData " +
            "WHERE h.metaData.code = :code " +
            "ORDER BY h.valueDate DESC ")
    List<HistoricalDataEntity> findOneByMetaDataCode(@Param("code") String code, Pageable pageable);

    @Query("SELECT h FROM HistoricalDataEntity h " +
            "JOIN FETCH h.metaData " +
            "WHERE h.valueDate = :valueDate")
    List<HistoricalDataEntity> findAllByValueDate(@Param("valueDate") LocalDate valueDate);
}
