package ru.job4j.tracker;

/**
 * Класс для запуска диалога с пользователем
 * @author Alex Rumyantcev
 * @version $Id$
 */
public class StartUI {
    // Получение данных от пользователя.
     private Input input;
    // хранилище заявок
    private Tracker tracker;

    /**
     * Конструктор с инициализацией данных
     * @param input система ввода
     * @param tracker хранилище заявок
     */
    public StartUI(final Input input, final Tracker tracker) {
        this.input = input;
        this.tracker = tracker;
    }
    /**
     * Основой цикл программы.
     */
    public void init() {
        MenuTracker menu = new MenuTracker(this.input, this.tracker);
        menu.fillActions();
        do {
            menu.show();
            menu.select(input.ask("Выбор:", menu.getRanges()));
        } while (!menu.getExit());
    }

   /**
     * Запуск программы.
     * @param args возможные аргументы
     */
 /*   public static void main(String[] args) {
        Tracker tracker = new Tracker();
        new StartUI(new ValidateInput(), tracker).init();
    } */
}