package org.seed.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BigDecimalMath {
    public static BigDecimal sqrt(BigDecimal value, int scale) {
        if (value.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal two = BigDecimal.valueOf(2);

        // Initial approximation
        BigDecimal x1 = BigDecimal.valueOf(Math.sqrt(value.doubleValue()));
        if (x1.compareTo(BigDecimal.ZERO) == 0) {
            x1 = BigDecimal.ONE; // fallback if value is very small
        }

        BigDecimal x0;
        do {
            x0 = x1;
            x1 = value.divide(x0, scale, RoundingMode.HALF_UP);
            x1 = x1.add(x0);
            x1 = x1.divide(two, scale, RoundingMode.HALF_UP);
        } while (!x0.equals(x1));

        return x1;
    }

    public static double convertToPercentage(BigDecimal value) {
        return value.multiply(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
}
