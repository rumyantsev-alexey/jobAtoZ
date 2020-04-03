package ru.job4j.cars.repositories;

import org.springframework.stereotype.Repository;
import ru.job4j.cars.models.CityEntity;

@Repository
public interface CityRepository extends MyRepository<CityEntity> {
}
