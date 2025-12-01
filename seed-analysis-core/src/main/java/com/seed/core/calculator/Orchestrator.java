package com.seed.core.calculator;

import com.seed.core.AnalysisContext;
import com.seed.core.Candle;
import com.seed.core.ResultKey;
import com.seed.core.exception.IllegalAccessException;

import java.util.List;

public class Orchestrator<T extends Candle> {
    private final List<Calculator<T>> steps;

    public Orchestrator(List<Calculator<T>> steps) {
        this.steps = steps;
    }

    public void run(AnalysisContext<T> ctx) {
        for (var calculator : steps) {
            for (ResultKey<?> req : calculator.produces()) {
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
