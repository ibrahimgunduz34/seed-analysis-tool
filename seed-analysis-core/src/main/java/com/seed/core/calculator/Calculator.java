package com.seed.core.calculator;

import com.seed.core.AnalysisContext;
import com.seed.core.model.HistoricalData;
import com.seed.core.model.ResultKey;

import java.util.List;
import java.util.Map;

public interface Calculator<H extends HistoricalData> {
    List<ResultKey<?>> requires();
    List<ResultKey<?>> produces();
    Map<ResultKey<?>, Object> calculate(AnalysisContext<?, H> ctx);
}
