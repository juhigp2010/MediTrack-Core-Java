package com.airtribe.meditrack.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm";
    private static final String DATE_FORMAT = "yyyy-MM-dd";

    public static boolean isValidDate(String dateStr) {
        return isValidFormat(dateStr, DATE_TIME_FORMAT) || isValidFormat(dateStr, DATE_FORMAT);
    }

    private static boolean isValidFormat(String dateStr, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setLenient(false);
        try {
            sdf.parse(dateStr);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    public static String formatDate(Date date) {
        return new SimpleDateFormat(DATE_TIME_FORMAT).format(date);
    }

    public static Date parseDate(String dateStr) {
        try {
            if (isValidFormat(dateStr, DATE_TIME_FORMAT)) {
                return new SimpleDateFormat(DATE_TIME_FORMAT).parse(dateStr);
            } else if (isValidFormat(dateStr, DATE_FORMAT)) {
                return new SimpleDateFormat(DATE_FORMAT).parse(dateStr);
            } else {
                throw new IllegalArgumentException("Unsupported date format.");
            }
        } catch (ParseException e) {
            throw new IllegalArgumentException("Cannot parse date: " + dateStr);
        }
    }
}
