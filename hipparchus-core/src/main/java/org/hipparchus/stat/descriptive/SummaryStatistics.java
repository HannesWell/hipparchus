/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.hipparchus.stat.descriptive;

import java.io.Serializable;

import org.hipparchus.exception.NullArgumentException;
import org.hipparchus.stat.descriptive.moment.GeometricMean;
import org.hipparchus.stat.descriptive.moment.Mean;
import org.hipparchus.stat.descriptive.moment.SecondMoment;
import org.hipparchus.stat.descriptive.moment.Variance;
import org.hipparchus.stat.descriptive.rank.Max;
import org.hipparchus.stat.descriptive.rank.Min;
import org.hipparchus.stat.descriptive.summary.Sum;
import org.hipparchus.stat.descriptive.summary.SumOfLogs;
import org.hipparchus.stat.descriptive.summary.SumOfSquares;
import org.hipparchus.util.FastMath;
import org.hipparchus.util.MathUtils;
import org.hipparchus.util.Precision;

/**
 * Computes summary statistics for a stream of data values added using the
 * {@link #addValue(double) addValue} method. The data values are not stored in
 * memory, so this class can be used to compute statistics for very large data
 * streams.
 * <p>
 * The {@link StorelessUnivariateStatistic} instances used to maintain summary
 * state and compute statistics are configurable via a builder. For example, the
 * default implementation for the variance can be overridden by calling
 * <pre>
 *    SummaryStatistics stats =
 *        SummaryStatistics.builder()
 *                         .withVariance(new MyVarianceImpl())
 *                         .build();
 * </pre>
 * <p>
 * Note: This class is not thread-safe. Use
 * {@link SynchronizedSummaryStatistics} if concurrent access from multiple
 * threads is required.
 */
public class SummaryStatistics implements StatisticalSummary, Serializable {

    /** Serialization UID */
    private static final long serialVersionUID = 20160422L;

    /** count of values that have been added */
    private long n = 0;

    /** SecondMoment is used to compute the mean and variance */
    private final SecondMoment secondMoment;
    /** min of values that have been added */
    private final StorelessUnivariateStatistic minImpl;
    /** max of values that have been added */
    private final StorelessUnivariateStatistic maxImpl;
    /** sum of values that have been added */
    private final StorelessUnivariateStatistic sumImpl;
    /** sum of the square of each value that has been added */
    private final StorelessUnivariateStatistic sumOfSquaresImpl;
    /** sumLog of values that have been added */
    private final StorelessUnivariateStatistic sumOfLogsImpl;
    /** mean of values that have been added */
    private final StorelessUnivariateStatistic meanImpl;
    /** variance of values that have been added */
    private final StorelessUnivariateStatistic varianceImpl;
    /** geoMean of values that have been added */
    private final StorelessUnivariateStatistic geoMeanImpl;
    /** population variance of values that have been added */
    private final StorelessUnivariateStatistic populationVariance;

    /** Indicates if the mean impl uses an external moment */
    private final boolean meanUsesExternalMoment;
    /** Indicates if the variance impl uses an external moment */
    private final boolean varianceUsesExternalMoment;
    /** Indicates if the geo mean impl uses an external sum of logs */
    private final boolean geoMeanUsesExternalSumOfLogs;

    /**
     * Returns a builder for a {@link SummaryStatistics}.
     *
     * @return a summary statistics builder.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Construct a SummaryStatistics instance.
     */
    public SummaryStatistics() {
        this(new Builder().build());
    }

    /**
     * A copy constructor. Creates a deep-copy of the {@code original}.
     *
     * @param original the {@code SummaryStatistics} instance to copy
     * @throws NullArgumentException if original is null
     */
    public SummaryStatistics(SummaryStatistics original) throws NullArgumentException {
        MathUtils.checkNotNull(original);

        this.n                = original.n;
        this.secondMoment     = original.secondMoment.copy();
        this.maxImpl          = original.maxImpl.copy();
        this.minImpl          = original.minImpl.copy();
        this.sumImpl          = original.sumImpl.copy();
        this.sumOfLogsImpl    = original.sumOfLogsImpl.copy();
        this.sumOfSquaresImpl = original.sumOfSquaresImpl.copy();

        // Keep default statistics with embedded moments in synch
        if (original.meanUsesExternalMoment) {
            this.meanImpl = new Mean(this.secondMoment);
        } else {
            this.meanImpl = original.meanImpl.copy();
        }
        this.meanUsesExternalMoment = original.meanUsesExternalMoment;

        if (original.varianceUsesExternalMoment) {
            this.varianceImpl = new Variance(this.secondMoment);
        } else {
            this.varianceImpl = original.varianceImpl.copy();
        }
        this.varianceUsesExternalMoment = original.varianceUsesExternalMoment;

        if (original.geoMeanUsesExternalSumOfLogs &&
            this.sumOfLogsImpl instanceof SumOfLogs) {
            this.geoMeanImpl = new GeometricMean((SumOfLogs) this.sumOfLogsImpl);
        } else {
            this.geoMeanImpl = original.geoMeanImpl.copy();
        }
        this.geoMeanUsesExternalSumOfLogs = original.geoMeanUsesExternalSumOfLogs;

        this.populationVariance = new Variance(false, this.secondMoment);
    }

    /**
     * Construct a new SummaryStatistics instance based
     * on the data provided by a builder.
     *
     * @param builder the builder to use.
     */
    protected SummaryStatistics(Builder builder) {
        this.secondMoment       = builder.secondMoment;
        this.maxImpl            = builder.maxImpl;
        this.minImpl            = builder.minImpl;
        this.meanImpl           = builder.meanImpl;
        this.sumImpl            = builder.sumImpl;
        this.sumOfSquaresImpl   = builder.sumOfSquaresImpl;
        this.sumOfLogsImpl      = builder.sumOfLogsImpl;
        this.varianceImpl       = builder.varianceImpl;
        this.geoMeanImpl        = builder.geoMeanImpl;

        // the population variance can not be overridden
        // it will always use the second moment.
        this.populationVariance = new Variance(false, this.secondMoment);

        this.meanUsesExternalMoment       = builder.meanUsesExternalMoment;
        this.varianceUsesExternalMoment   = builder.varianceUsesExternalMoment;
        this.geoMeanUsesExternalSumOfLogs = builder.geometricMeanUsesExternalSumOfLogs;
    }

    /**
     * Return a {@link StatisticalSummaryValues} instance reporting current
     * statistics.
     * @return Current values of statistics
     */
    public StatisticalSummary getSummary() {
        return new StatisticalSummaryValues(getMean(), getVariance(), getN(),
                                            getMax(), getMin(), getSum());
    }

    /**
     * Add a value to the data
     * @param value the value to add
     */
    public void addValue(double value) {
        secondMoment.increment(value);
        minImpl.increment(value);
        maxImpl.increment(value);
        sumImpl.increment(value);
        sumOfSquaresImpl.increment(value);
        sumOfLogsImpl.increment(value);

        // update mean/variance/geoMean if they
        // do not use external moments / sumOfLogs.

        if (!meanUsesExternalMoment) {
            meanImpl.increment(value);
        }
        if (!varianceUsesExternalMoment) {
            varianceImpl.increment(value);
        }
        if (!geoMeanUsesExternalSumOfLogs) {
            geoMeanImpl.increment(value);
        }

        n++;
    }

    /**
     * Returns the number of available values.
     * @return The number of available values
     */
    @Override
    public long getN() {
        return n;
    }

    /**
     * Returns the sum of the values that have been added.
     * @return The sum or <code>Double.NaN</code> if no values have been added
     */
    @Override
    public double getSum() {
        return sumImpl.getResult();
    }

    /**
     * Returns the sum of the squares of the values that have been added.
     * <p>
     * Double.NaN is returned if no values have been added.
     *
     * @return The sum of squares
     */
    public double getSumOfSquares() {
        return sumOfSquaresImpl.getResult();
    }

    /**
     * Returns the mean of the values that have been added.
     * <p>
     * Double.NaN is returned if no values have been added.
     *
     * @return the mean
     */
    @Override
    public double getMean() {
        return meanImpl.getResult();
    }

    /**
     * Returns the standard deviation of the values that have been added.
     * <p>
     * Double.NaN is returned if no values have been added.
     *
     * @return the standard deviation
     */
    @Override
    public double getStandardDeviation() {
        final long size = getN();
        if (size > 0) {
            return size > 1 ? FastMath.sqrt(getVariance()) : 0.0;
        } else {
            return Double.NaN;
        }
    }

    /**
     * Returns the quadratic mean, a.k.a.
     * <a href="http://mathworld.wolfram.com/Root-Mean-Square.html">
     * root-mean-square</a> of the available values
     *
     * @return The quadratic mean or {@code Double.NaN} if no values
     * have been added.
     */
    public double getQuadraticMean() {
        final long size = getN();
        return size > 0 ? FastMath.sqrt(getSumOfSquares() / size) : Double.NaN;
    }

    /**
     * Returns the (sample) variance of the available values.
     * <p>
     * This method returns the bias-corrected sample variance (using {@code n - 1} in
     * the denominator).  Use {@link #getPopulationVariance()} for the non-bias-corrected
     * population variance.
     * <p>
     * Double.NaN is returned if no values have been added.
     *
     * @return the variance
     */
    @Override
    public double getVariance() {
        return varianceImpl.getResult();
    }

    /**
     * Returns the <a href="http://en.wikibooks.org/wiki/Statistics/Summary/Variance">
     * population variance</a> of the values that have been added.
     * <p>
     * Double.NaN is returned if no values have been added.
     *
     * @return the population variance
     */
    public double getPopulationVariance() {
        return populationVariance.getResult();
    }

    /**
     * Returns the maximum of the values that have been added.
     * <p>
     * Double.NaN is returned if no values have been added.
     *
     * @return the maximum
     */
    @Override
    public double getMax() {
        return maxImpl.getResult();
    }

    /**
     * Returns the minimum of the values that have been added.
     * <p>
     * Double.NaN is returned if no values have been added.
     *
     * @return the minimum
     */
    @Override
    public double getMin() {
        return minImpl.getResult();
    }

    /**
     * Returns the geometric mean of the values that have been added.
     * <p>
     * Double.NaN is returned if no values have been added.
     *
     * @return the geometric mean
     */
    public double getGeometricMean() {
        return geoMeanImpl.getResult();
    }

    /**
     * Returns the sum of the logs of the values that have been added.
     * <p>
     * Double.NaN is returned if no values have been added.
     *
     * @return the sum of logs
     */
    public double getSumOfLogs() {
        return sumOfLogsImpl.getResult();
    }

    /**
     * Returns a statistic related to the Second Central Moment. Specifically,
     * what is returned is the sum of squared deviations from the sample mean
     * among the values that have been added.
     * <p>
     * Returns <code>Double.NaN</code> if no data values have been added and
     * returns <code>0</code> if there is just one value in the data set.
     *
     * @return second central moment statistic
     */
    public double getSecondMoment() {
        return secondMoment.getResult();
    }

    /**
     * Generates a text report displaying summary statistics from values that
     * have been added.
     *
     * @return String with line feeds displaying statistics
     */
    @Override
    public String toString() {
        StringBuilder outBuffer = new StringBuilder();
        String endl = "\n";
        outBuffer.append("SummaryStatistics:").append(endl);
        outBuffer.append("n: ").append(getN()).append(endl);
        outBuffer.append("min: ").append(getMin()).append(endl);
        outBuffer.append("max: ").append(getMax()).append(endl);
        outBuffer.append("sum: ").append(getSum()).append(endl);
        outBuffer.append("mean: ").append(getMean()).append(endl);
        outBuffer.append("variance: ").append(getVariance()).append(endl);
        outBuffer.append("population variance: ").append(getPopulationVariance()).append(endl);
        outBuffer.append("standard deviation: ").append(getStandardDeviation()).append(endl);
        outBuffer.append("geometric mean: ").append(getGeometricMean()).append(endl);
        outBuffer.append("second moment: ").append(getSecondMoment()).append(endl);
        outBuffer.append("sum of squares: ").append(getSumOfSquares()).append(endl);
        outBuffer.append("sum of logs: ").append(getSumOfLogs()).append(endl);
        return outBuffer.toString();
    }

    /**
     * Resets all statistics and storage.
     */
    public void clear() {
        this.n = 0;
        minImpl.clear();
        maxImpl.clear();
        sumImpl.clear();
        sumOfLogsImpl.clear();
        sumOfSquaresImpl.clear();
        secondMoment.clear();
        if (!meanUsesExternalMoment) {
            meanImpl.clear();
        }
        if (!varianceUsesExternalMoment) {
            varianceImpl.clear();
        }
        if (!geoMeanUsesExternalSumOfLogs) {
            geoMeanImpl.clear();
        }
    }

    /**
     * Returns true iff <code>object</code> is a <code>SummaryStatistics</code>
     * instance and all statistics have the same values as this.
     *
     * @param object the object to test equality against.
     * @return true if object equals this
     */
    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof SummaryStatistics == false) {
            return false;
        }
        SummaryStatistics stat = (SummaryStatistics)object;
        return Precision.equalsIncludingNaN(stat.getGeometricMean(), getGeometricMean()) &&
               Precision.equalsIncludingNaN(stat.getMax(),           getMax())           &&
               Precision.equalsIncludingNaN(stat.getMean(),          getMean())          &&
               Precision.equalsIncludingNaN(stat.getMin(),           getMin())           &&
               Precision.equalsIncludingNaN(stat.getN(),             getN())             &&
               Precision.equalsIncludingNaN(stat.getSum(),           getSum())           &&
               Precision.equalsIncludingNaN(stat.getSumOfSquares(),  getSumOfSquares())  &&
               Precision.equalsIncludingNaN(stat.getSumOfLogs(),     getSumOfLogs())     &&
               Precision.equalsIncludingNaN(stat.getVariance(),      getVariance());
    }

    /**
     * Returns hash code based on values of statistics.
     * @return hash code
     */
    @Override
    public int hashCode() {
        int result = 31 + MathUtils.hash(getGeometricMean());
        result = result * 31 + MathUtils.hash(getGeometricMean());
        result = result * 31 + MathUtils.hash(getMax());
        result = result * 31 + MathUtils.hash(getMean());
        result = result * 31 + MathUtils.hash(getMin());
        result = result * 31 + MathUtils.hash(getN());
        result = result * 31 + MathUtils.hash(getSum());
        result = result * 31 + MathUtils.hash(getSumOfSquares());
        result = result * 31 + MathUtils.hash(getSumOfLogs());
        result = result * 31 + MathUtils.hash(getVariance());
        return result;
    }

    /**
     * Returns a copy of this SummaryStatistics instance with the same internal state.
     *
     * @return a copy of this
     */
    public SummaryStatistics copy() {
        return new SummaryStatistics(this);
    }

    /**
     * A mutable builder for a SummaryStatistics.
     */
    public static class Builder {
        /** Second moment can not be set externally, needed for other statistics. */
        private final SecondMoment secondMoment = new SecondMoment();

        private StorelessUnivariateStatistic maxImpl;
        private StorelessUnivariateStatistic minImpl;
        private StorelessUnivariateStatistic sumImpl;
        private StorelessUnivariateStatistic sumOfSquaresImpl;
        private StorelessUnivariateStatistic sumOfLogsImpl;
        private StorelessUnivariateStatistic meanImpl;
        private StorelessUnivariateStatistic varianceImpl;
        private StorelessUnivariateStatistic geoMeanImpl;

        private boolean meanUsesExternalMoment;
        private boolean varianceUsesExternalMoment;
        private boolean geometricMeanUsesExternalSumOfLogs;

        protected Builder() {}

        /**
         * Sets the max implementation to use.
         *
         * @param maxImpl the max implementation
         * @return the builder
         * @throws NullArgumentException if maxImpl is null
         */
        public Builder withMaxImpl(StorelessUnivariateStatistic maxImpl) {
            MathUtils.checkNotNull(maxImpl);
            this.maxImpl = maxImpl;
            return this;
        }

        /**
         * Sets the min implementation to use.
         *
         * @param minImpl the min implementation
         * @return the builder
         * @throws NullArgumentException if minImpl is null
         */
        public Builder withMinImpl(StorelessUnivariateStatistic minImpl) {
            MathUtils.checkNotNull(minImpl);
            this.minImpl = minImpl;
            return this;
        }

        /**
         * Sets the mean implementation to use.
         *
         * @param meanImpl the mean implementation
         * @return the builder
         * @throws NullArgumentException if meanImpl is null
         */
        public Builder withMeanImpl(StorelessUnivariateStatistic meanImpl) {
            MathUtils.checkNotNull(meanImpl);
            this.meanImpl = meanImpl;
            return this;
        }

        /**
         * Sets the geometric mean implementation to use.
         *
         * @param geometricMeanImpl the geometric mean implementation
         * @return the builder
         * @throws NullArgumentException if geometricMeanImpl is null
         */
        public Builder withGeometricMeanImpl(StorelessUnivariateStatistic geometricMeanImpl) {
            MathUtils.checkNotNull(geometricMeanImpl);
            this.geoMeanImpl = geometricMeanImpl;
            return this;
        }

        /**
         * Sets the variance implementation to use.
         *
         * @param varianceImpl the variance implementation
         * @return the builder
         * @throws NullArgumentException if varianceImpl is null
         */
        public Builder withVarianceImpl(StorelessUnivariateStatistic varianceImpl) {
            MathUtils.checkNotNull(varianceImpl);
            this.varianceImpl = varianceImpl;
            return this;
        }

        /**
         * Sets the sum implementation to use.
         *
         * @param sumImpl the sum implementation
         * @return the builder
         * @throws NullArgumentException if sumImpl is null
         */
        public Builder withSumImpl(StorelessUnivariateStatistic sumImpl) {
            MathUtils.checkNotNull(sumImpl);
            this.sumImpl = sumImpl;
            return this;
        }

        /**
         * Sets the sum of squares implementation to use.
         *
         * @param sumSqImpl the sum of squares implementation
         * @return the builder
         * @throws NullArgumentException if sumSqImpl is null
         */
        public Builder withSumOfSquaresImpl(StorelessUnivariateStatistic sumSqImpl) {
            MathUtils.checkNotNull(sumSqImpl);
            this.sumOfSquaresImpl = sumSqImpl;
            return this;
        }

        /**
         * Sets the sum of logs implementation to use.
         *
         * @param sumLogImpl the sum of logs implementation
         * @return the builder
         * @throws NullArgumentException if sumLogImpl is null
         */
        public Builder withSumOfLogsImpl(StorelessUnivariateStatistic sumLogImpl) {
            MathUtils.checkNotNull(sumLogImpl);
            this.sumOfLogsImpl = sumLogImpl;
            return this;
        }

        /**
         * Sets up default implementations to use.
         */
        protected void setupDefaultsImpls() {
            if (maxImpl == null) {
                maxImpl = new Max();
            }
            if (minImpl == null) {
                minImpl = new Min();
            }
            if (sumImpl == null) {
                sumImpl = new Sum();
            }
            if (sumOfSquaresImpl == null) {
                sumOfSquaresImpl = new SumOfSquares();
            }
            if (sumOfLogsImpl == null) {
                sumOfLogsImpl = new SumOfLogs();
            }
            if (meanImpl == null) {
                meanImpl = new Mean(secondMoment);
                meanUsesExternalMoment = true;
            }
            if (varianceImpl == null) {
                varianceImpl = new Variance(secondMoment);
                varianceUsesExternalMoment = true;
            }
            if (geoMeanImpl == null) {
                if (sumOfLogsImpl instanceof SumOfLogs) {
                    geoMeanImpl = new GeometricMean((SumOfLogs) sumOfLogsImpl);
                    geometricMeanUsesExternalSumOfLogs = true;
                } else {
                    geoMeanImpl = new GeometricMean();
                }
            }
        }

        /**
         * Constructs a new SummaryStatistics instance with the values
         * stored in this builder.
         *
         * @return a new SummaryStatistics instance.
         */
        public SummaryStatistics build() {
            // setup default implementations.
            setupDefaultsImpls();
            return new SummaryStatistics(this);
        }
    }

}
