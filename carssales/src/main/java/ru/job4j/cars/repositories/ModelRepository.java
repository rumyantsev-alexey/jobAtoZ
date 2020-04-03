package ru.job4j.cars.repositories;

import org.springframework.stereotype.Repository;
import ru.job4j.cars.models.ModelEntity;

@Repository
public interface ModelRepository extends MyRepository<ModelEntity> {
}
