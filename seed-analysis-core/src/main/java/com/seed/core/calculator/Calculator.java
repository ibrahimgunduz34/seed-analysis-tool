package com.seed.core.calculator;

import com.seed.core.AnalysisContext;
import com.seed.core.model.Candle;
import com.seed.core.model.ResultKey;

import java.util.List;
import java.util.Map;

public interface Calculator<C extends Candle> {
    List<ResultKey<?>> requires();
    List<ResultKey<?>> produces();
    Map<ResultKey<?>, Object> calculate(AnalysisContext<?, C> ctx);
}
