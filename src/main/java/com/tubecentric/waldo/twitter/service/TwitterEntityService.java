package com.tubecentric.waldo.twitter.service;

import com.querydsl.jpa.JPQLQueryFactory;
import com.tubecentric.waldo.twitter.entity.HashtagEntity;
import com.tubecentric.waldo.twitter.entity.QHashtagEntity;
import com.tubecentric.waldo.twitter.entity.QTwitterEntity;
import com.tubecentric.waldo.twitter.entity.TweetEntity;
import com.tubecentric.waldo.twitter.entity.TweetHashtagEntity;
import com.tubecentric.waldo.twitter.entity.TwitterEntity;
import com.tubecentric.waldo.twitter.model.TwitterDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@Transactional
@RequiredArgsConstructor
public class TwitterEntityService implements ITwitterEntityService {

    private final JPQLQueryFactory queryFactory;
    private final EntityManager entityManager;

    @Override
    public TwitterEntity saveTweet(TwitterDTO twitterDTO) {

        // Get or Create Twitter Entity
        TwitterEntity twitterEntity = getOrCreate(twitterDTO);

        // Update the twitter follower count
        twitterEntity.setFollowers(twitterDTO.getFollowers());

        // Add Tweet to twitter
        TweetEntity tweetEntity = new TweetEntity();
        tweetEntity.setTwitter(twitterEntity);
        tweetEntity.setTweetId(twitterDTO.getTweet().getTweetId());
        tweetEntity.setTweetDate(twitterDTO.getTweet().getTweetDate());
        tweetEntity.setMessage(twitterDTO.getTweet().getMessage());
        tweetEntity.setFavorited(false);

        // Build hashtag list for tweet
        List<TweetHashtagEntity> tweetHashtagEntities = twitterDTO.getTweet().getHashtags().stream()
                .map(this::getOrCreate)
                .map(hashtagEntity -> new TweetHashtagEntity(tweetEntity, hashtagEntity))
                .collect(Collectors.toList());

        tweetEntity.setTweetHashtags(tweetHashtagEntities);

        // Save Tweet to Twitter account
        twitterEntity.getTweets().add(tweetEntity);
        entityManager.persist(twitterEntity);

        return twitterEntity;
    }

    private TwitterEntity getOrCreate(TwitterDTO twitterDTO) {

        if(existsTWitter(twitterDTO.getTwitterId())) {

            return getTwitterEntity(twitterDTO.getTwitterId());
        }

        TwitterEntity twitterEntity = new TwitterEntity();
        twitterEntity.setTwitterId(twitterDTO.getTwitterId());
        twitterEntity.setUsername(twitterDTO.getUsername());
        twitterEntity.setScreenName(twitterDTO.getScreenName());
        twitterEntity.setDescription(twitterDTO.getDescription());
        twitterEntity.setUrl(twitterDTO.getUrl());
        twitterEntity.setFollowers(twitterDTO.getFollowers());
        twitterEntity.setTweets(new ArrayList<>());

        return twitterEntity;
    }

    private TwitterEntity getTwitterEntity(Long twitterId) {

        QTwitterEntity qTwitterEntity = QTwitterEntity.twitterEntity;

        return queryFactory.selectFrom(qTwitterEntity)
                .where(qTwitterEntity.twitterId.eq(twitterId))
                .fetchOne();
    }

    private boolean existsTWitter(Long twitterId) {

        QTwitterEntity qTwitterEntity = QTwitterEntity.twitterEntity;

        long count = queryFactory.select(qTwitterEntity.id)
                .from(qTwitterEntity)
                .where(qTwitterEntity.twitterId.eq(twitterId))
                .fetchCount();

        return count >= 1;
    }

    private HashtagEntity getOrCreate(String hashtag) {

        if(existsHashtag(hashtag)) {

            return getHashTag(hashtag);
        }

        HashtagEntity hashtagEntity = new HashtagEntity();
        hashtagEntity.setHashtag(hashtag);

        return hashtagEntity;
    }

    private HashtagEntity getHashTag(String hashtag) {

        QHashtagEntity qHashtag = QHashtagEntity.hashtagEntity;

        return queryFactory.selectFrom(qHashtag)
                .where(qHashtag.hashtag.equalsIgnoreCase(hashtag))
                .fetchOne();
    }

    private boolean existsHashtag(String hashtag) {

        QHashtagEntity qHashtag = QHashtagEntity.hashtagEntity;

        long count = queryFactory.select(qHashtag.id)
                .from(qHashtag)
                .where(qHashtag.hashtag.equalsIgnoreCase(hashtag))
                .fetchCount();

        return count >= 1;
    }
}
