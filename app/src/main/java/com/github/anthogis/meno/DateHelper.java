package com.github.anthogis.meno;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateHelper {
    public static final String datePattern = "yy-MM-dd";
    private static SimpleDateFormat formatter = new SimpleDateFormat(datePattern);

    public static Date of(int year, int month, int day) {
        Calendar cal = new GregorianCalendar();
        cal.set(year, month, day);

        return cal.getTime();
    }

    public static String format(Date date) {
        return formatter.format(date);
    }

    public static Date parse(String dateString) throws ParseException {
        return formatter.parse(dateString);
    }
}
