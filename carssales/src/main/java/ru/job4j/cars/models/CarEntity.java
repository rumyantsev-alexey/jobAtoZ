package ru.job4j.cars.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Transient;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor
@Table(name = "cars")
public class CarEntity extends AbsProjectEntity {

    @Getter
    @Setter
    @ManyToOne(cascade = {CascadeType.REFRESH})
    private UsersEntity user;

    @Getter
    @Setter
    @ManyToOne(cascade = {CascadeType.REFRESH})
    private CityEntity city;

    @Getter
    @Setter
   @ManyToOne(cascade = {CascadeType.REFRESH})
    private MarkEntity mark;

    @Getter
    @Setter
    @ManyToOne(cascade = {CascadeType.REFRESH})
    private ModelEntity model;

    @Getter
    @Setter
    @Column(name = "price")
    private Integer price;

    @Getter
    @Setter
    @Column(name = "issue")
    private Integer issue;

    @Getter
    @Setter
    @Column(name = "dist")
    private Integer dist;

    @Getter
    @Setter
    @Column(name = "enginecapacity")
    private Integer enginecapacity;

    @Getter
    @Setter
    @Column(name = "power")
    private Integer power;

    @Getter
    @Setter
    @ManyToOne(cascade = {CascadeType.REFRESH})
    private TransmissionEntity trans;

    @Getter
    @Setter
    @ManyToOne(cascade = {CascadeType.REFRESH})
    private BodytypeEntity btype;

    @Getter
    @Setter
    @ManyToOne(cascade = {CascadeType.REFRESH})
    private EnginestypeEntity etype;

    @Getter
    @Setter
    @ManyToOne(cascade = {CascadeType.REFRESH})
    private DriveunitEntity dunit;

    @Getter
    @Setter
    @ManyToOne(cascade = {CascadeType.REFRESH})
    private WheelEntity wheel;

    @Getter
    @Setter
    @Column(name = "created")
    private Timestamp created;

    @Getter
    @Setter
    @Transient
    private String date;

    @Getter
    @Setter
    @Column(name = "old")
    private boolean old;

    @Getter
    @Setter
    @Column(name = "fotos")
    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    private Set<FotoEntity> fotos = new HashSet<>();

    public CarEntity(final String note) {
        super.setName(note);
        this.old = false;
        this.created = new Timestamp(System.currentTimeMillis());
    }

    public String getNote() {
        return super.getName();
    }

    public void setNote(String note) {
        super.setName(note);
    }

}
