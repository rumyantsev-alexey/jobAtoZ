package ru.job4j.tracker;

/**
 * интерфейс для пункта меню
 * @author Alex Rumyantcev
 * @version $Id$
 */
public interface UserAction {
    /**
     * Возврат номера пункта меню
     * @return номер пункта меню
     */
    int key();

    /**
     * Выполнение действий пункта меню
     * @param input интерфейс ввода
     * @param tracker хранилище заявок
     */
    void execute(Input input, Tracker tracker);

    /**
     * Возращает строку описания пункта меню
     * @return описание пункта меню
     */
    String info();
}
