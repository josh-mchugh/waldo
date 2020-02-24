package com.tubecentric.waldo.twitter.entity;

import com.tubecentric.waldo.framework.entity.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

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
    private Long tweetId;

    @Basic
    @Column(name = "message", nullable = false)
    private String message;

    @Basic
    @Column(name = "tweet_date", nullable = false)
    private LocalDateTime tweetDate;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "tweet")
    private List<HashtagEntity> hashtags;
}
