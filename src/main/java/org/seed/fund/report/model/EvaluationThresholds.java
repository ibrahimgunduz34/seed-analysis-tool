package org.seed.fund.report.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class EvaluationThresholds {

    private final double sharpeHigh;
    private final double sharpeMedium;

    private final double returnStrong;
    private final double returnGood;
    private final double returnModerate;

    private final double volatilityHigh;
    private final double volatilityMedium;
}
