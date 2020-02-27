package com.tubecentric.waldo.twitter.service;

import com.tubecentric.waldo.framework.TwitterConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

@Slf4j
@Component
@RequiredArgsConstructor
public class TwitterService implements ITwitterService {

    private final TwitterConfig twitterConfig;
    private Twitter twitter;

    @Override
    public Twitter getTwitter() {

        if(twitter == null) {

            TwitterFactory tf = new TwitterFactory(getConfiguration());
            twitter = tf.getInstance();
        }

        return twitter;
    }

    private Configuration getConfiguration() {

        return new ConfigurationBuilder()
                .setDebugEnabled(true)
                .setOAuthConsumerKey(twitterConfig.getKey())
                .setOAuthConsumerSecret(twitterConfig.getSecret())
                .setOAuthAccessToken(twitterConfig.getAccessToken())
                .setOAuthAccessTokenSecret(twitterConfig.getAccessTokenSecret())
                .build();
    }
}
