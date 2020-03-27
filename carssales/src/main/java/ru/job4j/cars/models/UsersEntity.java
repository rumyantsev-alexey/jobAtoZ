package ru.job4j.cars.models;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.HashSet;
import java.util.Set;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"email", "city", "car"}, callSuper = true)
@Table(name = "users")
public class UsersEntity extends AbsProjectEntity {

    @Getter
    @Setter
    @NonNull
    @Column(name = "password")
    private String password;

    @Getter
    @Setter
    @Email
    @Column(name = "email")
    private String email;

    @Getter
    @Setter
    @ManyToOne(cascade = {CascadeType.REFRESH})
    private CityEntity city;

    @Getter
    @Setter
    @Column(name = "car")
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private Set<CarEntity> car = new HashSet<>();

    public UsersEntity(String name, String password, String email) {
        super.setName(name);
        this.password = password;
        this.email = email;
    }

}
