package ru.job4j.tracker;

/**
 * Исключение при неправильном цифровом выборе пункта меню
 * @author Alex Rumyantcev
 * @version $Id$
 */
public class MenuOutException extends RuntimeException {

    public MenuOutException(final String msg) {
        super(msg);
    }
}
