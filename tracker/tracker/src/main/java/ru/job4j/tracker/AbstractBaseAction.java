package ru.job4j.tracker;

/**
 * абстрактный класс для пункта меню
 * @author Alex Rumyantcev
 * @version $Id$
 */
public abstract class AbstractBaseAction implements UserAction {
    private final int key;
    private final String name;

    /**
     * Конструктор
     * @param key номер пункта меню
     * @param name название пункта меню
     */
    protected AbstractBaseAction(final int key, final String name) {
        this.key = key;
        this.name = name;
    }

    /**
     * Возврат номера пункта меню
     * @return номер пункта меню
     */
    @Override
    public int key() {
        return this.key;
    }

    /**
     * Возращает строку описания пункта меню
     * @return описание пункта меню
     */
    @Override
    public String info() {
        return String.format("%s. %s", this.key, this.name);
    }
}
