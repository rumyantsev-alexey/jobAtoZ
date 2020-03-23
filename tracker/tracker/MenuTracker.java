package ru.job4j.tracker;

import java.util.ArrayList;

/**
 * Внешний внутренний класс, реализующий редактирование заявок
 * @author Alex Rumyantcev
 * @version $Id$
 */
class EditItem extends AbstractBaseAction {

    /**
     * Конструктор
     * @param key номер пункта меню
     * @param name название пункта меню
     */
    public EditItem(final int key, final String name) {
        super(key, name);
    }

    /**
     * Редактирование заявки
     * @param input интерфейс ввода
     * @param tracker хранилище заявок
     */
    public void execute(final Input input, final Tracker tracker) {
        System.out.println("------------ Редактирование заявки --------------");
        String strid = input.ask("Введите Id заявки:");
        Item item = tracker.findById(strid);
        if (item == null) {
            System.out.println("Нет такой заявки!!!!!");
        } else {
            System.out.println(String.format("Имя заявки:%s Описание заявки:%s", item.getName(), item.getDesc()));
            item.setName(input.ask("Введите новое имя заявки:"));
            item.setDesc(input.ask("Введите новое описание:"));
            tracker.update(item);
            System.out.println("Заявка изменена!!!");
        }
        System.out.println("---------------------------------------------------------");
    }
}

/**
 * Внешний внутренний класс, реализующий поиск завки по имени
 * @author Alex Rumyantcev
 * @version $Id$
 */
class FindByName extends AbstractBaseAction {

    /**
     * Конструктор
     * @param key номер пункта меню
     * @param name название пункта меню
     */
   FindByName(final int key, final String name) {
        super(key, name);
    }

    /**
     * Поиск по имени
     * @param input интерфейс ввода
     * @param tracker хранилище заявок
     */
    public void execute(final Input input, final Tracker tracker) {
        System.out.println("------------ Поиск заявки по имени --------------");
        String strid = input.ask("Введите имя заявки:");
        ArrayList<Item> items = tracker.findByName(strid);
        if (items == null) {
            System.out.println("Нет такой заявки!!!!!");
        } else {
            System.out.println("Заявки найдены!!!");
            for (Item item: items) {
                System.out.println(String.format("Имя заявки:%s Описание заявки:%s Код заявки:%s", item.getName(), item.getDesc(), item.getId()));
            }
        }
        System.out.println("---------------------------------------------------------");
    }
}


/**
 * Класс, отвечающий за работу меню с трекером
 * @author Alex Rumyantcev
 * @version $Id$
 */
public class MenuTracker {
    // количество пунктов меню
    private final int menulenght = 7;
    // возможные значения для выбора пунктов меню
    private ArrayList<Integer> ranges = new ArrayList<>(menulenght);
    // Система ввода пользователя
    private Input input;
    // Хранилище заявок
    private Tracker tracker;
    // Возможные действия, выбранные из меню
    private ArrayList<UserAction> actions = new ArrayList<UserAction>(menulenght);
    // признак выхода из меню
    private boolean vihod = false;

    /**
     * Конструктор, с инициализацией параметров
     * @param input система ввода
     * @param tracker хранилище заявок
     */
    public MenuTracker(final Input input, final Tracker tracker) {
        this.input = input;
        this.tracker = tracker;
    }

    public ArrayList<Integer> getRanges() {
        return this.ranges;
    }

    public boolean getExit() {
        return this.vihod;
    }

    public void setExit(final boolean exit) {
        this.vihod = exit;
    }

    /**
     * метод реализующий заполнение меню действий и возможных
     * значений выбора пользователя
     */
    public void fillActions() {
        this.actions.add(new AddItem(0, "Добавить заявку"));
        this.actions.add(new ShowItems(1, "Посмотреть существующие заявки"));
        this.actions.add(new EditItem(2, "Редактировать заявку"));
        this.actions.add(new DelItem(3, "Удаление заявки"));
        this.actions.add(new FindById(4, "Поиск заявки по Id"));
        this.actions.add(new FindByName(5, "Поиск заявки по имени"));
        this.actions.add(new Exit(6, "Выход из меню"));
        for (UserAction act: actions) {
            this.ranges.add(act.key());
        }
    }

    /**
     * метод реализующий различные действия при выборе из меню
     * @param key выбор в меню
     */
   public void select(final int key) {
        for (UserAction act: actions) {
            if (act.key() == key) {
                act.execute(this.input, this.tracker);
                break;
            }
        }
   }

    /**
     * метод, реализующий показ меню
     */
    public void show() {
        for (UserAction action:this.actions) {
            if (action != null) {
                System.out.println(action.info());
            }
        }
    }

    /**
     * внутрений класс, реализующий Добавление заявки
     * @author Alex Rumyantcev
     * @version $Id$
     */
   private class AddItem extends AbstractBaseAction {

        /**
         * Конструктор
         * @param key номер пункта меню
         * @param name название пункта меню
         */
    AddItem(final int key, final String name) {
        super(key, name);
    }

        /**
         * Добавление заявки
         * @param input интерфейс ввода
         * @param tracker хранилище заявок
         */
        public void execute(final Input input, final Tracker tracker) {
            System.out.println("------------ Добавление новой заявки --------------");
            String name = input.ask("Введите имя заявки:");
            String desc = input.ask("Введите описание заявки:");
            Item item = new Item(name, desc);
            tracker.add(item);
            System.out.println(String.format("------------ Введена новая заявка с Id : %s-----------", item.getId()));
        }
    }

    /**
     * внутрений статический класс, реализующий Показ всех заявок
     * @author Alex Rumyantcev
     * @version $Id$
     */
    private static class ShowItems extends AbstractBaseAction {

        /**
         * Конструктор
         * @param key номер пункта меню
         * @param name название пункта меню
         */
        ShowItems(final int key, final String name) {
            super(key, name);
        }

        /**
         * Показ всех заявок
         * @param input интерфейс ввода
         * @param tracker хранилище заявок
         */
        public void execute(final Input input, final Tracker tracker) {
            System.out.println("------------ Все зарегистрированные заявки --------------");
            for (Item item:tracker.findAll()) {
                System.out.println(String.format("Имя заявки:%s Описание заявки:%s Код заявки:%s", item.getName(), item.getDesc(), item.getId()));
            }
            System.out.println("---------------------------------------------------------");
        }
    }

    /**
     * внутрений класс, реализующий Удаление заявки
     * @author Alex Rumyantcev
     * @version $Id$
     */
    private class DelItem extends AbstractBaseAction {

        /**
         * Конструктор
         * @param key номер пункта меню
         * @param name название пункта меню
         */
        public DelItem(final int key, final String name) {
            super(key, name);
        }

        /**
         * Удаление завки
         * @param input интерфейс ввода
         * @param tracker хранилище заявок
         */
        public void execute(final Input input, final Tracker tracker) {
            System.out.println("------------ Удаление заявки --------------");
            String strid = input.ask("Введите Id заявки:");
            Item item = tracker.findById(strid);
            if (item == null) {
                System.out.println("Нет такой заявки!!!!!");
            } else {
                tracker.delete(item);
                System.out.println("Заявка удалена!!!");
            }
            System.out.println("---------------------------------------------------------");
        }
    }

    /**
     * внутрений статический класс, реализующий Поиск по Id
     * @author Alex Rumyantcev
     * @version $Id$
     */
   private static class FindById extends AbstractBaseAction {

        /**
         * Конструктор
         * @param key номер пункта меню
         * @param name название пункта меню
         */
        public FindById(final int key, final String name) {
            super(key, name);
        }

        /**
         * Поиск по Id
         * @param input интерфейс ввода
         * @param tracker хранилище заявок
         */
        public void execute(final Input input, final Tracker tracker) {
            System.out.println("------------ Поиск заявки по Id --------------");
            String strid = input.ask("Введите Id заявки:");
            Item item = tracker.findById(strid);
            if (item == null) {
                System.out.println("Нет такой заявки!!!!!");
            } else {
                System.out.println("Заявка найдена!!!");
                System.out.println(String.format("Имя заявки:%s Описание заявки:%s Код заявки:%s", item.getName(), item.getDesc(), item.getId()));
            }
            System.out.println("---------------------------------------------------------");
        }
    }

    /**
     * внутрений класс , реализующий Выход из меню
     * @author Alex Rumyantcev
     * @version $Id$
     */
    private class Exit extends AbstractBaseAction {

        /**
         * Конструктор
         * @param key номер пункта меню
         * @param name название пункта меню
         */
        public Exit(final int key, final String name) {
            super(key, name);
        }

        /**
         * Выход из меню
         *
         * @param input   интерфейс ввода
         * @param tracker хранилище заявок
         */
        public void execute(final Input input, final Tracker tracker) {
            System.out.println("------------ Выход из меню --------------");
            setExit(true);
        }
    }
}
