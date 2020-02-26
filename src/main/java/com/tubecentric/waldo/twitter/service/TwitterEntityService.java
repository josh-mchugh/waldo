package com.tubecentric.waldo.twitter.service;

import com.querydsl.jpa.JPQLQueryFactory;
import com.tubecentric.waldo.twitter.entity.HashtagEntity;
import com.tubecentric.waldo.twitter.entity.QTwitterEntity;
import com.tubecentric.waldo.twitter.entity.TweetEntity;
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
        List<HashtagEntity> hashtagEntities = twitterDTO.getTweet().getHashtags().stream()
                .map(hashtag -> new HashtagEntity(tweetEntity, hashtag))
                .collect(Collectors.toList());

        tweetEntity.setHashtags(hashtagEntities);

        // Save Tweet to Twitter account
        twitterEntity.getTweets().add(tweetEntity);
        entityManager.persist(twitterEntity);

        return twitterEntity;
    }

    private TwitterEntity getOrCreate(TwitterDTO twitterDTO) {

        TwitterEntity twitterEntity = null;

        if(existsTWitter(twitterDTO.getTwitterId())) {

            twitterEntity = getTwitterEntity(twitterDTO.getTwitterId());

        } else {

            twitterEntity = new TwitterEntity();
            twitterEntity.setTwitterId(twitterDTO.getTwitterId());
            twitterEntity.setUsername(twitterDTO.getUsername());
            twitterEntity.setScreenName(twitterDTO.getScreenName());
            twitterEntity.setDescription(twitterDTO.getDescription());
            twitterEntity.setUrl(twitterDTO.getUrl());
            twitterEntity.setFollowers(twitterDTO.getFollowers());
            twitterEntity.setTweets(new ArrayList<>());
        }

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
}
