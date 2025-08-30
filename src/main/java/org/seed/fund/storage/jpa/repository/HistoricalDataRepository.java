package org.seed.fund.storage.jpa.repository;

import org.seed.fund.storage.jpa.entity.HistoricalDataEntity;
import org.seed.fund.storage.jpa.entity.MetaDataEntity;
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

    // TODO: Refactoring required
    // As JQL does not support limit, I found the following solution to retrieve the latest price of the specified code.
    // Find better way to retrieve the last price of the specified fund code
    @Query("SELECT h FROM HistoricalDataEntity h " +
            "JOIN FETCH h.metaData " +
            "WHERE h.metaData.code = :code " +
            "ORDER BY h.valueDate DESC ")
    List<HistoricalDataEntity> findOneByMetaDataCode(@Param("code") String code, Pageable pageable);

    @Query("SELECT h FROM HistoricalDataEntity h " +
            "JOIN FETCH h.metaData " +
            "WHERE h.valueDate = :valueDate")
    List<HistoricalDataEntity> findAllByValueDate(@Param("valueDate") LocalDate valueDate);

    @Query("SELECT h FROM HistoricalDataEntity h " +
            "JOIN FETCH h.metaData " +
            "WHERE h.metaData.code = :code " +
            "AND h.valueDate BETWEEN :startDate AND :endDate " +
            "ORDER BY h.valueDate ASC")
    List<HistoricalDataEntity> findAllByDateRange(@Param("code") String code, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
