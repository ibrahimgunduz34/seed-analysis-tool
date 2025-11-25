package com.seed.fund.persistence.mapper;

import com.seed.fund.model.Fund;
import com.seed.fund.model.FundHistoricalData;
import com.seed.fund.model.FundMetaData;
import com.seed.fund.persistence.entity.FundHistoricalDataEntity;
import com.seed.fund.persistence.entity.FundMetaDataEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface FundMapper {
    FundMetaData mapToFundMetaData(FundMetaDataEntity metaDataEntity);
    FundHistoricalData mapToFundHistoricalData(FundHistoricalDataEntity fundHistoricalDataEntity);
    Fund mapToFundByMetaDataAndHistoricalDataList(FundMetaDataEntity metaDataEntity, List<FundHistoricalDataEntity> fundHistoricalDataEntity);

    @Mapping(target = "id", source = "metaDataEntity.id")
    @Mapping(target = "metaData", source = "metaDataEntity")
    @Mapping(target = "historicalData", expression = "java(List.of())")
    Fund mapToFundByMetaDataEntity(FundMetaDataEntity metaDataEntity);

    @Mapping(target = "id", source = "historicalDataEntity.metaData.id")
    @Mapping(target = "metaData", source = "historicalDataEntity.metaData")
    @Mapping(target = "historicalData", expression = "java(List.of(mapToFundHistoricalData(historicalDataEntity)))")
    Fund mapToFundByHistoricalDataEntity(FundHistoricalDataEntity historicalDataEntity);

    List<Fund> mapToFundListByFundHistoricalDataEntityList(List<FundHistoricalDataEntity> historicalDataEntities);

    List<Fund> mapToFundListByFundMetaDataEntityList(List<FundMetaDataEntity> metaDataEntities);

    @Mapping(target = "id", source = "metaDataEntity.id")
    @Mapping(target = "metaData", source = "historicalDataEntity.metaData")
    @Mapping(target = "historicalData", source = "historicalDataList")
    default List<Fund> mapToFundListByHistoricalDataEntityGroupingByMetaData(List<FundHistoricalDataEntity> historicalDataList) {
        return historicalDataList.stream()
                .collect(Collectors.groupingBy(FundHistoricalDataEntity::getMetaData))
                .entrySet()
                .stream()
                .map(item -> this.mapToFundByMetaDataAndHistoricalDataList(item.getKey(), item.getValue()))
                .toList();
    }
}
