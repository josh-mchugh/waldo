package com.tubecentric.waldo.twitter.integration;

import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway(defaultRequestChannel = "twitterChannel")
public interface TwitterGateway {

    void send(String command);
}
