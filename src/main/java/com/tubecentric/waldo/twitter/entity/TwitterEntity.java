package com.tubecentric.waldo.twitter.entity;

import com.tubecentric.waldo.framework.entity.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "twitter")
public class TwitterEntity extends AbstractEntity {

    @Basic
    @Column(name = "twitter_id", unique = true, nullable = false)
    private String twitterId;

    @Basic
    @Column(name = "username", nullable = false)
    private String username;

    @Basic
    @Column(name = "screen_name", nullable = false)
    private String screenName;

    @Basic
    @Column(name = "description")
    private String description;

    @Basic
    @Column(name = "url")
    private String url;

    @Basic
    @Column(name = "followers")
    private Long followers;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "twitter")
    private List<TweetEntity> tweets;
}
