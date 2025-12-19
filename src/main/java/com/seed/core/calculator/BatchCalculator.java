package com.seed.core.calculator;

import com.seed.core.AnalysisContext;
import com.seed.core.model.HistoricalData;
import com.seed.core.model.MetaData;
import com.seed.core.model.ResultKey;

import java.util.List;

public interface BatchCalculator <M extends MetaData, H extends HistoricalData> {
    List<ResultKey<?>> requires();
    List<ResultKey<?>> produces();
    List<AnalysisContext<M, H>> calculate(List<AnalysisContext<M, H>> contexts);
}
