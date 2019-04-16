/**
 * Copyright (c) 2007-2013 Alysson Bessani, Eduardo Alchieri, Paulo Sousa, and the authors indicated in the @author tags
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package bftsmart.tom.util;

/**
 * The type Storage.
 */
public class Storage {

    private long[] values;
    private int count = 0;

    /**
     * Creates a new instance of Storage
     *
     * @param size the size
     */
    public Storage(int size) {
        values = new long[size];
    }

    /**
     * Gets count.
     *
     * @return the count
     */
    public int getCount() {
        return count;
    }

    /**
     * Reset.
     */
    public void reset() {
        count = 0;
    }

    /**
     * Store.
     *
     * @param value the value
     */
    public void store(long value) {
        if (count < values.length) {
            values[count++] = value;
        }
    }

    /**
     * Gets average.
     *
     * @param limit the limit
     * @return the average
     */
    public double getAverage(boolean limit) {
        long[] values = java.util.Arrays.copyOfRange(this.values, 0, count);

        return computeAverage(values, limit);
    }

    /**
     * Gets dp.
     *
     * @param limit the limit
     * @return the dp
     */
    public double getDP(boolean limit) {
        long[] values = java.util.Arrays.copyOfRange(this.values, 0, count);

        return computeDP(values, limit);
    }

    /**
     * Get values long [ ].
     *
     * @return the long [ ]
     */
    public long[] getValues() {
        return values;
    }

    /**
     * Gets max.
     *
     * @param limit the limit
     * @return the max
     */
    public long getMax(boolean limit) {
        long[] values = java.util.Arrays.copyOfRange(this.values, 0, count);
        return computeMax(values, limit);
    }

    private double computeAverage(long[] values, boolean percent) {
        java.util.Arrays.sort(values);
        int limit = 0;
        if (percent) {
            limit = values.length / 10;
        }
        long count = 0;
        for (int i = limit; i < values.length - limit; i++) {
            count = count + values[i];
        }
        return (double)count / (double)(values.length - 2 * limit);
    }

    private long computeMax(long[] values, boolean percent) {
        java.util.Arrays.sort(values);
        int limit = 0;
        if (percent) {
            limit = values.length / 10;
        }
        long max = 0;
        for (int i = limit; i < values.length - limit; i++) {
            if (values[i] > max) {
                max = values[i];
            }
        }
        return max;
    }

    private double computeDP(long[] values, boolean percent) {
        if (values.length <= 1) {
            return 0;
        }
        java.util.Arrays.sort(values);
        int limit = 0;
        if (percent) {
            limit = values.length / 10;
        }
        long num = 0;
        double med = computeAverage(values, percent);
        long quad = 0;

        for (int i = limit; i < values.length - limit; i++) {
            num++;
            quad = quad + values[i] * values[i]; //Math.pow(values[i],2);
        }
        double var = (quad - (num * (med * med))) / (num - 1);
        ////br.ufsc.das.util.Logger.println("mim: "+values[limit]);
        ////br.ufsc.das.util.Logger.println("max: "+values[values.length-limit-1]);
        return Math.sqrt(var);
    }

    /**
     * Gets percentile.
     *
     * @param percentile the percentile
     * @return the percentile
     */
    public long getPercentile(double percentile) {

        long[] values = java.util.Arrays.copyOfRange(this.values, 0, count);
        java.util.Arrays.sort(values);

        int index = (int)(percentile * values.length);
        return values[index];
    }
}
