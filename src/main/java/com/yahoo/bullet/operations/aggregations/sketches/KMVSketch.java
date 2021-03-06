package com.yahoo.bullet.operations.aggregations.sketches;

import com.yahoo.bullet.result.Metadata.Concept;

import java.util.HashMap;
import java.util.Map;

/**
 * This class wraps some common metadata information for KMV Sketches - Theta and Tuple.
 */
public abstract class KMVSketch extends Sketch {
    // Metadata keys for Standard Deviation
    public static final String META_STD_DEV_1 = "1";
    public static final String META_STD_DEV_2 = "2";
    public static final String META_STD_DEV_3 = "3";
    public static final String META_STD_DEV_UB = "upperBound";
    public static final String META_STD_DEV_LB = "lowerBound";

    /**
     * Gets the theta value for this sketch after the last collect. Only applicable after {@link #collect()}.
     *
     * @return A Double value that is the theta for this sketch.
     * @throws NullPointerException if collect had not been called.
     */
    protected abstract Double getTheta();

    /**
     * Gets the lower bound at this standard deviation after the last collect. Only applicable after {@link #collect()}.
     *
     * @param standardDeviation The standard deviation.
     * @return A Double representing the maximum value at this standard deviation.
     * @throws NullPointerException if collect had not been called.
     */
    protected abstract Double getLowerBound(int standardDeviation);

    /**
     * Gets the uppper bound at this standard deviation after the last collect. Only applicable after {@link #collect()}.
     *
     * @param standardDeviation The standard deviation.
     * @return A Double representing the minimum value at this standard deviation.
     * @throws NullPointerException if collect had not been called.
     */
    protected abstract Double getUpperBound(int standardDeviation);

    @Override
    protected Map<String, Object> getMetadata(Map<String, String> conceptKeys) {
        Map<String, Object> metadata = super.getMetadata(conceptKeys);
        addIfNonNull(metadata, conceptKeys.get(Concept.STANDARD_DEVIATIONS.getName()), this::getStandardDeviations);
        addIfNonNull(metadata, conceptKeys.get(Concept.THETA.getName()), this::getTheta);
        return metadata;
    }

    /**
     * Gets all the standard deviations for this sketch.
     *
     * @return A standard deviations {@link Map} containing all the standard deviations.
     */
    private Map<String, Map<String, Double>> getStandardDeviations() {
        Map<String, Map<String, Double>> standardDeviations = new HashMap<>();
        standardDeviations.put(META_STD_DEV_1, getStandardDeviation(1));
        standardDeviations.put(META_STD_DEV_2, getStandardDeviation(2));
        standardDeviations.put(META_STD_DEV_3, getStandardDeviation(3));
        return standardDeviations;
    }

    /**
     * Gets the standard deviation for this sketch.
     *
     * @param standardDeviation The standard deviation to get. Valid values are 1, 2, and 3.
     * @return A standard deviation {@link Map} containing the upper and lower bound.
     */
    private Map<String, Double> getStandardDeviation(int standardDeviation) {
        double lowerBound = getLowerBound(standardDeviation);
        double upperBound = getUpperBound(standardDeviation);
        Map<String, Double> bounds = new HashMap<>();
        bounds.put(META_STD_DEV_LB, lowerBound);
        bounds.put(META_STD_DEV_UB, upperBound);
        return bounds;
    }
}
