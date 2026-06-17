package io.pacenspeedanalysis.model.analysis;

public sealed interface Analysis<T> permits PaceAnalysis, SpeedAnalysis {

    String name();

    short laps();

    short totalLaps();

    T average();

    T median();

    T variance();

    T standardDeviation();
}
