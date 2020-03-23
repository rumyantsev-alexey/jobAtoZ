package ru.job4j.cars.models;

import lombok.*;

import javax.persistence.*;

@MappedSuperclass
@EqualsAndHashCode(exclude = {"id"})
public abstract class AbsProjectEntity {

    @Id
    @GeneratedValue (strategy = GenerationType.SEQUENCE)
    @Getter
    private int id;

    @Getter
    @Setter
    @NonNull
    @Column(name = "name")
    private String name;
}
