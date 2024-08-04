package org.blb.repository.news;

import org.blb.models.news.NewsSection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsSectionRepository extends JpaRepository<NewsSection, Long> {
   List<NewsSection> findAll();
}
