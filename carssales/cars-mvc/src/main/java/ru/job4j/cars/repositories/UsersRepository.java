package ru.job4j.cars.repositories;

import org.springframework.stereotype.Repository;
import ru.job4j.cars.models.UsersEntity;

@Repository
public interface UsersRepository extends MyRepository<UsersEntity> {
}
