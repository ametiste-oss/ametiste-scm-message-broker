package org.ametiste.scm.broker.broadcast;

/**
 * Exception signals about attempt to broadcast when no one subscriber present.
 */
public class NoSubscriberException extends RuntimeException {

    public NoSubscriberException(String message) {
        super(message);
    }

    public NoSubscriberException(String message, Throwable cause) {
        super(message, cause);
    }
}
