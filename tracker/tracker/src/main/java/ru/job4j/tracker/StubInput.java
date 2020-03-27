package ru.job4j.tracker;

import java.util.ArrayList;

/**
 * реализация имитации ввода пользователем данных (для тестов)
 * @author Alex Rumyantcev
 * @version $Id$
 */
public class StubInput implements Input {

    // массив ответов имитации пользователя
    private String[] answers;
    // позиция следующего ответа в массиве ответов
    private int position = 0;

    /**
     * конструктор класс с инициализацией массива ответов
     * @param answers массив ответов
     */
    public StubInput(final String[] answers) {
        this.answers = answers;
    }

    /**
     * Реализация основного метода ask
     * @param question текст вопроса к пользователю
     * @return ответ пользователя на вопрос
     */
    public String ask(final String question) {
        return answers[position++];
    }

    /**
     * Реализация альтернативного метода ask
     * @param question текст вопроса к пользователю
     * @param range массив возможных ответов на вопрос
     * @return ответ пользователя на вопрос
     * @exception MenuOutException пользователь выбрал несуществующий пункт меню
     */
    public int ask(final String question, final ArrayList<Integer> range) {
        int key = Integer.valueOf(this.ask(question));
        boolean exist = false;
        for (Integer value: range) {
            if (value == key) {
                exist = true;
                break;
            }
        }
        if (exist) {
            return key;
        } else {
            throw new MenuOutException("Нет такого пункта менню");
        }
    }
}
