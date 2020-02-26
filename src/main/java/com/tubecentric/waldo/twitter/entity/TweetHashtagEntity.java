package com.tubecentric.waldo.twitter.entity;

import com.tubecentric.waldo.framework.entity.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@Table(name = "tweet_hashtag")
public class TweetHashtagEntity extends AbstractEntity {

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "tweet_id", nullable = false)
    private TweetEntity tweet;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "hashtag_id", nullable = false)
    private HashtagEntity hashtag;

    public TweetHashtagEntity(TweetEntity tweet, HashtagEntity hashtag) {

        this.tweet = tweet;
        this.hashtag = hashtag;
    }
}
