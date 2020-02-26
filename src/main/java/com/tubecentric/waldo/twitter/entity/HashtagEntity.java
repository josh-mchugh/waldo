package com.tubecentric.waldo.twitter.entity;

import com.tubecentric.waldo.framework.entity.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name="hashtag")
public class HashtagEntity extends AbstractEntity {

    @Basic
    @Column(name = "hashtag", nullable = false)
    private String hashtag;

    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true, mappedBy = "hashtag")
    private List<TweetHashtagEntity> tweetHashtag;
}
