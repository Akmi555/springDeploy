package org.blb.models.news;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.blb.models.user.User;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewsComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String comment;

    private LocalDateTime commentDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="author_id")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="news_id")
    private NewsDataEntity newsDataEntity;

    public NewsComment(String comment, LocalDateTime commentDate, User user, NewsDataEntity newsDataEntity) {
        this.comment = comment;
        this.commentDate = commentDate;
        this.user = user;
        this.newsDataEntity = newsDataEntity;
    }
}
