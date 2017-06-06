package com.pixelplex.qtum.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by max-v on 6/5/2017.
 */

public class DateCalculator {

    public static String getDate(long date){
        long currentTime = (new Date()).getTime();
        long delay = currentTime - date;
        String dateString;
        if (delay < 60000) {
            dateString = delay / 1000 + " sec ago";
        } else
        if (delay < 3600000) {
            dateString = delay / 60000 + " min ago";
        } else {

            Calendar calendarNow = Calendar.getInstance();
            calendarNow.set(Calendar.HOUR_OF_DAY, 0);
            calendarNow.set(Calendar.MINUTE, 0);
            calendarNow.set(Calendar.SECOND, 0);


            Date dateTransaction = new Date(date);
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(dateTransaction);
            if ((date - calendarNow.getTimeInMillis()) > 0) {
                dateString = calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE);
            } else {
                dateString = calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.US) + ", " + calendar.get(Calendar.DATE);
            }
        }
        return dateString;
    }

}
