package org.ametiste.scm.broker.broadcast

import spock.lang.Specification


class BroadcastOperationExceptionTest extends Specification {
    private String message = "message"
    private RuntimeException e = new RuntimeException()

    def "test exception create normal"() {
        when: "create exception with message only"
        BroadcastOperationException exception = new BroadcastOperationException(message);

        then: "expect take correct object"
        exception != null
        exception.getMessage().equals(message)

        when: "create exception with message and throwable"
        BroadcastOperationException exceptionWithThrowable = new BroadcastOperationException(message, e)

        then: "expect take correct object"
        exceptionWithThrowable != null
        exceptionWithThrowable.getMessage().equals(message)
        exceptionWithThrowable.getCause().equals(e)
    }
}
