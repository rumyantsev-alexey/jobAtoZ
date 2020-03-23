package ru.job4j.cars.repositories;

import org.springframework.stereotype.Repository;
import ru.job4j.cars.models.TransmissionEntity;

@Repository
public interface TransmissionRepository extends MyRepository<TransmissionEntity> {
}
