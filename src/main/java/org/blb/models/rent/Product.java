package org.blb.models.rent;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.blb.models.region.Region;
import org.blb.models.user.User;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "product")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    @JsonBackReference
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Category category;
    private Double price;
    private Boolean isInStock;

    @Column(length = 500)
    private String description;

    @Column(length = 1000)
    private String link;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "region_id", referencedColumnName = "id")
    @JsonBackReference
    private Region region;

    public Product(String name, Category category, Double price, Boolean isInStock) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.isInStock = isInStock;
    }
}
