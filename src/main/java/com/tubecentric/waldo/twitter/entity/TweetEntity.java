package com.tubecentric.waldo.twitter.entity;

import com.tubecentric.waldo.framework.entity.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@Table(name = "tweet")
public class TweetEntity extends AbstractEntity {

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "twitter_id", nullable = false)
    private TwitterEntity twitter;

    @Basic
    @Column(name = "tweet_id", nullable = false, unique = true)
    private String tweetId;

    @Basic
    @Column(name = "message", nullable = false)
    private String message;
}
