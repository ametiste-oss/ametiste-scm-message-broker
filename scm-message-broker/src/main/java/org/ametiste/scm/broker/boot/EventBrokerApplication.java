package org.ametiste.scm.broker.boot;

import org.ametiste.scm.broker.boot.config.EventBrokerConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@EnableAutoConfiguration
@Import(EventBrokerConfiguration.class)
public class EventBrokerApplication extends SpringBootServletInitializer {

    private static Class<EventBrokerApplication> APPLICATION_CLASS = EventBrokerApplication.class;

    public static void main(String[] args) {
        SpringApplication.run(APPLICATION_CLASS, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(APPLICATION_CLASS);
    }
}
