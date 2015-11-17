package org.ametiste.scm.broker.boot.config;

import org.ametiste.scm.messaging.sender.EventSender;
import org.ametiste.scm.messaging.sender.HttpEventSender;
import org.apache.http.client.HttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Configuration that defines components for sending messages outbound the application. Event receiving mechanism
 * includes with boot starter module.
 * <p>
 * Configuration enables {@code SenderClientProperties} to customize Http Client for event sending.
 */
@Configuration
@EnableWebMvc
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
