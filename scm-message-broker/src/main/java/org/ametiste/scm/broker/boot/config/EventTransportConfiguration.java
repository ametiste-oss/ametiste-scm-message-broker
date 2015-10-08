package org.ametiste.scm.broker.boot.config;

import org.ametiste.scm.messaging.sender.EventSender;
import org.ametiste.scm.messaging.sender.HttpEventSender;
import org.apache.http.client.HttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Configuration that defines components for sending and receiving messages outbound the application.
 * <p>
 * Configuration enables {@code org.ametiste.scm.messaging.receiver.EventReceivingController} for receiving messages and
 * publish it with Spring ApplicationEventPublisher. Also config create beans for sending messages.
 * <p>
 * Configuration enables {@code SenderClientProperties} to customize Http Client for event sending.
 */
@Configuration
@EnableWebMvc
@ComponentScan("org.ametiste.scm.messaging.receiver")
@EnableConfigurationProperties(SenderClientProperties.class)
public class EventTransportConfiguration {

    @Autowired
    private SenderClientProperties props;

    @Bean
    public HttpClient eventSenderHttpClient() {
        return HttpEventSender.createHttpClient(props.getConnectTimeout(), props.getReadTimeout());
    }

    @Bean
    public EventSender scmEventSender() {
        return new HttpEventSender(eventSenderHttpClient());
    }
}
