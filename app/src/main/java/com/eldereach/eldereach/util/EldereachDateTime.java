package com.eldereach.eldereach.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EldereachDateTime {
    public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yy hh:mm a", Locale.ENGLISH);

    public static boolean isDateAfterCurrentDate(String date) {
        Date currentDate = new Date();
        Date comparingDate = null;

        try {
            comparingDate = simpleDateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        assert comparingDate != null;
        return comparingDate.after(currentDate);
    }

    public static boolean isDateTime(String dateTimeDest) {
        try {
            simpleDateFormat.parse(dateTimeDest);
        } catch (ParseException e) {
            return false;
        }

        return true;
    }

    public static boolean isDateBefore(String home, String dest) {
        Date dateHome = null;
        Date dateDest = null;

        try {
            dateHome = simpleDateFormat.parse(home);
            dateDest = simpleDateFormat.parse(dest);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        assert dateHome != null;
        return dateHome.before(dateDest);
    }

    public static String getCurrentDate() {
        Date currentDate = new Date();
        return simpleDateFormat.format(currentDate);
    }
}
