package io.pacenspeedanalysis.feature.filter;

public abstract class BestLapsFilter extends LapsFilter {

    private static final int MIN_THRESHOLD = 1;

    protected int threshold;

    public BestLapsFilter(int threshold) {
        if (threshold < MIN_THRESHOLD) {
            throw new IllegalArgumentException("threshold must be greater or equal to " + (MIN_THRESHOLD));
        }

        this.threshold = threshold;
    }
}
