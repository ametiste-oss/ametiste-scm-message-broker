package org.ametiste.scm.broker.boot.config;

import org.ametiste.scm.broker.broadcast.EventMessageBroadcaster;
import org.ametiste.scm.broker.processing.EventMessageProcessor;
import org.ametiste.scm.coordinator.accessor.EventSubscribersFetcher;
import org.ametiste.scm.coordinator.config.ScmSubscribersFetcherConfiguration;
import org.ametiste.scm.messaging.data.transport.EventTransportMessage;
import org.ametiste.scm.messaging.sender.EventSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.event.EventListener;

/**
 * Configuration defines components that are responsible for listen events and sending them to processing pipeline via
 * {@code EventMessageProcessor} interface.
 * Configuration includes xml config with processing flow on spring integration.
 * Also configuration provide {@code EventMessageBroadcaster} component for execution main function: broadcast
 * aggregated messages to subscribes.
 */
@Configuration
@Import(EventTransportConfiguration.class)
@ImportResource("classpath:spring/integration/queue-config.xml")
public class EventProcessingConfiguration {

    @Autowired
    private EventMessageProcessor eventMessageProcessor;

    @Autowired
    private EventSender scmEventSender;

    @Autowired
    private EventSubscribersFetcher subscribersFetcher;

    @EventListener
    private void onMessageReceived(EventTransportMessage message) {
        eventMessageProcessor.process(message);
    }

    @Bean
    public EventMessageBroadcaster eventMessageBroadcaster() {
        return new EventMessageBroadcaster(subscribersFetcher, scmEventSender);
    }
}
