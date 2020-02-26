package com.tubecentric.waldo.twitter.model;

import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import java.util.List;

@Value
@Builder(builderClassName = "Builder")
public class TwitterDTO {

    private final Long twitterId;
    private final String username;
    private final String screenName;
    private final String description;
    private final String url;
    private final Long followers;
    private final TweetDTO tweet;
}
