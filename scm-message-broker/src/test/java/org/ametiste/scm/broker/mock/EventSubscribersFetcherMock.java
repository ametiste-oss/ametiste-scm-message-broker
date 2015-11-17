package org.ametiste.scm.broker.mock;

import org.ametiste.scm.coordinator.accessor.EventSubscribersFetcher;
import org.ametiste.scm.coordinator.accessor.FetchSubscribersException;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static org.apache.commons.lang3.Validate.isTrue;

/**
 * Mock for {@code EventSubscribersFetcher} interface that provide possibility get predefined list of subscribers
 * without Distributed Coordinator connection.
 */
public class EventSubscribersFetcherMock implements EventSubscribersFetcher {

    private final Collection<URI> subscribers;

    /**
     * Create default EventSubscribersFetcher with two subscribers.
     */
    public EventSubscribersFetcherMock() {
        try {
            subscribers = Arrays.asList(
                    new URI("http://subscriber1:8080/event-receiver"),
                    new URI("http://subscriber2:8080/event-receiver")
            );
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * Create EventSubscribersFetcher with specified list of subscribers.
     */
    public EventSubscribersFetcherMock(Collection<URI> subscribers) {
        isTrue(subscribers != null, "'subscribers' must be initialized");
        this.subscribers = subscribers;
    }

    @Override
    public Collection<URI> fetchSubscribers() throws FetchSubscribersException {
        return subscribers;
    }

    @Override
    public Collection<URI> fetchSubscribers(String pattern) throws FetchSubscribersException {
        return Collections.emptyList();
    }

    /**
     * Returns number of subscribers.
     */
    public long getNumberOfSubscribers() {
        return subscribers.size();
    }
}
