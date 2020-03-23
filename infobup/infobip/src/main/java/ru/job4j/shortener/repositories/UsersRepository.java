package ru.job4j.shortener.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.job4j.shortener.models.User;

@Repository
public interface UsersRepository extends JpaRepository<User, Long> {
    User findByName(String name);
}
