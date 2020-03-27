package ru.job4j.shortener.models;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue()
    private long id;

    @Column(unique = true)
    private String name;

    @Column
    private String password;

    @Column
    @OneToMany(mappedBy = "user")
    private List<Link> links;

    public User(String username) {
        this.name = username;
    }
}
