package com.tubecentric.waldo.twitter;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.tubecentric.waldo.framework.TwitterConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import twitter4j.FilterQuery;
import twitter4j.HashtagEntity;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class TwitterService {

    private final TwitterConfig twitterConfig;

    @EventListener(ApplicationReadyEvent.class)
    public void doSomethingAfterStartup() {

        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(twitterConfig.getKey())
                .setOAuthConsumerSecret(twitterConfig.getSecret())
                .setOAuthAccessToken(twitterConfig.getAccessToken())
                .setOAuthAccessTokenSecret(twitterConfig.getAccessTokenSecret());


        StatusListener listener = new StatusListener(){

            public void onStatus(Status status) {

                log.info("=======================================================");
                log.info("Url: {}", status.getUser().getURL());
                log.info("Username: {}", status.getUser().getName());
                log.info("Description: {}", status.getUser().getDescription());
                log.info("Message: {}", status.getText());
                log.info("Hashtags: {}", Joiner.on(",").join(Lists.newArrayList(status.getHashtagEntities()).stream().map(HashtagEntity::getText).collect(Collectors.toList())));
                log.info("Is Retweet: {}", status.isRetweet());
                log.info("=======================================================");
            }

            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {

                log.info("On Deletion Notice: {}", statusDeletionNotice);
            }

            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {

                log.info("On Limited status notice: {}", numberOfLimitedStatuses);
            }

            @Override
            public void onScrubGeo(long userId, long upToStatusId) {

                log.info("userId: {}, {}", userId, upToStatusId);
            }

            @Override
            public void onStallWarning(StallWarning stallWarning) {

                log.warn("Twitter Stall Warning: {}", stallWarning);
            }

            public void onException(Exception ex) {

                log.error("Unable to process status", ex);
            }
        };

        TwitterStream twitterStream = new TwitterStreamFactory(cb.build()).getInstance();
        twitterStream.addListener(listener);

        // sample() method internally creates a thread which manipulates TwitterStream and calls these adequate listener methods continuously.
        twitterStream.sample();
        twitterStream.filter(new FilterQuery().track("#smallyoutuber", "#SmallYouTuberArmy", "#youtubers", "#smallyoutubecommunity").language("en"));
    }
}
