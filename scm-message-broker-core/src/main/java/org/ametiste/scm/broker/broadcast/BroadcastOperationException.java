package org.ametiste.scm.broker.broadcast;

/**
 * Exception signals about error occurred during broadcast operation.
 * <p>
 * For example when broadcaster failed to send packet to all subscribers.
 */
public class BroadcastOperationException extends RuntimeException {

    public BroadcastOperationException(String message) {
        super(message);
    }

    public BroadcastOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
