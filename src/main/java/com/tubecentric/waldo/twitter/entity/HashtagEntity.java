package com.tubecentric.waldo.twitter.entity;

import com.tubecentric.waldo.framework.entity.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name="hashtag")
public class HashtagEntity extends AbstractEntity {

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "tweet_id", nullable = false)
    private TweetEntity tweet;

    @Basic
    @Column(name = "hashtag", nullable = false)
    private String hashtag;
}
