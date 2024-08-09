package org.blb.models.news;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.blb.models.user.User;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    @JoinColumn(name="author_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    @JoinColumn(name="news_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private NewsDataEntity newsDataEntity;

    public NewsComment(String comment, LocalDateTime commentDate, User user, NewsDataEntity newsDataEntity) {
        this.comment = comment;
        this.commentDate = commentDate;
        this.user = user;
        this.newsDataEntity = newsDataEntity;
    }
}
