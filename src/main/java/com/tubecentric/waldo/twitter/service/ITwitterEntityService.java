package com.tubecentric.waldo.twitter.service;

import com.tubecentric.waldo.twitter.entity.TwitterEntity;
import com.tubecentric.waldo.twitter.model.TwitterDTO;

public interface ITwitterEntityService {

    TwitterEntity saveTweet(TwitterDTO twitterDTO);
}
