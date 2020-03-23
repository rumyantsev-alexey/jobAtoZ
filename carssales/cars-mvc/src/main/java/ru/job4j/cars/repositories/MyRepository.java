package ru.job4j.cars.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.models.AbsProjectEntity;

@Repository
public interface MyRepository<T extends AbsProjectEntity> extends CrudRepository<T, Integer> {

}
