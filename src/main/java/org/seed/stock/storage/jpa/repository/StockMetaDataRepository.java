package org.seed.stock.storage.jpa.repository;

import org.seed.stock.storage.jpa.entity.StockMetaDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockMetaDataRepository extends JpaRepository<StockMetaDataEntity, Long> {
}
