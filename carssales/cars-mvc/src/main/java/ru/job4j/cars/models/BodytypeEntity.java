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
@Table(name = "bodytype")
public class BodytypeEntity extends AbsProjectEntity {

    @Getter
    @Setter
    @OneToMany(mappedBy = "btype", fetch = FetchType.EAGER)
    @Column(name = "car_id")
    private Set<CarEntity> car = new HashSet<>();

    public BodytypeEntity(String name) {
        super.setName(name);
    }
}
