package ru.job4j.cars.repositories;

import org.springframework.stereotype.Repository;
import ru.job4j.cars.models.MarkEntity;

@Repository
public interface MarkRepository extends MyRepository<MarkEntity> {
}
