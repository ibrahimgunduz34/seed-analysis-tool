package com.seed.core.printer;

import com.seed.core.AnalysisContext;
import com.seed.core.model.HistoricalData;
import com.seed.core.model.MetaData;

import java.util.List;

public interface Printer {
    <M extends MetaData, H extends HistoricalData> void print(List<AnalysisContext<M, H>> contexts);
}
