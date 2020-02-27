package com.tubecentric.waldo.twitter.scheduler;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQueryFactory;
import com.tubecentric.waldo.framework.TwitterConfig;
import com.tubecentric.waldo.twitter.entity.QTweetEntity;
import com.tubecentric.waldo.twitter.entity.TweetEntity;
import com.tubecentric.waldo.twitter.service.ITwitterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@Transactional
@RequiredArgsConstructor
public class TwitterFavoritedScheduler {

    private final ITwitterService twitterService;
    private final JPQLQueryFactory queryFactory;
    private final EntityManager entityManager;

    @Scheduled(fixedDelay = 60000)
    public void FavoriteTweets() {

        log.info("Retrieve tweets to be favorites.");

        Twitter twitter = twitterService.getTwitter();

        for(TweetEntity tweet : getTweets()) {

            try {

                log.info("Favoring tweet: {}", tweet.getTweetId());
                twitter.createFavorite(tweet.getTweetId());

                tweet.setFavorited(true);
                entityManager.persist(tweet);

                Thread.sleep(1000 * 10);

            } catch (TwitterException e) {

                if(e.getErrorCode() == 139) {

                    log.warn("Tweet is already favorite: {}", tweet.getTweetId());

                    tweet.setFavorited(true);
                    entityManager.persist(tweet);

                }else if(e.getErrorCode() == 144) {

                    log.warn("Tweet no longer exists: {}", tweet.getTweetId());

                    entityManager.remove(tweet);

                } else {

                    log.error(String.format("Unable to favorite tweet: %s", e.getErrorCode()), e);
                }

            } catch (Exception e) {

                log.error("Unable to favorite tweet", e);
            }
        }
    }

    private List<TweetEntity> getTweets() {

        QTweetEntity qTweet = QTweetEntity.tweetEntity;

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qTweet.favorited.isFalse());
        builder.and(qTweet.createdDate.before(LocalDateTime.now().minusMinutes(RandomUtils.nextInt(5, 8))));
        builder.and(qTweet.twitter.url.containsIgnoreCase("youtube"));

        return queryFactory.selectFrom(qTweet)
                .where(builder)
                .fetch();
    }
}
