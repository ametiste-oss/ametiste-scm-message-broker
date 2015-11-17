package org.ametiste.scm.broker.broadcast

import org.ametiste.scm.coordinator.accessor.EurekaEventSubscribersFetcher
import org.ametiste.scm.coordinator.accessor.EventSubscribersFetcher
import org.ametiste.scm.messaging.data.transport.EventTransportMessage
import org.ametiste.scm.messaging.sender.EventSendException
import org.ametiste.scm.messaging.sender.EventSender
import org.ametiste.scm.messaging.sender.HttpEventSender
import spock.lang.Specification

class EventMessageBroadcasterTest extends Specification {

    private EventMessageBroadcaster broadcaster;
    private EventSender eventSender;
    private EventSubscribersFetcher fetcher;

    private List<EventTransportMessage> messages = Arrays.asList(new EventTransportMessage());
    private List<URI> subscribers = Arrays.asList(
            new URI("http://subscriber1:8080/event-receiver"),
            new URI("http://subscriber2:8080/event-receiver"),
    );

    def setup() {
        eventSender = Mock(HttpEventSender.class)
        fetcher = Mock(EurekaEventSubscribersFetcher.class)
        broadcaster = new EventMessageBroadcaster(fetcher, eventSender)
    }

    def "validation on create"() {
        when: "create broadcaster with not initialized fetcher"
        new EventMessageBroadcaster(null, eventSender)

        then: "expect IllegalArgumentException thrown"
        thrown(IllegalArgumentException.class)

        when: "create broadcaster with not initialized sender"
        new EventMessageBroadcaster(fetcher, null)

        then: "expect IllegalArgumentException thrown"
        thrown(IllegalArgumentException.class)

        when: "create broadcaster with initialized arguments"
        new EventMessageBroadcaster(fetcher, eventSender)

        then: "expect IllegalArgumentException thrown"
        noExceptionThrown()
    }

    def "should nothing do with empty or not initialized list of messages"() {
        when: "try send empty messages list"
        broadcaster.broadcast(Collections.emptyList())

        then: "broadcaster nothing to do"
        0 * eventSender.send(_,_)
        0 * fetcher.fetchSubscribers()

        when: "try send not initialized list"
        broadcaster.broadcast(null)

        then: "broadcaster nothing to do"
        0 * eventSender.send(_,_)
        0 * fetcher.fetchSubscribers()
    }

    def "should throw exception when found no subscribers"() {
        when: "send messages"
        broadcaster.broadcast(messages);

        then: "fetcher return empty subscribers list"
        1 * fetcher.fetchSubscribers() >> Collections.emptyList();

        and: "broadcaster throw exception"
        thrown NoSubscriberException
    }

    def "should throw exception when fail send messages to all subscribers"() {
        when: "send messages"
        broadcaster.broadcast(messages)

        then: "fetcher return list of subscribers"
        1 * fetcher.fetchSubscribers() >> subscribers

        and: "sender fail send messages to all subscribers"
        2 * eventSender.send(_,_) >> { throw new EventSendException("fail") }

        and: "broadcaster throw exception"
        thrown BroadcastOperationException
    }

    def "shouldn't throw exception when at least one subscriber receive messages"() {
        when: "send messages"
        broadcaster.broadcast(messages)

        then: "fetcher return list of subscribers"
        1 * fetcher.fetchSubscribers() >> subscribers

        and: "sender fail send messages to one of subscribers"
        1 * eventSender.send(subscribers[0], _) >> { throw new EventSendException("fail") }
        1 * eventSender.send(subscribers[1], _)

        and: "broadcaster finish processing without exception"
        notThrown Exception
    }
}
