package com.github.anthogis.meno;

import android.widget.EditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateHelper {
    public static final String datePattern = "yyyy-MM-dd";
    private static SimpleDateFormat formatter = new SimpleDateFormat(datePattern);

    public static Date dateOf(int year, int month, int day) {
        Calendar cal = new GregorianCalendar();
        cal.set(year, month, day);

        return cal.getTime();
    }

    public static String stringOf(int year, int month, int day) {
        return stringOf(dateOf(year, month, day));
    }

    public static String stringOf(Date date) {
        return formatter.format(date);
    }

    public static Date now() {
        Calendar cal = Calendar.getInstance();
        return cal.getTime();
    }

    public static Date parse(String dateString) throws ParseException {
        return formatter.parse(dateString);
    }

    public static Date parse(EditText dateField) throws ParseException {
        return formatter.parse(dateField.getText().toString());
    }
}
