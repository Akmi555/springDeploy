package org.blb.models.news;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.blb.models.region.Region;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "news")
public class NewsDataEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "region_id", referencedColumnName = "id")
    private Region region = new Region();

    private String sectionName;
    private String title;
    private String date;
    private String titleImageSquare;
    private String titleImageWide;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private int likeCount = 0;

    @Column(nullable = false)
    private int dislikeCount = 0;

    @Column(nullable = false, name = "comments_count")
    private int commentsCount = 0;
}