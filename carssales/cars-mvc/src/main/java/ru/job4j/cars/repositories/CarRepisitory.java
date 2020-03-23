package ru.job4j.cars.repositories;

import org.springframework.stereotype.Repository;
import ru.job4j.cars.models.CarEntity;

@Repository
public interface CarRepisitory extends MyRepository<CarEntity> {
}
