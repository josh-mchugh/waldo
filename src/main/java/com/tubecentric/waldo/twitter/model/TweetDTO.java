package com.tubecentric.waldo.twitter.model;

import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.List;

@Value
@Builder(builderClassName = "Builder")
public class TweetDTO {

    private final Long tweetId;
    private final String message;
    private final LocalDateTime tweetDate;
    private final boolean isRetweet;

    @Singular
    private final List<String> hashtags;
}
