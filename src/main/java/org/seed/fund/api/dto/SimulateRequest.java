package org.seed.fund.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@Getter
public class SimulateRequest {
    private final String code;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private final LocalDate startDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private final LocalDate endDate;
    private final BigDecimal initialAmount;
    private final Integer frequency;
}
