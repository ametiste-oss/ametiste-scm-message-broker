package org.ametiste.scm.broker.processing;

import org.ametiste.scm.messaging.data.event.Event;
import org.ametiste.scm.messaging.data.transport.TransportMessage;

/**
 * Interface provides protocol to communicate with transport message processing system of Event Broker.
 * <p>
 * Any implementation of this interface should be designed for high load operation.
 */
public interface EventMessageProcessor {

    /**
     * Handle transport message with broker processing system.
     * <p>
     * Method execution should be as fast as possible to provide short time of request processing. Good idea is store
     * message in some type of storage and than later process it.
     *
     * @param message transport message with event payload.
     */
    void process(TransportMessage<Event> message);
}
