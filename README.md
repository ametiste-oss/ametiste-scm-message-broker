# SCM Message Broker

## Build Status
[![Build Status](https://travis-ci.org/ametiste-oss/ametiste-scm-message-broker.svg?branch=master)](https://travis-ci.org/ametiste-oss/ametiste-scm-message-broker)
[![codecov.io](https://codecov.io/github/ametiste-oss/ametiste-scm-message-broker/coverage.svg?branch=master&precision=2)](https://codecov.io/github/ametiste-oss/ametiste-scm-message-broker?branch=master)

## Table Of Content

- [Overview](#overview)
- [Usage](#usage)
  - [Artifact](#artifact)
  - [Installation](#installation)
  - [Requirements](#requirements)
  - [Configuration properties](#configuration-properties)
- [Structure and processing logic](#structure-and-processing-logic)
  - [Transport events](#transport-events)
  - [Fetching subscribers](#fetching-subscribers)
  - [Processing messages](#processing-messages)
    - [Queue pipeline](#queue-pipeline)
    - [Broadcasting](#broadcasting)

## Overview

*SCM Message Broker* is a service that performs function of gathering messages from system service instances and broadcast it to subscribers. Service use SCM messaging and coordination components to communicate with external components of the system.

## Usage

#### Artifact

Message Broker is Spring Boot application and distributed as jar file. It placed in `jcenter()` repository:
```
http://jcenter.bintray.com/org/ametiste/scm/scm-message-broker/
```
Also release store on github or you can build artifact itself from sources.

#### Installation

***TODO***

#### Requirements
- JDK 1.8 or higher.

#### Configuration properties

***TODO***

## Structure and processing logic

#### Transport events

For receiving and sending transport messages with event payload used [*SCM Messaging Library*](https://github.com/ametiste-oss/ametiste-scm-messaging).

#### Fetching subscribers

For fetching subscribers used SCM Coordinator Library. Read more about it here.
This library requires use SCM Coordinator Library for subscribing to event messages. Or interested service must register in Eureka server with required metadata independently.

#### Processing messages

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
