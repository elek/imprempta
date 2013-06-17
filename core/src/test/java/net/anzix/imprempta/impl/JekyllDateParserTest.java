package net.anzix.imprempta.impl;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

public class JekyllDateParserTest {
    @Test
    public void testTransform() throws Exception {
        SimpleDateFormat fieldParser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ");
        System.out.println(fieldParser.format(new Date()));
        fieldParser.parse("2013-05-24 08:55:00+0200");
    }
}
