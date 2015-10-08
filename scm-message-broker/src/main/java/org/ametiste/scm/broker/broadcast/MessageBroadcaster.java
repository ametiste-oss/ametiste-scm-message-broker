package org.ametiste.scm.broker.broadcast;

import org.ametiste.scm.messaging.data.transport.TransportMessage;

import java.util.Collection;

/**
 * Interface of the service that provides broadcasting transport messages to group of subscribers.
 * <p>
 * Any implementation of this interface provide way to get list of subscribers and send mechanism by itself.
 */
public interface MessageBroadcaster<T> {

    /**
     * Broadcast collection of messages to group of subscribers.
     * <p>
     * Operation is successful if messages successful delivered to at least one subscriber from group.
     *
     * @param messages collection of transport messages with T type payload.
     *
     * @throws BroadcastOperationException when broadcast operation failed. For example failed send messages to all
     *                                     subscribers group.
     * @throws NoSubscriberException when broadcaster receive empty group of subscribers and no one to send.
     */
    void broadcast(Collection<TransportMessage<T>> messages) throws BroadcastOperationException, NoSubscriberException;
}
