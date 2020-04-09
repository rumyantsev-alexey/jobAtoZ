package ru.job4j.parser;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Класс предназначен для работы с бд и обработку полученного списка вакансий
 */
public class DataBase {
    private String driver;
    private String url;
    private String user;
    private String pass;
    private static final Logger LOG = LogManager.getLogger(DataBase.class.getName());

    /**
     * Метод предназначен для инициализации работы с бд
     * @param opt файл настроек
     */
    public DataBase(final String opt) {
        Properties prt = new Properties();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(opt)) {
            prt.load(inputStream);
            driver = prt.getProperty("driver");
            url = prt.getProperty("url");
            user = prt.getProperty("user");
            pass = prt.getProperty("pass");
            Class.forName(driver);
            try (Connection con = DriverManager.getConnection(url, user, pass);
                 Statement st = con.createStatement()) {
                st.execute("create table if not exists vacancy (id integer, subj varchar(200), link varchar(200), created TIMESTAMP );");
                st.execute("create table if not exists session (session TIMESTAMP, succ boolean );");
            } catch (SQLException e) {
                LOG.error("SQL error", e);
            }
        } catch (IOException e) {
            LOG.error("IO error", e);
        } catch (ClassNotFoundException e) {
            LOG.error("Properties IO error", e);
        }
    }

    /**
     * Метод записывает в бд полученный список вакансий с проверкой на уникальность
     * @param list список вакансий
     * @return количество внесенных записей
     */
    public int writeDB(final List<Item> list) {
        int cnt = 0;
        try (Connection conn = DriverManager.getConnection(url, user, pass);
             PreparedStatement stOne = conn.prepareStatement("select * from vacancy  where id = ?");
             PreparedStatement stTwo = conn.prepareStatement("insert into vacancy (id, subj, link, created) values (?,?,?,?)");
             Statement stThree = conn.createStatement()) {
            for (Item lst: list) {
                stOne.setInt(1, lst.getId());
                try (ResultSet rs = stOne.executeQuery()) {
                    if (!rs.next()) {
                        stTwo.setInt(1, lst.getId());
                        stTwo.setString(2, lst.getSubj());
                        stTwo.setString(3, lst.getLink().toString());
                        stTwo.setTimestamp(4, lst.getCreated());
                        stTwo.executeUpdate();
                        cnt++;
                    }
                }
            }
            stThree.executeUpdate("insert into session (session, succ) values ( NOW(), 'true')");
        } catch (SQLException e) {
            LOG.error("SQL error", e);
        }
        return cnt;
    }

    /**
     * Метод удаляет все данные из бд
     * для тестов
     */
    public void  delTable() {
        try (Connection con = DriverManager.getConnection(url, user, pass);
             Statement st = con.createStatement()) {
            st.execute("delete from vacancy;");
            st.execute("delete from session;");
        } catch (SQLException e) {
            LOG.error("Delete table error:", e);
        }
    }

    /**
     * Метод возвращает 1е января текущего года
     * @return дата в таймстампе
     */
    private Timestamp setDateToCurrentNewYear() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(LocalDate.now().getYear(), 0, 1, 0, 0, 0);
        return new Timestamp(calendar.getTimeInMillis());
    }

    /**
     * Метод возвращает дату последней обработке сайта с вакансиями
     * @return дата в таймстампе
     */
    public Timestamp getLastTime() {
        Timestamp result = setDateToCurrentNewYear();
        try (Connection con = DriverManager.getConnection(url, user, pass);
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("select max(session) from session")) {
            rs.next();
            if (rs.getTimestamp(1) != null) {
                result = rs.getTimestamp(1);
            }
        } catch (SQLException e) {
            LOG.error("SQL error", e);
        }
        return  result;
    }

}
