package com.github.anthogis.meno;

import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Helper class for handling dates and strings of dates.
 *
 * @author antHogis
 * @version 1.0
 * @since 1.0
 */
public class DateHelper {

    /**
     * The string pattern which the formatter uses, yyyy-MM-dd.
     *
     * The string pattern which the formatter uses, yyyy-MM-dd.
     * Used when parsing dates to strings and vice versa.
     */
    public static final String datePattern = "yyyy-MM-dd";

    /**
     * The formatter which converts strings to dates and vice versa.
     */
    private static SimpleDateFormat formatter = new SimpleDateFormat(datePattern);

    /**
     * Constructs a date of year, month, and day.
     *
     * @param year
     * @param month
     * @param day
     * @return the date.
     */
    public static Date dateOf(int year, int month, int day) {
        Calendar cal = new GregorianCalendar();
        cal.set(year, month, day);

        return cal.getTime();
    }

    /**
     * Constructs a date string of year, month, and day.
     *
     * @param year
     * @param month
     * @param day
     * @return
     */
    public static String stringOf(int year, int month, int day) {
        return stringOf(dateOf(year, month, day));
    }

    /**
     * Constructs a date string of a date.
     * @param date the date to format.
     * @return the formatted date string.
     */
    public static String stringOf(Date date) {
        return formatter.format(date);
    }

    /**
     * Returns the current time as a date.
     *
     * @return the current time as a date.
     */
    public static Date now() {
        Calendar cal = Calendar.getInstance();
        return cal.getTime();
    }

    /**
     * Parses a date string to a Date.
     * @param dateString the string to parse.
     * @return the date.
     * @throws ParseException if the string is in an incorrect format.
     */
    public static Date parse(String dateString) throws ParseException {
        return formatter.parse(dateString);
    }

    /**
     * Parses a date string from within a TextView to a Date.
     * @param dateField the Textview which contains the string.
     * @return the date.
     * @throws ParseException if the string within the TextView is in an incorrect format.
     */
    public static Date parse(TextView dateField) throws ParseException {
        return formatter.parse(dateField.getText().toString());
    }
}
