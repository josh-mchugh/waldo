package com.tubecentric.waldo.twitter.integration;

import com.google.common.base.Joiner;
import com.tubecentric.waldo.twitter.model.TwitterDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;

@Slf4j
@MessageEndpoint
public class TwitterServiceActivator {

    @ServiceActivator(inputChannel = "twitterChannel")
    public void processMessage(Message<TwitterDTO> message) {

        log.info("Message: {}, Headers: {}", message.getPayload(), message.getHeaders().toString());


        log.info("=======================================================");
        log.info("Url: {}", message.getPayload().getUrl());
        log.info("Username: {}", message.getPayload().getUsername());
        log.info("Description: {}", message.getPayload().getDescription());
        log.info("Message: {}", message.getPayload().getTweets().get(0).getMessage());
        log.info("Hashtags: {}", Joiner.on(",").join(message.getPayload().getTweets().get(0).getHashtags()));
        log.info("Is Retweet: {}", message.getPayload().getTweets().get(0).isRetweet());
        log.info("=======================================================");
    }
}
