package com.tubecentric.waldo.twitter.integration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;

@Slf4j
@MessageEndpoint
public class TwitterServiceActivator {

    @ServiceActivator(inputChannel = "twitterChannel")
    public void processMessage(Message<String> message) {

        log.info("Message: {}, Headers: {}", message.getPayload(), message.getHeaders().toString());
    }
}
