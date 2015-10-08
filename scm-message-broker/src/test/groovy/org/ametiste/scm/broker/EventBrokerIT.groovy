package org.ametiste.scm.broker

import org.ametiste.scm.messaging.data.event.Event
import org.ametiste.scm.messaging.data.event.InstanceStartupEvent
import org.ametiste.scm.messaging.data.transport.TransportMessage
import org.ametiste.scm.messaging.receiver.EventReceivingController
import org.ametiste.scm.messaging.transport.http.dto.EventDTOMessage
import org.ametiste.scm.messaging.transport.http.dto.InstanceStartupEventDTO
import org.ametiste.scm.broker.boot.config.EventBrokerTestConfiguration
import org.ametiste.scm.broker.mock.EventSenderMock
import org.ametiste.scm.broker.mock.EventSubscribersFetcherMock
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.boot.test.WebIntegrationTest
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

import java.util.stream.Collectors

@ContextConfiguration(classes = EventBrokerTestConfiguration.class, loader = SpringApplicationContextLoader.class)
@WebIntegrationTest("server.port=0")
class EventBrokerIT extends Specification {

    @Autowired
    private EventReceivingController controller;

    @Autowired
    private EventSenderMock sender;

    @Autowired
    private EventSubscribersFetcherMock fetcher;

    def setup() {
        sender.clean()
    }

    def "event listener should process same number of messages as received"() {
        when: "receive messages"
        controller.receiveEvents(getTransportMessages())

        then: "wait for system process events"
        Thread.sleep(1000)

        and: "expect broker process same number of messages"
        getTransportMessages().size() == (int)sender.totalSendEvents() / fetcher.getNumberOfSubscribers()
    }


    private static Collection<TransportMessage<Event>> getTransportMessages() {
        InstanceStartupEvent[] events = [
            InstanceStartupEvent.builder().addInstanceId("APP1").addVersion("0.2.5").build(),
            InstanceStartupEvent.builder().addInstanceId("APP2").addVersion("0.3.1").build()
        ]
        return Arrays.stream(events).map({e -> new EventDTOMessage(new InstanceStartupEventDTO(e))}).collect(Collectors.toList());
    }
}
