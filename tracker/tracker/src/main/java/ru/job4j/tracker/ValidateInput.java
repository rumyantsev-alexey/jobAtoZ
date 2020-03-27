package ru.job4j.tracker;

import java.util.ArrayList;

/**
 * Дочерний класс от ConsoleInput, для проверки ввода корректного значения при выборе меню
 * @author Alex Rumyantcev
 * @version $Id$
 */
public class ValidateInput extends ConsoleInput {

    /**
     * Запрос у пользователя ответа на вопрос, с проверкой
     * @param question текст вопроса к пользователю
     * @param range массив возможных ответов на вопрос
     * @return значение выбора пользователя
     * @exception MenuOutException выход из диапазона меню
     * @exception NumberFormatException ввели не число
     */
    public int ask(final String question, final ArrayList<Integer> range) {
        boolean invalid = true;
        int value = -1;
        do {
            try {
                value = super.ask(question, range);
                invalid = false;
            } catch (MenuOutException moe) {
                System.out.println("Нет такого пункта меню. Введите правильное число.");
            } catch (NumberFormatException nfe) {
                System.out.println("Введите ЧИСЛО в требуемом диапазоне.");
            }
        } while (invalid);
        return value;
    }
}
