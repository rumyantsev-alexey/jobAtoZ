package ru.job4j.cars.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Table(name = "fotos")
public class FotoEntity extends AbsProjectEntity {

    @Getter
    @Setter
    @Column(name = "foto")
    @Lob
    private byte[] foto;

    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.EAGER)
    private CarEntity car;
}
