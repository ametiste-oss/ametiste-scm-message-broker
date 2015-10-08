package org.ametiste.scm.broker.boot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties for ActiveMQ Broker and queues.
 * <p>
 * Defined properties are included (org.ametiste.scm.broker.amq.*):
 * <ul>
 * <li>queue-name.raw-event - name of queue with raw event messages;</li>
 * <li>queue-name.aggregated-event - name of queue with aggregated lists of event messages;</li>
 * <li>broker-url - broker URL to configure ActiveMQ;</li>
 * <li>username - username to access to ActiveMQ instance;</li>
 * <li>password - password to access to ActiveMQ instance.</li>
 * </ul>
 * <p>
 * This properties is designed provide usable default configuration that provides embedded ActiveMQ Broker without
 * username/password access.
 */
@ConfigurationProperties("org.ametiste.scm.broker.amq")
public class AmqProperties {

    /**
     * Nested properties that contains names of used queues.
     */
    private QueueName queueName;

    /**
     * Broker URL to configure ActiveMQ. Default is "vm://localhost".
     */
    private String brokerUrl = "vm://localhost";

    /**
     * Username to access to ActiveMQ instance. No default value.
     */
    private String username = "";

    /**
     * Password to access to ActiveMQ instance. No default value.
     */
    private String password = "";

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

    /**
     * Nested properties that contains names of required queues.
     */
    public static class QueueName {

        /**
         * Name of queue with raw event messages. Default value is "queue.raw".
         */
        private String rawEvent = "queue.raw";

        /**
         * Name of queue with aggregated lists of event messages. Default value is "queue.aggregated".
         */
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
