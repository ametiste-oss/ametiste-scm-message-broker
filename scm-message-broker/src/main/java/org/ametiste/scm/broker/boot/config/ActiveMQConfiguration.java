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
    public ActiveMQConnectionFactory amqConnectionFactory() {
        ActiveMQConnectionFactory amqFactory = new ActiveMQConnectionFactory();
        amqFactory.setRedeliveryPolicy(redeliveryPolicy());
        amqFactory.setBrokerURL(props.getBrokerUrl());
        amqFactory.setUserName(props.getUsername());
        amqFactory.setPassword(props.getPassword());
        return amqFactory;
    }

    @Bean
    public RedeliveryPolicy redeliveryPolicy() {
        RedeliveryPolicy policy = new RedeliveryPolicy();

        policy.setQueue("*");
        policy.setMaximumRedeliveries(props.getRedelivery().getMaxRedeliveries());
        policy.setInitialRedeliveryDelay(props.getRedelivery().getInitialDelay());
        policy.setMaximumRedeliveryDelay(props.getRedelivery().getMaxDelay());

        return policy;
    }

    @Bean
    public JmsTransactionManager jmsTransactionManager() {
        JmsTransactionManager manager = new JmsTransactionManager();
        manager.setConnectionFactory(connectionFactory());
        return manager;
    }
}
