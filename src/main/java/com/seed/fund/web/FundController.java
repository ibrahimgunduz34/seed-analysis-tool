package com.seed.fund.web;

import com.seed.core.BatchAssetAnalyzer;
import com.seed.core.storage.MetaDataStorage;
import com.seed.fund.model.FundTypeEnum;
import com.seed.fund.mapper.AnalysisContextMapper;
import com.seed.fund.mapper.MetaDataMapper;
import com.seed.fund.model.FundHistoricalData;
import com.seed.fund.model.FundMetaData;
import com.seed.fund.web.dto.AnalysisContextDto;
import com.seed.fund.web.dto.AnalysisFundsRequestDto;
import com.seed.fund.web.dto.ErrorCategory;
import com.seed.fund.web.dto.ErrorModel;
import com.seed.fund.web.dto.FundMetaDataDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/funds")
public class FundController {
    private final BatchAssetAnalyzer<FundMetaData, FundHistoricalData> analyzer;
    private final MetaDataStorage<FundMetaData> metaDataStorage;
    private final AnalysisContextMapper analysisContextMapper;
    private final MetaDataMapper metaDataMapper;

    public FundController(BatchAssetAnalyzer<FundMetaData, FundHistoricalData> analyzer,
                          MetaDataStorage<FundMetaData> metaDataStorage, AnalysisContextMapper analysisContextMapper,
                          MetaDataMapper metaDataMapper) {
        this.analyzer = analyzer;
        this.metaDataStorage = metaDataStorage;
        this.analysisContextMapper = analysisContextMapper;
        this.metaDataMapper = metaDataMapper;
    }

    @GetMapping()
    public List<FundMetaDataDto> getMetaDataList(@RequestParam(name = "fundType", required = false)FundTypeEnum fundType) {
        if (fundType == null) {
            return metaDataStorage.getAllMetaData()
                    .stream()
                    .map(metaDataMapper::toDto)
                    .toList();
        }

        return metaDataStorage.getAllMetaData()
                .stream()
                .filter(metaData -> metaData.fundType().equals(fundType.getValue()))
                .map(metaDataMapper::toDto)
                .toList();
    }

    @GetMapping("/{code}")
    public ResponseEntity<?> getMetaData(@PathVariable("code") String code) {
        Optional<FundMetaDataDto> fundMetaDataDto = metaDataStorage.getMetaDataByCode(code.toUpperCase())
                .map(metaDataMapper::toDto);

        if (fundMetaDataDto.isPresent()) {
            return ResponseEntity.ok().body(fundMetaDataDto.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorModel(ErrorCategory.NOT_FOUND, "No metadata found with the specified code"));
        }
    }

    @PostMapping("/analysis")
    public ResponseEntity<?> analyzeFund(@RequestBody AnalysisFundsRequestDto requestDto) {
        // TODO: Refactor this method later

        try {
            requestDto.codes().forEach(code -> {
                metaDataStorage.getMetaDataByCode(code.toUpperCase())
                        .orElseThrow();
            });
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorModel(ErrorCategory.NOT_FOUND, "No metadata found with the specified code"));
        }


        List<AnalysisContextDto> contextDtoList = analyzer.analyze(requestDto.codes().toArray(String[]::new),
                        requestDto.startDate(),
                        requestDto.endDate())
                .stream().map(analysisContextMapper::toDto)
                .toList();

        return ResponseEntity.ok(contextDtoList);
    }
}
