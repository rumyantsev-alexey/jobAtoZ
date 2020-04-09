package ru.job4j.magnit;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Класс генерит последовательность чисел и записывает ее в базу
 */
public class StoreSQL {
    private static String driver;
    private static String url;
    private static final Logger LOG = Logger.getLogger(StoreSQL.class.getName());

    /**
     * Конструктор читает свойства, инициализирует драйвера и проверяет наличие таблицы
     * @param opt файл параметров
     */
    public StoreSQL(final String opt) {
        Properties prt = new Properties();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(opt)) {
                prt.load(inputStream);
                driver = prt.getProperty("driver");
                url = prt.getProperty("url");
                Class.forName(driver);
                try (Connection con = DriverManager.getConnection(url);
                Statement st = con.createStatement()) {
                    st.execute("create table if not exists entry (field INTEGER );");
                } catch (SQLException e) {
                    LOG.log(Level.WARNING, "SQL error", e);
                }
        } catch (IOException | ClassNotFoundException e) {
           LOG.log(Level.WARNING, e.getMessage(), e);
        }
    }

    /**
     * Метод генерит последовательность и записывает ее в базу
     * @param n количество элементов
     */
    public void generate(int n) {
        try (Connection con = DriverManager.getConnection(url);
             Statement stOne = con.createStatement();
             PreparedStatement stTwo = con.prepareStatement("INSERT INTO entry (field) values (?);")) {
            stOne.execute("delete from entry;");
            con.setAutoCommit(false);
            for (int i = 0; i < n; i++) {
                stTwo.setInt(1, i + 1);
                stTwo.executeUpdate();
            }
            con.commit();
            con.setAutoCommit(true);
        } catch (SQLException e) {
        LOG.log(Level.WARNING, "SQL error", e);
        }
    }
}
