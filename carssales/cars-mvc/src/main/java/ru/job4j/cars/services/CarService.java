package ru.job4j.cars.services;

import org.springframework.stereotype.Service;
import ru.job4j.cars.models.CarEntity;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class CarService extends CommonMethodForService<CarEntity> {

    private AntiSwitch answ = new AntiSwitch();

    /**
     * Конструктор создает объект доступа к базе объявоений и инициализирует фильтры
     */
    public CarService() {
        answ.load("actual", this.actual());
        answ.load("lastday", this.lastday());
        answ.load("withfoto", this.withfoto());
        answ.load("selectmark", this.selectmark());
    }

    /**
     * Метод реализует получение списка объявлений с примененными фильтрами
     * @param filters вписок параметров фильтра
     * @return списрк объявлений
     */
    public List<CarEntity> findAllCarsWithFilter(final List<String> filters) {
        return answ.run(filters.get(0), filters);
    }


    /**
     * Функция реализующая фильтр актуальности объявлений
     * @return
     */
    private Function<List<String>, List<CarEntity>> actual() {
        return (params) -> this.findAll().stream().filter((x) -> !x.isOld()).collect(Collectors.toList());
    }

    /**
     * Функция реализующая фильтр объявлений за сегодня
     * @return
     */
    private Function<List<String>, List<CarEntity>> lastday() {
        return (params) ->      {
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Timestamp ts = new Timestamp(System.currentTimeMillis());
            Date date = new Date(ts.getTime());
            Date todayWithZeroTime = null;
            try {
                todayWithZeroTime = formatter.parse(formatter.format(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Timestamp ts1 = new Timestamp(todayWithZeroTime.getTime());
            return this.findAll().stream().filter((x) -> x.getCreated().compareTo(ts1) > 0).collect(Collectors.toList());
        };
    }

    /**
     * Функция реализующая фильтр объявлений с фото
     * @return
     */
    private Function<List<String>, List<CarEntity>> withfoto() {
        return (params) ->   this.findAll().stream().filter((x) -> x.getFotos().size() > 0).collect(Collectors.toList());
    }

    /**
     * Функция реализующая фильтр объявлений с определенной маркой автомобиля
     * @return
     */
    private Function<List<String>, List<CarEntity>> selectmark() {
        return (params) ->    this.findAll().stream().filter((x) -> x.getMark().getName().equals(params.get(1))).collect(Collectors.toList());
    }

}
