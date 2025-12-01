package com.seed.core.calculator;

import com.seed.core.AnalysisContext;
import com.seed.core.Candle;
import com.seed.core.ResultKey;

import java.util.List;
import java.util.Map;

public interface Calculator<T extends Candle> {
    List<ResultKey<?>> requires();
    List<ResultKey<?>> produces();
    Map<ResultKey<?>, Object> calculate(AnalysisContext<T> ctx);
}
