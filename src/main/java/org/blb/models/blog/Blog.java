package org.blb.models.blog;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.blb.models.region.Region;
import org.blb.models.user.User;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Blog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    @Column(columnDefinition = "TEXT")
    private String content;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User author;
    private LocalDate publishedDate;
    private LocalDateTime editedDate;
    private Integer views;
    private Integer comments;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "region_id")
    private Region region;

    public Blog(String title, String content, User author,  int views, int comments, Region region) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.publishedDate = LocalDate.now();
        this.editedDate = LocalDateTime.now();
        this.views = views;
        this.comments = comments;
        this.region= region;
    }
}
