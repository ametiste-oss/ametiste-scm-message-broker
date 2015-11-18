# SCM Message Broker

Link to umbrella project: [>> System's Configuration Management <<](https://github.com/ametiste-oss/ametiste-scm)

## Build Status
[![Build Status](https://travis-ci.org/ametiste-oss/ametiste-scm-message-broker.svg?branch=master)](https://travis-ci.org/ametiste-oss/ametiste-scm-message-broker)
[![Codacy Badge](https://api.codacy.com/project/badge/grade/7f40cf615e734819a291742e477b2543)](https://www.codacy.com/app/Ametiste-OSS/ametiste-scm-message-broker)
[![codecov.io](https://codecov.io/github/ametiste-oss/ametiste-scm-message-broker/coverage.svg?branch=master&precision=2)](https://codecov.io/github/ametiste-oss/ametiste-scm-message-broker?branch=master)

## Table Of Content

- [Overview](#overview)
- [Usage](#usage)
  - [Installation](#installation)
  - [Requirements](#requirements)
  - [Configuration properties](#configuration-properties)
- [Structure and processing logic](#structure-and-processing-logic)
  - [Transport events](#transport-events)
  - [Fetching subscribers](#fetching-subscribers)
  - [Message processing](#message-processing)
    - [Queue pipeline](#queue-pipeline)
    - [Broadcasting](#broadcasting)

## Overview

*SCM Message Broker* is a service that performs function of gathering messages from system service instances and broadcast it to subscribers. Service use SCM messaging and coordination components to communicate with external components of the system.

## Usage

Message Broker is Spring Boot application and disctributed as jar file. It placed in `jcenter()` repository:
```
http://jcenter.bintray.com/org/ametiste/scm/scm-message-broker/
```
Also releases stored on github or you can build artifact by itself from sources.

#### Installation

Installation process include next steps:

1. Get a ready-to-deploy jar file:
  1. download ready artifact from one of available sources;
  2. build artifact from sources:<br/>project based on Gradle, so to build execute next command from project root directory:<br/><code>gradle build</code>
2. Deploy jar file to the target server.
3. Configure properties for Broker (see [here](#configuration-properties)).
4. Start service by executing:<br/><code>java -jar dph-scm-message-broker-[version].jar</code>.

#### Requirements
- JDK 1.8 or higher.

#### Configuration properties

Application has few sets of properties separated by functional modules.

##### Common properties

|Name|Type|Description|Default|
|----|----|-----------|-------|
|`org.ametiste.scm.broker.group.timeout`|integer|Group expiration timeout (in milliseconds).<br/>Used to define maximum time to collect list of messages by aggregate.|`250`|
|`org.ametiste.scm.broker.group.threshold`|integer|Maximum group size. When group is filled aggregate push group<br/> and start gather next.|`200`|
|`org.ametiste.scm.broker.broadcast.timeout`|integer|Time delay between broadcasting operations (in milliseconds).|`30000`|

##### HttpClient properties

|Name|Type|Description|Default|
|----|----|-----------|-------|
|`org.ametiste.scm.sender.client.connect-timeout`|integer|Connection timeout for HTTP client (in milliseconds)|`1000`|
|`org.ametiste.scm.sender.client.read-timeout`|integer|Read timeout for HTTP client (in milliseconds)|`1000`|

*Note*: if parameters not defined default values will be used.

##### ActiveMQ properties
Service use ActiveMQ for storing queues of messages and has next properties:

|Name|Type|Description|Default|
|----|----|-----------|-------|
|`org.ametiste.scm.broker.amq.queue-name.raw-event`|string|Name of queue for raw events received from system instances.|`queue.raw`|
|`org.ametiste.scm.broker.amq.queue-name.aggregated-event`|string|Name of queue for aggregated lists of messages.|`queue.aggregated`|
|`org.ametiste.scm.broker.amq.broker-url`|URL|Configuration URL for ActiveMQ broker ([more info](http://activemq.apache.org/broker-uri.html)).|`vm://localhost`|
|`org.ametiste.scm.broker.amq.username`|string|Username for access to ActiveMQ broker.||
|`org.ametiste.scm.broker.amq.password`|string|Password for access to ActiveMQ broker.||
|`org.ametiste.scm.broker.amq.redelivery.maxRedeliveries`|integer|Number of redelivery retries.|`-1` (infinite redelivery)|
|`org.ametiste.scm.broker.amq.redelivery.initialDelay`|integer|Initial redelivery delay and increase step (if exponential backoff disabled (in milliseconds).|`1000`|
|`org.ametiste.scm.broker.amq.redelivery.maxDelay`|integer|Maximum value of redelivery delay (in milliseconds).|`30000`|

##### SCM Coordinator properties
Broker use SCM SubscribersFetcher configuration from Scm Coordinator Library and require define properties for it:

|Name|Type|Description|Default|
|----|----|-----------|-------|
|`spring.application.name`|string|Name of application.|`scm-message-broker`|
|`eureka.client.serviceUrl.defaultZone`|URL|URL to Eureka Server instance.<br/>:warning: *Important*: end URL with back slash symbol "/".|`http://localhost:8761/eureka/`|

You can provide and other spring cloud eureka properties for discovering client.

## Structure and processing logic

#### Transport events

For receiving and sending transport messages with event payload used [*SCM Messaging Library*](https://github.com/ametiste-oss/ametiste-scm-messaging).

#### Fetching subscribers

For fetching subscribers used *SCM Coordinator Library*. Read more about it [here](https://github.com/ametiste-oss/ametiste-scm-coordination).<br/>This library requires use SCM Coordinator Library for subscribing to event messages. Or interested service must register in Eureka server with required metadata independently.

#### Message processing

All messages received by broker published with *ApplicationEventPublisher*. In broker configuratuion (`EventProcessingConfiguration.class`) defines *@EventListener* that catch message and pass it to *EventMessageProcessor* interface.

##### Queue pipeline

All processing of messages defined as *Spring Integration Flow*:

![Message Broker Flow](https://cloud.githubusercontent.com/assets/11256858/10623471/fe8c4976-7797-11e5-8811-15fe41dca1dd.png)

Flow:
- EventListener pass message to  EventMessageProcessorGateway and store in JMS channel.
- After broadcasting timeout stored messages grep from channel and aggregated to packets of messages.
- Packets stores in other JMS channel (message driven).
- Service Activator grep packets from channel and send it to subscribers.

*Aggregator* group messages to packets by number that defined in property `com.dph.scm.broker.group.threshold`. If group not full and group timeout expired aggregator push partial packet to channel. It provides safe of messages in time when they present in service.

Jms channels backed with embedded ActiveMQ broker with persistence.

Service Activator execution is transnational and all messages rollback to queue if service failed to send it. It save messages in case when no one subscriber is present or service fail send packet to all subscribers.

##### Broadcasting

Broadcasting executed by implementation of MessageBroadcaster<Event> interface. This component uses fetcher and sender component for provide own logic.

Class diagram placed below:

![Broadcasting class diagram](https://cloud.githubusercontent.com/assets/11256858/10623521/562c195e-7798-11e5-9ced-59ee95ff7acd.png)
