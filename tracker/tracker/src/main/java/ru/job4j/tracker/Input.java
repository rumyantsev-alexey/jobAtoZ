package ru.job4j.tracker;

import java.util.ArrayList;
import java.util.List;

/**
 * интерфейс для ввода
 * @author Alex Rumyantcev
 * @version $Id$
 */
public interface Input {

    /**
     * Запрос у пользователя ответа на вопрос
     * @param question вопрос
     * @return ответ
     */
    String ask(String question);

    /**
     * Запрос у пользователя ответа на вопрос, с проверкой
     * @param question вопрос
     * @param range допустимые ответы
     * @return ответ
     */
    int ask(String question, ArrayList<Integer> range);

}
