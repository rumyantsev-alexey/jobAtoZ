package ru.job4j.servlets.cinema;

import com.fasterxml.jackson.databind.JsonNode;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Класс реализует поддержку БД для задания Кинотеатр (часть model моделе MVC)
 */
public class DBcinema {
    private static final String URL = "jdbc:postgresql://localhost:5432/cinema";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "tester";
    private static final Logger LOG = Logger.getLogger(DBcinema.class.getName());

    private static final DBcinema INSTANCE = new DBcinema();

    private DBcinema() { }

    public static DBcinema getInstance() {
        return INSTANCE;
    }

    /**
     * Метод добавляет кинозал
     * @param name название кинозала
     * @param row кол-во рядов в кинозале
     * @param col кол-во мест в каждом ряду
     * @return успех
     */
    public boolean addZone(String name, int row, int col) {
        boolean result = false;
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement st = connection.prepareStatement("insert into zone (name, size_row, size_col) "
                                                                    + "values (?,?,?);")) {
            st.setString(1, name);
            st.setInt(2, row);
            st.setInt(3, col);
            if (st.executeUpdate() > 0) {
                result = true;
            }
        } catch (SQLException e) {
            LOG.log(Level.WARNING, "SQL error", e);
        }
        return result;
    }

    /**
     * Метод дабавляет сеанс
     * @param name название сеанса
     * @param time время начала сеанса
     * @return успех
     */
    public boolean addSession(String name, String time) {
        boolean result = false;
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement st = connection.prepareStatement("insert into session (name, time) values (?,?);")) {
            st.setString(1, name);
            st.setString(2, time);
            if (st.executeUpdate() > 0) {
                result = true;
            }
        } catch (SQLException e) {
            LOG.log(Level.WARNING, "SQL error", e);
        }
        return result;
    }

    public boolean addClient(String name, String phone) {
        boolean result = false;
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement st = connection.prepareStatement("insert into clients (name, phone) values (?,?);")) {
            st.setString(1, name);
            st.setString(2, phone);
            if (st.executeUpdate() > 0) {
                result = true;
            }
        } catch (SQLException e) {
            LOG.log(Level.WARNING, "SQL error", e);
        }
        return result;
    }

    /**
     * Метод добавляет стоимость ряда мест в конкретный кинозал на конкретный сеанс
     * @param row номер ряда
     * @param cost стоимость
     * @param sessid айди сеанса
     * @param zoneid айди кинозала
     * @return успех
     */
    public boolean addCostp(int row, int cost, int sessid, int zoneid) {
        boolean result = false;
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement st = connection.prepareStatement("insert into cost_pattern (row, cost, sess_id, zone_id) "
                                                                    + "values (?,?,?,?);")) {
            st.setInt(1, row);
            st.setInt(2, cost);
            st.setInt(3, sessid);
            st.setInt(4, zoneid);
            if (st.executeUpdate() > 0) {
                result = true;
            }
        } catch (SQLException e) {
            LOG.log(Level.WARNING, "SQL error", e);
        }
        return result;
    }

    /**
     * Метод добавляет список занятых мест на определенную дату в определнный кинозал и на определенный сеанс
     * @param date дата
     * @param seats список мест
     * @param sessid айди сеанса
     * @param zoneid айди кинозала
     * @param cliid айди клиента, сделавшего заказ
     * @return успех
     */
    public boolean addBusyp(String date, JsonNode seats, int sessid, int zoneid, int cliid) {
        boolean result = false;
        String[] yx = new String[2];
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement st = connection.prepareStatement("INSERT INTO busy_pattern (date, row, col, sess_id, "
                                                                    + "zone_id, cli_id) VALUES (?,?,?,?,?,?);")) {
            connection.setAutoCommit(false);
            for (JsonNode se: seats) {
                yx = se.textValue().split("x");
                st.setString(1, date);
                st.setInt(2, Integer.parseInt(yx[0]));
                st.setInt(3, Integer.parseInt(yx[1]));
                st.setInt(4, sessid);
                st.setInt(5, zoneid);
                st.setInt(6, cliid);
                st.addBatch();
            }
            int[] res = st.executeBatch();
            if (Arrays.stream(res).sum() == seats.size()) {
                connection.commit();
                result = true;
            } else {
                connection.rollback();
            }
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            LOG.log(Level.WARNING, "SQL error", e);
        }
        return result;
    }

    /**
     * Метод позволяет получить значение определенной колонки (только int) определенной таблицы по фильтру name
     * @param name имя
     * @param table название таблицы
     * @param col название колонки
     * @return успех
     */
    public Integer colByName(String name, String table, String col) {
        Integer result = null;
        try (Connection con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement st = con.prepareStatement("select " + col + " from " + table + " where name = ?")) {
            st.setString(1, name);
            try (ResultSet rst = st.executeQuery()) {
                if (rst.next()) {
                    result = rst.getInt(1);
                }
            }
        } catch (SQLException e) {
            LOG.log(Level.WARNING, "SQL error", e);
        }
        return result;
    }

    /**
     * Метод позволяет получить имя по id в определенной таблице
     * @param id айди
     * @param table имя таблицы
     * @return успех
     */
    public String nameById(Integer id, String table) {
        String result = null;
        try (Connection con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement st = con.prepareStatement("select name from " + table + " where id = ?")) {
            st.setInt(1, id);
            try (ResultSet rst = st.executeQuery()) {
                if (rst.next()) {
                    result = rst.getString(1);
                }
            }
        } catch (SQLException e) {
            LOG.log(Level.WARNING, "SQL error", e);
        }
        return result;
    }

    /**
     * Метод выдает список все доступных кинозалов
     * @return успех
     */
    public ArrayList<Zone> findAllZones() {
        ArrayList<Zone> result = new ArrayList<>();
        try (Connection con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             Statement st = con.createStatement();
             ResultSet rst = st.executeQuery("select name, size_row, size_col from zone")) {
            while (rst.next()) {
                result.add(new Zone(rst.getString(1), rst.getInt(2), rst.getInt(3)));
            }
        } catch (SQLException e) {
            LOG.log(Level.WARNING, "SQL error", e);
        }
        return result;
    }

    /**
     * Метод выдает список всех сеансов
     * @return успех
     */
    public ArrayList<Session> findAllSessions() {
        ArrayList<Session> result = new ArrayList<>();
        try (Connection con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             Statement st = con.createStatement();
             ResultSet rst = st.executeQuery("select name, time from session")) {
            while (rst.next()) {
                result.add(new Session(rst.getString(1), rst.getString(2)));
            }
        } catch (SQLException e) {
            LOG.log(Level.WARNING, "SQL error", e);
        }
        return result;
    }

    /**
     * Метод выдает список всех цен в текущий кинозал на текущий сеанс
     * @param zone имя кинозала
     * @param session имя сеанса
     * @return успех
     */
    public ArrayList<Integer> costPerRow(String zone, String session) {
        ArrayList<Integer> result = new ArrayList();
        try (Connection con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement st = con.prepareStatement("select cost from cost_pattern where zone_id=? and sess_id=? order by row")) {
            st.setInt(1, colByName(zone, "zone", "id"));
            st.setInt(2, colByName(session, "session", "id"));
            try (ResultSet rst = st.executeQuery()) {
                while (rst.next()) {
                    result.add(rst.getInt(1));
                }
            }
        } catch (SQLException e) {
            LOG.log(Level.WARNING, "SQL error", e);
        }
        return result;
    }

    /**
     * Метод выдает список занятых мест на текущую дату в текущий кинозал и на текущий сеанс
     * @param date дата
     * @param zone имя кинозала
     * @param session имя сеанса
     * @return успех
     */
    public ArrayList<Seat> returnBusySeats(String date, String zone, String session) {
        ArrayList<Seat> result = new ArrayList();
        try (Connection con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement st = con.prepareStatement("select row,col from busy_pattern where date = ? and "
                                                                + "zone_id=? and sess_id=?")) {
            st.setString(1, date);
            st.setInt(2, colByName(zone, "zone", "id"));
            st.setInt(3, colByName(session, "session", "id"));
            try (ResultSet rst = st.executeQuery()) {
                while (rst.next()) {
                    result.add(new Seat(rst.getInt(1), rst.getInt(2)));
                }
            }
        } catch (SQLException e) {
            LOG.log(Level.WARNING, "SQL error", e);
        }
        return result;
    }

 }
