package ru.job4j.tracker;

import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class TrackerTest {
    Tracker tracker = Tracker.initTracker("config.properties");

    @Before
    public void iniTracker() {
        tracker.checkAll();
        tracker.delTable();
        tracker.checkAll();
    }

    // тест методов add и update
    @Test
    public void whenUpdateNameThenReturnNewName() {
        Item previous = new Item("test1", "testDescription");
        tracker.add(previous);
        // Создаем новую заявку.
        Item next = new Item("test2", "testDescription2");
        // Проставляем старый id из previous, который был сгенерирован выше.
        next.setId(previous.getId());
        // Обновляем заявку в трекере.
        tracker.update(next);
        // Проверяем, что заявка с таким id имеет новые имя test2.
        assertThat(tracker.findById(previous.getId()).getName(), is("test2"));
    }

    // тест метода findAll
    @Test
    public void whenAddNewItemThenTrackerHasSameItem() {
        // создаем заявку
        Item item = new Item("test1", "testDescription");
        // добавляем заявку в трекер
        tracker.add(item);
        // проверяем что она есть в списке заявок
        assertThat(tracker.findById(item.getId()), is(item));
    }

    // тест метода FindById
    @Test
    public void whenFindByIdThenTrackerHasTrue() {
        Item[] items = new Item[4];
        // создаем тестовые заявки
        items[0] = new Item("testname1", "testDesc1");
        items[1] = new Item("testname2", "testDesc2");
        items[2] = new Item("testname3", "testDesc3");
        items[3] = new Item("testname4", "testDesc5");
        // добавляем заявки в трекер
        tracker.add(items[0]);
        tracker.add(items[1]);
        tracker.add(items[2]);
        tracker.add(items[3]);
        // проверяем что элемнт найденный по id третей заявки является третьим элементом
        assertThat(tracker.findById(items[2].getId()), is(items[2]));
    }

    // тест метода FindByName
    @Test
    public void whenFindByNameThenTrackerHasTrue() {
        Item[] items = new Item[4];
        // создаем тестовые заявки
        items[0] = new Item("testname1", "testDesc1");
        items[1] = new Item("testname244", "testDesc2");
        items[2] = new Item("testname3", "testDesc3");
        items[3] = new Item("testname4", "testDesc5");
        // добавляем заявки в трекер
        tracker.add(items[0]);
        tracker.add(items[1]);
        tracker.add(items[2]);
        tracker.add(items[3]);
        // проверяем что элемнт найденный по имени второй заявки является вторым элементом
        assertThat(tracker.findByName(items[1].getName()).get(0), is(items[1]));
    }

    @Test
    // тест метода del
    public void whenDelItemThenTrackerDoesIt() {
        Item[] items = new Item[4];
        // создаем тестовые заявки
        items[0] = new Item("testname1", "testDesc1");
        items[1] = new Item("testname2", "testDesc2");
        items[2] = new Item("testname3", "testDesc3");
        items[3] = new Item("testname4", "testDesc5");
        Tracker tracker = new Tracker();
        // добавляем заявки в трекер
        tracker.add(items[0]);
        tracker.add(items[1]);
        tracker.add(items[2]);
        tracker.add(items[3]);
        // удаляем 3й элемент
        tracker.delete(items[2]);
        // проверяем встал ли на его место 4й
        assertThat(tracker.findAll().get(2).getName(), is(items[3].getName()));
    }

}