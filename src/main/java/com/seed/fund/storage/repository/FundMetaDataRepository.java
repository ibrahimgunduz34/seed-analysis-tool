package com.seed.fund.storage.repository;

import com.seed.fund.storage.entity.FundMetaDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface FundMetaDataRepository extends JpaRepository<FundMetaDataEntity, Long> {
    @Query("SELECT f FROM FundMetaDataEntity f WHERE f.code = :code")
    Optional<FundMetaDataEntity> findOneByCode(@Param("code") String code);

    @Query("SELECT f FROM FundMetaDataEntity f " +
            "WHERE EXISTS (" +
            "SELECT 1 FROM FundHistoricalDataEntity h " +
            "WHERE h.metaData = f " +
            "AND h.valueDate = :valueDate" +
            ")")
    List<FundMetaDataEntity> findAllByValueDate(@Param("valueDate") LocalDate valueDate);
}
