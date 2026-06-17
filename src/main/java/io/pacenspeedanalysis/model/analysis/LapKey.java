package io.pacenspeedanalysis.model.analysis;

public record LapKey(String entityName, short lapNumber) {
    public LapKey {
        if (entityName == null || entityName.isBlank()) {
            throw new IllegalArgumentException("entityName must be neither null or blank");
        }
        if (lapNumber <= 0) {
            throw new IllegalArgumentException("lapNumber must be a positive number");
        }
    }
}

