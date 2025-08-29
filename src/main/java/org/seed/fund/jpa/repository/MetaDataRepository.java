package org.seed.fund.jpa.repository;

import org.seed.fund.jpa.entity.MetaDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MetaDataRepository extends JpaRepository<MetaDataEntity, Long> {
    @Query("SELECT f FROM MetaDataEntity f WHERE f.code = :code")
    Optional<MetaDataEntity> findOneByCode(@Param("code") String code);
}
