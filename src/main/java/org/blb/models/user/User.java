package org.blb.models.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "`user`")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String password;
    private String email;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    private Role role;
    private State state;
    private String code;
    private String newPassword;
    public User(String name, String password, String email,  Role role, State state, String code) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.role = role;
        this.state = state;
        this.code = code;
        this.newPassword = "";
    }




}
