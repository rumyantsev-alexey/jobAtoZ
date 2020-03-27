package ru.job4j.magnit;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.stream.StreamResult;

/**
 * Класс конвертирует один xml в другой с помощью шаблона
 */
public class ConvertXSQT {
    private static final Logger LOG = Logger.getLogger(StoreSQL.class.getName());

    /**
     * Метод конвертирует один xml в другой с помощью шаблона
     * @param source исходный файл
     * @param dest результирующий файл
     * @param scheme шаблон
     */
    public void convert(File source, File dest, File scheme) {
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = null;
        try {
            transformer = factory.newTransformer(new StreamSource(scheme));
            transformer.transform(new StreamSource(source), new StreamResult(dest));
        } catch (TransformerException e) {
            LOG.log(Level.WARNING, "XSLT error", e);
        }
    }

}
