package org.ametiste.scm.broker.boot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.handler.advice.RequestHandlerRetryAdvice;
import org.springframework.retry.backoff.BackOffPolicy;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

/**
 * Configuration with components for send messages retry mechanism that organized as advice instance with specified retry
 * policy ({@code RetryTemplate}).
 */
@Configuration
@EnableConfigurationProperties(SenderRetryProperties.class)
public class RetryPolicyConfiguration {

    @Autowired
    private SenderRetryProperties properties;

    @Bean
    public RequestHandlerRetryAdvice sendMessagesRetryAdvice() {
        RequestHandlerRetryAdvice advice = new RequestHandlerRetryAdvice();
        advice.setRetryTemplate(sendMessagesRetryTemplate());

        return advice;
    }

    @Bean
    public RetryTemplate sendMessagesRetryTemplate() {
        RetryTemplate template = new RetryTemplate();

        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
        retryPolicy.setMaxAttempts(properties.getMaxAttempts());

        template.setRetryPolicy(retryPolicy);
        template.setBackOffPolicy(backOffPolicy());

        return template;
    }

    /**
     * Create backoff policy instance based on application properties.
     * @return {@code BackOffPolicy} instance.
     */
    private BackOffPolicy backOffPolicy() {
        if (properties.isExponentialBackOff()) {
            ExponentialBackOffPolicy exponentialBackOffPolicy = new ExponentialBackOffPolicy();
            exponentialBackOffPolicy.setInitialInterval(properties.getInterval());
            exponentialBackOffPolicy.setMaxInterval(properties.getMaxInterval());
            exponentialBackOffPolicy.setMultiplier(properties.getMultiplier());

            return exponentialBackOffPolicy;
        } else {
            FixedBackOffPolicy fixedBackOffPolicy = new FixedBackOffPolicy();
            fixedBackOffPolicy.setBackOffPeriod(properties.getInterval());

            return fixedBackOffPolicy;
        }
    }
}
