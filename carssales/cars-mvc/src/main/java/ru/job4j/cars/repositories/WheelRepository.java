package ru.job4j.cars.repositories;

import org.springframework.stereotype.Repository;
import ru.job4j.cars.models.WheelEntity;

@Repository
public interface WheelRepository extends MyRepository<WheelEntity> {
}
