package ru.job4j.cars.services;

import org.springframework.stereotype.Component;
import ru.job4j.cars.models.CarEntity;

import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

/**
 * Класс реализующий механизм AntiSwitch
 */
@Component
public class AntiSwitch {
    private final HashMap<String, Function<List<String>, List<CarEntity>>> list = new HashMap<>();

    /**
     * Метод реализует сохранение значения и соответствующего ему действия
     * @param choose значение
     * @param action действие
     */
    public void load(String choose, Function<List<String>, List<CarEntity>> action) {
        list.put(choose, action);
    }

    /**
     * Метод реализует выполнение действия, соответствующего значению
     * @param choose значение
     * @return успех
     */
    public List<CarEntity> run(String choose, List<String> param) {
        return list.get(choose).apply(param);
    }

}
