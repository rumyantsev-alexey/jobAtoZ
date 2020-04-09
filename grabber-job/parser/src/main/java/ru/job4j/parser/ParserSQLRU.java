package ru.job4j.parser;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Класс предназначен для парсинга сайта вакансий
 */
public class ParserSQLRU {

    private static final Logger LOG = LogManager.getLogger(ParserSQLRU.class.getName());
    private final SimpleDateFormat format = new SimpleDateFormat("d MMM yy, HH:mm", new Locale("ru", "RU"));

    /**
     * Метод парсит сайт в поиске вакансий, младше заданного времени
     * @param lastime заданное время
     * @return список найденных вакансии
     */
    public List<Item> loadVac(final Timestamp lastime) {
        String subj;
        String link;
        Timestamp  created = lastime;
        List<Item> result = new LinkedList<>();
        for (int i = 1;  i < 200 && checkDate(lastime, created); i++) {
            try {
                Document doc = Jsoup.connect("http://www.sql.ru/forum/job-offers/" + i).get();
                Elements elms = doc.getElementsByClass("postslisttopic");
                for (Element el : elms) {
                    created = normalDate(el.parent().child(5).text());
                    if (checkTitle(el.child(0).text()) && checkDate(lastime, created)) {
                        subj = el.child(0).text();
                        link = el.child(0).attr("href");
                        result.add(new Item(subj, new URL(link), created));
                    }
               }
            } catch (IOException e) {
                LOG.error("IO error:", e);
            }
        }
        return result;
    }

    /**
     * Метод проверяет что дата меньше даты последней обработки
     * @param lastime дата последней сессии
     * @param date проверяемая дата
     * @return
     */
    private boolean checkDate(final Timestamp lastime, final Timestamp date) {
        boolean result = false;
        if (lastime.before(date) || lastime == date) {
            result = true;
        }
        return result;
    }

    /**
     * Метод преобразует дату с сайта в нужный формат
     * @param date дата в виде текста
     * @return дата в виде таймстампа
     */
    private Timestamp normalDate(String date) {
        Calendar calendar = Calendar.getInstance();
        if (date.contains("сегодня")) {
            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(date.substring(9, 11)));
            calendar.set(Calendar.MINUTE, Integer.parseInt(date.substring(12, 14)));
        } else if (date.contains("вчера")) {
            calendar.add(Calendar.DATE, -1);
            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(date.substring(7, 9)));
            calendar.set(Calendar.MINUTE, Integer.parseInt(date.substring(10, 12)));
        } else {
            try {
                calendar.setTime(format.parse(date));
            } catch (ParseException e) {
                LOG.error("IO error", e);
            }
        }
        return new Timestamp(calendar.getTimeInMillis());
    }

    /**
     * Метод проверяет строку на наличие ключевых слов
     * @param str строка
     * @return
     */
    private boolean checkTitle(final String str) {
        boolean result = false;
        if (str.toLowerCase().contains("java") && !str.toLowerCase().contains("script") && !str.toLowerCase().contains("Важно:")) {
            result = true;
        }
        return result;
    }

}
