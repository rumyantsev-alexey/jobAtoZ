package ru.job4j.cars.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor
@Table(name = "mark")
public class MarkEntity extends AbsProjectEntity {

    @Getter
    @Setter
    @Column(name = "model")
    @OneToMany(mappedBy = "mark", fetch = FetchType.EAGER)
    private Set<ModelEntity> model = new HashSet<>();

    @Getter
    @Setter
    @Column(name = "car")
    @OneToMany(mappedBy = "mark", fetch = FetchType.EAGER)
    private Set<CarEntity> car = new HashSet<>();

    public MarkEntity(String name) {
        super.setName(name);
    }

}
