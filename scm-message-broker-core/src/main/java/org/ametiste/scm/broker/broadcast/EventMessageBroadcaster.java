package org.ametiste.scm.broker.broadcast;

import org.ametiste.scm.coordinator.accessor.EventSubscribersFetcher;
import org.ametiste.scm.messaging.data.event.Event;
import org.ametiste.scm.messaging.data.transport.TransportMessage;
import org.ametiste.scm.messaging.sender.EventSendException;
import org.ametiste.scm.messaging.sender.EventSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import static org.apache.commons.lang3.Validate.isTrue;

/**
 * Implementation of {@code MessageBroadcaster} interface for broadcasting {@code TransportMessage} with {@code Event}
 * payload.
 * <p>
 * {@code EventMessageBroadcaster} failed broadcast only if there are no subscribers or fail send messages to all
 * subscribers.
 */
public class EventMessageBroadcaster implements MessageBroadcaster<Event> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final EventSubscribersFetcher fetcher;
    private final EventSender sender;

    /**
     * Create event transport messages broadcaster.
     * @param fetcher implementation of {@code EventSubscribersFetcher} interface used for fetching subscribers list.
     * @param sender implementation of {@code EventSender} interface used for sending messages.
     */
    public EventMessageBroadcaster(EventSubscribersFetcher fetcher, EventSender sender) {
        isTrue(fetcher != null, "'fetcher' must be initialized");
        isTrue(sender != null, "'sender' must be initialized");

        this.fetcher = fetcher;
        this.sender = sender;
    }

    @Override
    public void broadcast(Collection<TransportMessage<Event>> messages) throws BroadcastOperationException, NoSubscriberException {
        if (messages == null || messages.isEmpty()) {
            warning(i -> logger.warn("attempt to broadcast null or empty collection of messages"));
        } else {
            Collection<URI> uris = fetcher.fetchSubscribers();
            if (uris.isEmpty()) {
                throw new NoSubscriberException("broadcaster found no subscriber");
            }

            AtomicInteger errors = new AtomicInteger(0);
            uris.stream().forEach(p -> {
                try {
                    sender.send(p, messages);
                } catch (EventSendException e) {
                    errors.incrementAndGet();
                    warning(i -> logger.warn("failed send messages to " + p.toString(), e));
                }
            });

            if (errors.get() == uris.size()) {
                throw new BroadcastOperationException("failed to send messages to all subscribers");
            }
        }
    }

    private void warning(Consumer<Void> operation) {
        if (logger.isWarnEnabled()) { operation.accept(null); }
    }
}
