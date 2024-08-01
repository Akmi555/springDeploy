package org.blb.repository.news;

import org.blb.models.news.NewsDataEntity;
import org.blb.models.news.NewsReaction;
import org.blb.models.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NewsReactionRepository extends JpaRepository<NewsReaction, Long> {

    Optional<NewsReaction> findByNewsDataAndUser(NewsDataEntity newsData, User user);
}