package ru.job4j.magnit;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Класс подсчитывает сумму всех элементов в xml файле
 */
public class SumSAX {
    private static final Logger LOG = Logger.getLogger(StoreSQL.class.getName());
    private int sum = 0;

    /**
     * Метод подсчитывает сумму всех элементов в xml файле
     * @return сумма
     */
    public int sum() {
        SAXParserFactory saxParsFact = SAXParserFactory.newInstance();

        DefaultHandler handler = new DefaultHandler() {
            @Override
            public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                if (qName.equals("entry")) {
                    sum = sum + Integer.valueOf(attributes.getValue(0));
                }

            }
        };

        try {
            SAXParser saxParser = saxParsFact.newSAXParser();
            saxParser.parse(new File("ConvertXSQT.xml"), handler);
        } catch (ParserConfigurationException | IOException | SAXException e) {
            LOG.log(Level.WARNING, e.getMessage(), e);
        }
        return sum;
    }
}
