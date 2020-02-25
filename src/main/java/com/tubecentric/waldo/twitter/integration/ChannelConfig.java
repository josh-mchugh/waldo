package com.tubecentric.waldo.twitter.integration;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;

@Component
public class ChannelConfig {

    @Bean
    public MessageChannel twitterChannel() {

        return new QueueChannel();
    }

    @Bean(name = PollerMetadata.DEFAULT_POLLER)
    public PollerMetadata poller() {

        return Pollers.fixedDelay(1000).get();
    }
}
