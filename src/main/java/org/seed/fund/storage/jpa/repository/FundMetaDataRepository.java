package org.seed.fund.storage.jpa.repository;

import org.seed.fund.storage.jpa.entity.FundMetaDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FundMetaDataRepository extends JpaRepository<FundMetaDataEntity, Long> {
    @Query("SELECT f FROM FundMetaDataEntity f WHERE f.code = :code")
    Optional<FundMetaDataEntity> findOneByCode(@Param("code") String code);
}
