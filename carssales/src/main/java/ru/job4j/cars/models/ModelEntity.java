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
@Table(name = "model")
public class ModelEntity extends AbsProjectEntity {

    @Getter
    @Setter
    @ManyToOne (cascade = {CascadeType.REFRESH})
    private MarkEntity mark;

    @Getter
    @Setter
    @Column(name = "car")
    @OneToMany(mappedBy = "model", fetch = FetchType.EAGER)
    private Set<CarEntity> car = new HashSet<>();

    public ModelEntity(String name, MarkEntity mark) {
        super.setName(name);
        this.mark = mark;
    }

}
