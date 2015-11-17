package org.ametiste.scm.broker.broadcast

import spock.lang.Specification


class NoSubscriberExceptionTest extends Specification {
    private String message = "message"
    private RuntimeException e = new RuntimeException()

    def "test exception create normal"() {
        when: "create exception with message only"
        NoSubscriberException exception = new NoSubscriberException(message);

        then: "expect take correct object"
        exception != null
        exception.getMessage().equals(message)

        when: "create exception with message and throwable"
        NoSubscriberException exceptionWithThrowable = new NoSubscriberException(message, e)

        then: "expect take correct object"
        exceptionWithThrowable != null
        exceptionWithThrowable.getMessage().equals(message)
        exceptionWithThrowable.getCause().equals(e)
    }
}
