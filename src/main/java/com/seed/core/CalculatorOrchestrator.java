package com.seed.core;

import com.seed.core.calculator.Calculator;
import com.seed.core.exception.IllegalAccessException;
import com.seed.core.model.HistoricalData;
import com.seed.core.model.ResultKey;

import java.util.List;

public class CalculatorOrchestrator<C extends HistoricalData> {
    private final List<Calculator<C>> steps;

    public CalculatorOrchestrator(List<Calculator<C>> steps) {
        this.steps = steps;
    }

    public void run(AnalysisContext<?, C> ctx) {
        for (var calculator : steps) {
            for (ResultKey<?> req : calculator.requires()) {
                if (ctx.get(req).isEmpty()) {
                    throw new IllegalAccessException("Missing dependency: " + req.name());
                }
            }

            var result = calculator.calculate(ctx);

            for (var entry : result.entrySet()) {
                @SuppressWarnings("unchecked")
                ResultKey<Object> key = (ResultKey<Object>) entry.getKey();
                Object value = entry.getValue();
                boolean stored = ctx.putIfAbsent(key, value);
                if (!stored) {
                    throw new IllegalAccessException("Duplicate dependency: " + key.name());
                }
            }
        }
    }
}
