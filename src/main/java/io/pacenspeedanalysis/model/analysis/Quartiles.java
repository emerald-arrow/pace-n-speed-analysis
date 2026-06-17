package io.pacenspeedanalysis.model.analysis;

public record Quartiles<T>(
        T q1,
        T q2,
        T q3,
        T iqr,
        T lowerFence,
        T upperFence
) {
}
