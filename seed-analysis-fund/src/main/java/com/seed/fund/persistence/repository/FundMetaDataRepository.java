package com.seed.fund.persistence.repository;

import com.seed.fund.persistence.entity.FundMetaDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FundMetaDataRepository extends JpaRepository<FundMetaDataEntity, Long> {
    @Query("SELECT f FROM FundMetaDataEntity f WHERE f.code = :code")
    Optional<FundMetaDataEntity> findByCode(@Param("code") String code);
}
