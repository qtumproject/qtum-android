package org.qtum.wallet.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by kirillvolkov on 22.11.2017.
 */

public class DateCalculator {
    public static String getShortDate(long timeInMills) {
        long currentTime = (new Date()).getTime();
        long delay = currentTime - timeInMills;
        String dateString;
        if (delay < 60000) {
            dateString = "few seconds ago";
        } else if (delay < 3600000) {
            dateString = delay / 60000 + " min ago";
        } else {
            Calendar calendarTodayBegin = Calendar.getInstance();
            calendarTodayBegin.set(Calendar.HOUR_OF_DAY, 0);
            calendarTodayBegin.set(Calendar.MINUTE, 0);
            calendarTodayBegin.set(Calendar.SECOND, 0);
            Date dateTransaction = new Date(timeInMills);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateTransaction);
            if ((calendarTodayBegin.getTimeInMillis() - timeInMills) < 0) {
                SimpleDateFormat timeFormatter = new SimpleDateFormat("hh:mm a", Locale.US);
                dateString = timeFormatter.format(dateTransaction);
            } else if ((calendarTodayBegin.getTimeInMillis() - timeInMills) < 86400000) {
                dateString = "yesterday";
            } else {
                dateString = String.format(Locale.US, "%s, %d", calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.US), calendar.get(Calendar.DAY_OF_MONTH));
            }
        }
        return dateString;
    }
}
