package com.tubecentric.waldo.twitter.scheduler;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQueryFactory;
import com.tubecentric.waldo.twitter.entity.QTweetEntity;
import com.tubecentric.waldo.twitter.entity.TweetEntity;
import com.tubecentric.waldo.twitter.service.ITwitterEntityService;
import com.tubecentric.waldo.twitter.service.ITwitterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import twitter4j.Twitter;
import twitter4j.TwitterException;

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

        for(TweetEntity tweetEntity : getTweets()) {

            try {

                handleFavoriteTweet(tweetEntity);

                Thread.sleep(1000 * 10);

            } catch (TwitterException e) {

                switch (e.getErrorCode()) {
                    case 139:
                        handleAlreadyFavorite(tweetEntity);
                        break;
                    case 144:
                        handleTweetRemoved(tweetEntity);
                        break;
                    default:
                        log.error("Unable to favorite tweet.", e);
                }

            } catch (Exception e) {

                log.error("Error occurred favoriting tweet", e);
            }
        }
    }

    private void handleFavoriteTweet(TweetEntity tweetEntity) throws TwitterException {

        log.info("Favoring tweet: {}", tweetEntity.getTweetId());

        Twitter twitter = twitterService.getTwitter();
        twitter.createFavorite(tweetEntity.getTweetId());

        tweetEntity.setFavorited(true);
        entityManager.persist(tweetEntity);
    }

    private void handleAlreadyFavorite(TweetEntity tweetEntity) {

        log.warn("Tweet is already favorite: {}", tweetEntity.getTweetId());

        tweetEntity.setFavorited(true);
        entityManager.persist(tweetEntity);
    }

    private void handleTweetRemoved(TweetEntity tweetEntity) {

        log.warn("Tweet no longer exists: {}", tweetEntity.getTweetId());

        entityManager.remove(tweetEntity);
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
