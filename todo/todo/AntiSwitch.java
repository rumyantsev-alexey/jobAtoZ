package ru.job4j.todo;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.function.BiConsumer;

/**
 * Класс реализующий механизм AntiSwitch
 */
public class AntiSwitch {
    private HashMap<String, BiConsumer<PrintWriter, JsonNode>> list = new HashMap<>();

    /**
     * Метод реализует сохранение значения и соответствующего ему действия
     * @param choose значение
     * @param action действие
     */
    public void load(String choose, BiConsumer<PrintWriter, JsonNode> action) {
        list.put(choose, action);
    }

    /**
     * Метод реализует выполнение действия, соответствующего значению
     * @param choose значение
     * @param out параметры
     * @param json параметры
     * @return успех
     */
    public void run(String choose, PrintWriter out, JsonNode json) {
        list.get(choose).accept(out, json);
    }

}
