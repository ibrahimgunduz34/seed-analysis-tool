package org.seed.fund.api.controller;

import org.seed.fund.api.dto.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.swing.text.DateFormatter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController()
@RequestMapping("/simulator")
public class SimulatorController {
    @PostMapping("simulate")
    public SimulateResponse simulate(@RequestBody SimulateRequest request) {
        return new SimulateResponse(
                new FundMetaData(
                        "ABC",
                        "ABC portfoy",
                        "Serbest Fon",
                        "TRY"
                ),
                new InvestmentData(
                        request.getInitialAmount(),
                        request.getFrequency(),
                        request.getStartDate(),
                        request.getEndDate()
                ),
                new StrategyGroupData(
                        new StrategyData(
                                BigDecimal.valueOf(1000000),
                                BigDecimal.valueOf(1200000),
                                BigDecimal.valueOf(200000),
                                200000.0 / 1000000.0
                        ),
                        new StrategyData(
                                BigDecimal.valueOf(1000000),
                                BigDecimal.valueOf(1200000),
                                BigDecimal.valueOf(200000),
                                200000.0 / 1000000.0
                        ),
                        new StrategyData(
                                BigDecimal.valueOf(1000000),
                                BigDecimal.valueOf(1200000),
                                BigDecimal.valueOf(200000),
                                200000.0 / 1000000.0
                        )
                )
        );
    }
}
