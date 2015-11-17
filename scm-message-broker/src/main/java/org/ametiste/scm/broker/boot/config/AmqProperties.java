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
 *     <tr>
 *         <td>maxRedeliveries</td>
 *         <td>Integer</td>
 *         <td>Number of redelivery retries.</td>
 *         <td>-1 (infinite redelivery)</td>
 *     </tr>
 * </table>
 * <p>
 * This properties is designed provide usable default configuration that provides embedded ActiveMQ Broker without
 * username/password access.
 */
@ConfigurationProperties("org.ametiste.scm.broker.amq")
public class AmqProperties {

    private QueueName queueName;

    private String brokerUrl = "vm://localhost";
    private String username = "";
    private String password = "";
    private int maxRedeliveries = -1;

    public QueueName getQueueName() {
        return queueName;
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

    public int getMaxRedeliveries() {
        return maxRedeliveries;
    }

    public void setQueueName(QueueName queueName) {
        this.queueName = queueName;
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

    public void setMaxRedeliveries(int maxRedeliveries) {
        this.maxRedeliveries = maxRedeliveries;
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
}
