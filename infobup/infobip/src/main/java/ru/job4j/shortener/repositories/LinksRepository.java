package ru.job4j.shortener.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.job4j.shortener.models.Link;

@Repository
public interface LinksRepository extends JpaRepository<Link, Long> {
    Link findByResult(String result);
}
