package io.pacenspeedanalysis.feature.filter;

public abstract class BestLapsFilter extends LapsFilter {

    private static final int MIN_THRESHOLD = 1;

    protected int threshold;

    public BestLapsFilter(int threshold) {
        if (threshold < MIN_THRESHOLD) {
            throw new IllegalArgumentException("Threshold must be greater than " + (MIN_THRESHOLD - 1));
        }

        this.threshold = threshold;
    }
}
