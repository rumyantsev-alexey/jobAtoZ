package ru.job4j.magnit;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Класс конвертирует данные из базы в XML
 */
public class StoreXML {
    private File file;
    private static final Logger LOG = Logger.getLogger(StoreSQL.class.getName());

    public StoreXML(File target) {
        file = target;
    }

    /**
     * Метод записывает данные из списка в XML
     * @param list список
     */
    private void save(List<Entry> list) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Entries.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.marshal(new Entries(list), file);
        } catch (JAXBException e) {
            LOG.log(Level.WARNING, "JAXB error", e);
        }
    }

    /**
     * Метод вытаскивает данные из базы в список
     * @param prop файл свойств
     * @return список
     */
    private List<Entry> fromDBtoLIST(String prop) {
        List<Entry> result = new ArrayList<>();
        Properties prt = new Properties();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(prop)) {
            prt.load(inputStream);
            Class.forName(prt.getProperty("driver"));
            try (Connection con = DriverManager.getConnection(prt.getProperty("url"));
                 Statement st = con.createStatement();
                 ResultSet rs = st.executeQuery("select * from entry")) {
            while (rs.next()) {
                result.add(new Entry(rs.getInt("field")));
            }
            } catch (SQLException e) {
                LOG.log(Level.WARNING, "SQL error", e);
            }
        } catch (IOException | ClassNotFoundException e) {
            LOG.log(Level.WARNING, e.getMessage(), e);
        }
        return result;
    }

    /**
     * Метод генерирует файл XML из базы данных
     */
    public void generate() {
        save(fromDBtoLIST("StoreSQLconfig.properties"));
    }

    /**
     * Класс описывает структуру XML
     */
    @XmlRootElement
    public static class Entries {
        private List<Entry> entry;

        public Entries() {
        }

        public Entries(List<Entry> values) {
            this.entry = values;
        }

        public List<Entry> getEntry() {
            return entry;
        }

        public void setEntry(List<Entry> values) {
            this.entry = values;
        }
    }

    /**
     * Класс описывает структуру XML
     */
    @XmlRootElement
    public static class Entry {
        private int field;

        public Entry() {
        }

        public Entry(int value) {
            this.field = value;
        }

        public int getField() {
            return field;
        }

        public void setField(int value) {
            this.field = value;
        }
    }

}
