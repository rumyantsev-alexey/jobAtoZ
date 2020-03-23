package ru.job4j.cars.services;

import org.springframework.beans.factory.annotation.Autowired;
import ru.job4j.cars.models.AbsProjectEntity;
import ru.job4j.cars.repositories.MyRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


class CommonMethodForService<K extends AbsProjectEntity> {

    @Autowired
    private MyRepository<K> repo;

    public int add(K model) {
        return repo.save(model).getId();
    }

    public boolean update(K model) {
        int id = model.getId();
        repo.deleteById(id);
        repo.save(model);
        return true;
    }

    public boolean delete(int id) {
        repo.deleteById(id);
        return true;
    }

    public List<K> findAll() {
        return StreamSupport.stream(repo.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public K findById(int id) {
        return repo.findById(id).orElse(null);
    }

    public int findIdByModel(K model) {
        int result = 0;
        for (K ent: repo.findAll()) {
            if (model.equals(ent)) {
                result = ent.getId();
                break;
            }
        }
        return result;
    }
    public int findIdByName(String name) {
        int result = 0;
        for (K ent: repo.findAll()) {
            if (name.equals(ent.getName())) {
                result = ent.getId();
                break;
            }
        }
        return result;
    }

    public  K findByName(String name) {
        K result = null;
        int i = findIdByName(name);
        if (i > 0) {
            result = repo.findById(i).get();
        }
        return result;
    }

}
