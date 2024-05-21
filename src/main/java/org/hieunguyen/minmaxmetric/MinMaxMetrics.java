package org.hieunguyen.minmaxmetric;

public class MinMaxMetrics {

  volatile Long minValue, maxValue;

  /**
   * Initializes all member variables
   */
  public MinMaxMetrics() {
    // Add code here
  }

  /**
   * Adds a new sample to our metrics.
   */
  public synchronized void addSample(long newSample) {
    if (minValue == null) {
      minValue = newSample;
      maxValue = newSample;
    } else {
      minValue = Math.min(newSample, minValue);
      maxValue = Math.max(newSample, maxValue);
    }
  }

  /**
   * Returns the smallest sample we've seen so far.
   */
  public long getMin() {
    return minValue;
  }

  /**
   * Returns the biggest sample we've seen so far.
   */
  public long getMax() {
    return maxValue;
  }
}

