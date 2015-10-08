package org.ametiste.scm.broker.boot.config;

import org.ametiste.scm.broker.mock.EventSenderMock;
import org.ametiste.scm.broker.mock.EventSubscribersFetcherMock;
import org.ametiste.scm.coordinator.accessor.EventSubscribersFetcher;
import org.ametiste.scm.coordinator.accessor.FetchSubscribersException;
import org.ametiste.scm.messaging.data.event.Event;
import org.ametiste.scm.messaging.data.transport.TransportMessage;
import org.ametiste.scm.messaging.sender.EventSendException;
import org.ametiste.scm.messaging.sender.EventSender;
import org.ametiste.scm.messaging.sender.HttpEventSender;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.net.URISyntaxException;

/**
 * Configuration for integration testing Event Broker application.
 * Test configuration replace fetcher and sender beans with Mocks for testing system without external services.
 */
@Configuration
@EnableAutoConfiguration
@EnableWebMvc
@Import(EventBrokerConfiguration.class)
public class EventBrokerTestConfiguration {

    @Bean
    public EventSubscribersFetcher eurekaEventSubscribersFetcher() throws URISyntaxException {
        return new EventSubscribersFetcherMock();
    }

    @Bean
    public EventSender scmEventSender() {
        return new EventSenderMock();
    }
}
