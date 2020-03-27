package ru.job4j.tracker;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * реализация интерфейса ввода для консоли
 * @author Alex Rumyantcev
 * @version $Id$
 */
public class ConsoleInput implements Input  {

    private Scanner scanner = new Scanner(System.in);

    /**
     * Реализация основного метода ask
     * @param question текст вопроса к пользователю
     * @return ответ пользователя на вопрос
     */
    public String ask(final String question) {
        System.out.print(question);
        return scanner.nextLine();
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
