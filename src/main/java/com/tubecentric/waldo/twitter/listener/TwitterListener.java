package com.tubecentric.waldo.twitter.listener;

import com.google.common.collect.Lists;
import com.tubecentric.waldo.framework.TwitterConfig;
import com.tubecentric.waldo.twitter.model.TweetDTO;
import com.tubecentric.waldo.twitter.model.TwitterDTO;
import com.tubecentric.waldo.twitter.service.ITwitterEntityService;
import com.tubecentric.waldo.utils.DateUtils;
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
public class TwitterListener {

    private final TwitterConfig twitterConfig;
    private final ITwitterEntityService twitterEntityService;

    @EventListener(ApplicationReadyEvent.class)
    public void doSomethingAfterStartup() {

        ConfigurationBuilder cb = new ConfigurationBuilder()
                .setDebugEnabled(true)
                .setOAuthConsumerKey(twitterConfig.getKey())
                .setOAuthConsumerSecret(twitterConfig.getSecret())
                .setOAuthAccessToken(twitterConfig.getAccessToken())
                .setOAuthAccessTokenSecret(twitterConfig.getAccessTokenSecret());

        TwitterStream twitterStream = new TwitterStreamFactory(cb.build()).getInstance();
        twitterStream.addListener(getTwitterListener());

        twitterStream.sample();
        twitterStream.filter(new FilterQuery().track("#smallyoutuber", "#SmallYouTuberArmy", "#youtubers", "#smallyoutubecommunity").language("en"));
    }

    private StatusListener getTwitterListener() {

        return new StatusListener() {

            public void onStatus(Status status) {

                log.info("=======================================================");
                log.info("Username: {}", status.getUser().getName());
                log.info("Screen name: {}", status.getUser().getScreenName());
                log.info("Url: {}", status.getUser().getURL());
                log.info("Description: {}", status.getUser().getDescription());
                log.info("Message: {}", status.getText());
                log.info("Retweet: {}", status.isRetweet());
                log.info("=======================================================");

                if(!status.isRetweet()) {

                    TwitterDTO twitterDTO = TwitterDTO.builder()
                            .twitterId(status.getUser().getId())
                            .username(status.getUser().getName())
                            .screenName(status.getUser().getScreenName())
                            .description(status.getUser().getDescription())
                            .url(status.getUser().getURL())
                            .followers((long) status.getUser().getFollowersCount())
                            .tweet(TweetDTO.builder()
                                    .tweetId(status.getId())
                                    .tweetDate(DateUtils.toLocalDateTime(status.getCreatedAt()))
                                    .message(status.getText())
                                    .hashtags(Lists.newArrayList(status.getHashtagEntities()).stream()
                                            .map(HashtagEntity::getText).collect(Collectors.toList())
                                    )
                                    .build()
                            )
                            .build();

                    twitterEntityService.saveTweet(twitterDTO);
                }
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
    }
}
