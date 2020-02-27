package com.tubecentric.waldo.twitter.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(builderClassName = "Builder")
public class TwitterDTO {

    private final Long twitterId;
    private final String username;
    private final String screenName;
    private final String description;
    private final String url;
    private final Integer followers;
    private final boolean favorited;
    private final TweetDTO tweet;
}
