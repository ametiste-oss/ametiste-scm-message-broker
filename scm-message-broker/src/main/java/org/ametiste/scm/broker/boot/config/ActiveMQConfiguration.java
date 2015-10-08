package org.ametiste.scm.broker.boot.config;

import org.apache.activemq.RedeliveryPolicy;
import org.apache.activemq.spring.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.connection.JmsTransactionManager;
import org.springframework.jms.connection.SingleConnectionFactory;

/**
 * ActiveMQ components configuration: jms connection factory and transaction manager.
 * <p>
 * This components used in spring integration part backend. Queue configured with {@code AmqProperties} properties.
 */
@Configuration
@EnableConfigurationProperties(AmqProperties.class)
public class ActiveMQConfiguration {

    @Autowired
    private AmqProperties props;

    /**
     * The redelivery policy is ignored (except of maximumRedeliveries, -1 is infinite redelivery) because redelivery
     * is handled by the consumer which needs to be cached to ensure the previous delay is taken into account.
     * Therefore the connection factory needs to be a CachingConnectionFactory. Unfortunately this won't work because
     * the session is then also cached and won't have an associated transaction so rollback doesn't force redelivery.
     * Configuration of redelivery on the broker is ignored.
     * <p>
     * The default Dead Letter Queue in ActiveMQ is called ActiveMQ.DLQ; in case of maximumRedeliveries greater than 0,
     * all undeliverable messages will get sent to this queue (configuration of deadLetterStrategy on broker is ignored).
     */
    @Bean
    public ActiveMQConnectionFactory amqConnectionFactory() {
        ActiveMQConnectionFactory amqFactory = new ActiveMQConnectionFactory();
        amqFactory.setRedeliveryPolicy(redeliveryPolicy());
        amqFactory.setBrokerURL(props.getBrokerUrl());
        amqFactory.setUserName(props.getUsername());
        amqFactory.setPassword(props.getPassword());
        return amqFactory;
    }

    /**
     * Set up the connection factory. You must name the bean "connectionFactory" for the JMS-backed channels to
     * automatically find it. Otherwise, you must specify the connection when you declare the channel.
     */
    @Bean
    public SingleConnectionFactory connectionFactory() {
        SingleConnectionFactory factory = new SingleConnectionFactory();
        factory.setTargetConnectionFactory(amqConnectionFactory());
        return factory;
    }

    @Bean
    public JmsTransactionManager jmsTransactionManager() {
        JmsTransactionManager manager = new JmsTransactionManager();
        manager.setConnectionFactory(connectionFactory());
        return manager;
    }

    private RedeliveryPolicy redeliveryPolicy() {
        RedeliveryPolicy policy = new RedeliveryPolicy();
        policy.setQueue("*");
        policy.setMaximumRedeliveries(-1);
        return policy;
    }
}
