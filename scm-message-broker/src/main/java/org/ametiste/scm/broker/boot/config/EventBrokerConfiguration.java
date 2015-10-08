package org.ametiste.scm.broker.boot.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Main configuration of Event Broker application.
 */
@Configuration
@Import({EventProcessingConfiguration.class, EventTransportConfiguration.class})
public class EventBrokerConfiguration {
}
