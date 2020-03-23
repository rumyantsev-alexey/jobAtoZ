package ru.job4j.shortener.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import javax.persistence.*;

@Entity
@Table
@Data
@NoArgsConstructor
public class Link {

    @Id
    @GeneratedValue
    private long id;

    @Column
    private String source;

    @Column(unique = true)
    private String result;

    @Column
    @Enumerated(EnumType.STRING)
    private HttpStatus status;

    @Column
    private int count;

    @ManyToOne (cascade = {CascadeType.REFRESH})
    private User user;

}
