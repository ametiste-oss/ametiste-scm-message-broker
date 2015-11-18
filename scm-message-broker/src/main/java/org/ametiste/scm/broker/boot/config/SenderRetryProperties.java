package org.ametiste.scm.broker.boot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Defines set of properties that would be used to configure {@code RetryTemplate} instance for sending process retry
 * mechanism.
 * Defined properties are included (org.ametiste.scm.broker.sender.retry.*):
 * <table summary="parameters description">
 *     <tr><td>Name</td><td>Type</td><td>Description</td><td>Default</td></tr>
 *     <tr>
 *         <td>maxAttempts</td>
 *         <td>int</td>
 *         <td>Maximum number of attempts.</td>
 *         <td>5</td>
 *     </tr>
 *     <tr>
 *         <td>interval</td>
 *         <td>int</td>
 *         <td>Interval between retry attempts (in milliseconds).</td>
 *         <td>1000</td>
 *     </tr>
 *     <tr>
 *         <td>exponentialBackOff</td>
 *         <td>boolean</td>
 *         <td>Enables exponential backoff policy.</td>
 *         <td>false</td>
 *     </tr>
 *     <tr>
 *         <td>maxInterval</td>
 *         <td>int</td>
 *         <td>Maximum interval between attempts in case when {@literal exponentialBackOff} is enabled (in milliseconds).</td>
 *         <td>30000</td>
 *     </tr>
 *     <tr>
 *         <td>multiplier</td>
 *         <td>double</td>
 *         <td>Multiplier for {@literal exponentialBackOff} policy.</td>
 *         <td>3.0</td>
 *     </tr>
 * </table>
 * <p>
 * This properties is designed provide usable default configuration that provides retry with fixed number of attempts
 * and fixed retry interval.
 */
@ConfigurationProperties("org.ametiste.scm.broker.sender.retry")
public class SenderRetryProperties {

    private int maxAttempts = 5;
    private int interval = 1000;

    private boolean exponentialBackOff = false;
    private int maxInterval = 30000;
    private double multiplier = 3.0;

    public int getMaxAttempts() {
        return maxAttempts;
    }

    public int getInterval() {
        return interval;
    }

    public boolean isExponentialBackOff() {
        return exponentialBackOff;
    }

    public int getMaxInterval() {
        return maxInterval;
    }

    public double getMultiplier() {
        return multiplier;
    }

    public void setMaxAttempts(int maxAttempts) {
        this.maxAttempts = maxAttempts;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public void setExponentialBackOff(boolean exponentialBackOff) {
        this.exponentialBackOff = exponentialBackOff;
    }

    public void setMaxInterval(int maxInterval) {
        this.maxInterval = maxInterval;
    }

    public void setMultiplier(double multiplier) {
        this.multiplier = multiplier;
    }
}
