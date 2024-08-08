package org.blb.models.team;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "team")
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String role;
    private String description;
    private String photoUrl;
    private String url;

    public Team(String name, String role, String description, String photoUrl, String url) {
        this.name = name;
        this.role = role;
        this.description = description;
        this.photoUrl = photoUrl;
        this.url = url;
    }
}
