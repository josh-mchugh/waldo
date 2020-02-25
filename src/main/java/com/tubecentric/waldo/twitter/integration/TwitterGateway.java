package com.tubecentric.waldo.twitter.integration;

import com.tubecentric.waldo.twitter.model.TwitterDTO;
import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway(defaultRequestChannel = "twitterChannel")
public interface TwitterGateway {

    void send(TwitterDTO twitterDTO);
}
