package org.ametiste.scm.broker.boot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties for ActiveMQ Broker and queues.
 * <p>
 * Defined properties are included (org.ametiste.scm.broker.amq.*):
 * <table summary="parameters description">
 *     <tr><td>Name</td><td>Type</td><td>Description</td><td>Default</td></tr>
 *     <tr>
 *         <td>queue-name.raw-event</td>
 *         <td>String</td>
 *         <td>Name of queue with raw event messages.</td>
 *         <td>queue.raw</td>
 *     </tr>
 *     <tr>
 *         <td>queue-name.aggregated-event</td>
 *         <td>String</td>
 *         <td>Name of queue with aggregated lists of event messages.</td>
 *         <td>queue.aggregated</td>
 *     </tr>
 *     <tr>
 *         <td>broker-url</td>
 *         <td>URI</td>
 *         <td>Broker URL to configure ActiveMQ.</td>
 *         <td>vm://localhost</td>
 *     </tr>
 *     <tr>
 *         <td>username</td>
 *         <td>String</td>
 *         <td>Username to access to ActiveMQ instance.</td>
 *         <td>"" (empty string)</td>
 *     </tr>
 *     <tr>
 *         <td>password</td>
 *         <td>String</td>
 *         <td>Password to access to ActiveMQ instance.</td>
 *         <td>"" (empty string)</td>
 *     </tr>
 * </table>
 * Redelivery properties are included (org.ametiste.scm.broker.amq.redelivery.*):
 * <table summary="parameters description">
 *     <tr>
 *         <td>maxRedeliveries</td>
 *         <td>Integer</td>
 *         <td>Number of redelivery retries.</td>
 *         <td>-1 (infinite redelivery)</td>
 *     </tr>
 *     <tr>
 *         <td>initialDelay</td>
 *         <td>Integer</td>
 *         <td>Initial redelivery delay and increase step (if exponential backoff disabled (in milliseconds).</td>
 *         <td>1000</td>
 *     </tr>
 *     <tr>
 *         <td>maxDelay</td>
 *         <td>Integer</td>
 *         <td>Maximum value of redelivery delay (in milliseconds).</td>
 *         <td>30000</td>
 *     </tr>
 * </table>
 * <p>
 * This properties is designed provide usable default configuration that provides embedded ActiveMQ Broker without
 * username/password access.
 */
@ConfigurationProperties("org.ametiste.scm.broker.amq")
public class AmqProperties {

    private QueueName queueName = new QueueName();
    private Redelivery redelivery = new Redelivery();

    private String brokerUrl = "vm://localhost";
    private String username = "";
    private String password = "";

    public QueueName getQueueName() {
        return queueName;
    }

    public Redelivery getRedelivery() {
        return redelivery;
    }

    public String getBrokerUrl() {
        return brokerUrl;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setQueueName(QueueName queueName) {
        this.queueName = queueName;
    }

    public void setRedelivery(Redelivery redelivery) {
        this.redelivery = redelivery;
    }

    public void setBrokerUrl(String brokerUrl) {
        this.brokerUrl = brokerUrl;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Nested properties that contains names of required queues.
     */
    public static class QueueName {

        private String rawEvent = "queue.raw";
        private String aggregatedEvent = "queue.aggregated";

        public String getRawEvent() {
            return rawEvent;
        }

        public String getAggregatedEvent() {
            return aggregatedEvent;
        }

        public void setRawEvent(String rawEvent) {
            this.rawEvent = rawEvent;
        }

        public void setAggregatedEvent(String aggregatedEvent) {
            this.aggregatedEvent = aggregatedEvent;
        }
    }

    /**
     * Nested properties for ActiveMQ redelivery policy parameters.
     */
    public static class Redelivery {

        private int maxRedeliveries = -1;

        private int initialDelay = 1000;
        private int maxDelay = 30000;

        public int getMaxRedeliveries() {
            return maxRedeliveries;
        }

        public int getInitialDelay() {
            return initialDelay;
        }

        public int getMaxDelay() {
            return maxDelay;
        }

        public void setMaxRedeliveries(int maxRedeliveries) {
            this.maxRedeliveries = maxRedeliveries;
        }

        public void setInitialDelay(int initialDelay) {
            this.initialDelay = initialDelay;
        }

        public void setMaxDelay(int maxDelay) {
            this.maxDelay = maxDelay;
        }
    }
}
