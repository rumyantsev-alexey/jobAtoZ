package ru.job4j.magnit;

import org.junit.Test;

import java.io.File;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class Magnit {

    @Test
    public void testMagnit() {
        StoreSQL ssql = new StoreSQL("StoreSQLconfig.properties");
        StoreXML sxml = new StoreXML(new File("StoreXML.xml"));
        ConvertXSQT cxsqt = new ConvertXSQT();
        SumSAX ssax = new SumSAX();

        ssql.generate(1000000);
        sxml.generate();
        cxsqt.convert(new File("StoreXML.xml"), new File("ConvertXSQT.xml"), new File("convert.xsl"));
        assertThat(ssax.sum(), is(1784293664));
    }

}
