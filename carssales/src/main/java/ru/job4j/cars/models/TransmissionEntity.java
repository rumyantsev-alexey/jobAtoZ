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
@Table(name = "transmission")
public class TransmissionEntity extends AbsProjectEntity {

    @Getter
    @Setter
    @Column(name = "car")
    @OneToMany(mappedBy = "trans", fetch = FetchType.EAGER)
    private Set<CarEntity> car = new HashSet<>();

    public TransmissionEntity(String name) {
        super.setName(name);
    }

}
