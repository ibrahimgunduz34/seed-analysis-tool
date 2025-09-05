package org.seed.fund.report.calculator;

import org.seed.fund.report.model.ReportContext;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Function;

public class PositiveNegativeDaysCalculator implements Function<ReportContext, ReportContext> {
    @Override
    public ReportContext apply(ReportContext ctx) {
        List<BigDecimal> dailyChanges = ctx.getDailyChanges();

        if (ctx.getNumberOfDays() == 0) {
            ctx.setNumberOfPositiveDays(0);
            ctx.setNumberOfNegativeDays(0);
            ctx.setWeightOfPositiveDays(0.0);
            ctx.setWeightOfNegativeDays(0.0);
            return ctx;
        }

        int numberOfPositiveDays = 0;
        int numberOfNegativeDays = 0;
        double weightOfPositiveDays = 0.0;
        double weightOfNegativeDays = 0.0;

        for (BigDecimal dailyChange : dailyChanges) {
            if (dailyChange.signum() > 0) {
                numberOfPositiveDays++;
            } else if (dailyChange.signum() < 0) {
                numberOfNegativeDays++;
            }
        }

        int totalNonZeroDays = numberOfPositiveDays + numberOfNegativeDays;
        if (totalNonZeroDays > 0) {
            weightOfPositiveDays = (double) numberOfPositiveDays / totalNonZeroDays;
            weightOfNegativeDays = (double) numberOfNegativeDays / totalNonZeroDays;
        } else {
            weightOfPositiveDays = 0.0;
            weightOfNegativeDays = 0.0;
        }


        ctx.setNumberOfPositiveDays(numberOfPositiveDays);
        ctx.setNumberOfNegativeDays(numberOfNegativeDays);
        ctx.setWeightOfPositiveDays(weightOfPositiveDays);
        ctx.setWeightOfNegativeDays(weightOfNegativeDays);

        return ctx;
    }
}
