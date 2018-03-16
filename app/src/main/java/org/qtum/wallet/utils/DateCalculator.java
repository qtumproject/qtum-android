package org.qtum.wallet.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

public class DateCalculator {

    public static String getShortDate(String dateInFormat) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-d HH:mm:ss", Locale.US);
        long date = 0;
        try {
            date = formatter.parse(dateInFormat).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long currentTime = (new Date()).getTime();
        long delay = currentTime - date;
        String dateString;
        if (delay < 60000) {
            dateString = delay / 1000 + " sec ago";
        } else if (delay < 3600000) {
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
                SimpleDateFormat timeFormatter = new SimpleDateFormat("hh:mm a", Locale.US);
                dateString = timeFormatter.format(dateTransaction);
            } else {
                dateString = calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.US) + ", " + calendar.get(Calendar.DATE);
            }
        }
        return dateString;
    }

    public static Long getLongDate(String dateInFormat){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-d HH:mm:ss", Locale.US);
        long date = 0;
        try {
            date = formatter.parse(dateInFormat).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

        public static Long getLongDateInTimeZone(String dateInFormat){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-d HH:mm:ss", Locale.US);
        long date = 0;
        try {
            date = formatter.parse(dateInFormat).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date+TimeZone.getDefault().getOffset((new Date()).getTime());
    }


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

    public static String getFullDate(long timeInMills) {
        String dateString;
        Date dateTransaction = new Date(timeInMills);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateTransaction);
        SimpleDateFormat timeFormatter = new SimpleDateFormat("hh:mm:ss a", Locale.US);
        dateString = String.format(Locale.US, "%s, %d %s", calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.US), calendar.get(Calendar.DAY_OF_MONTH), timeFormatter.format(dateTransaction));
        return dateString;
    }

    public static String getDateInFormat(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        return formatter.format(date);
    }

    public static String getDateInFormat(Long date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        return formatter.format(new Date(date));
    }

    public static String getDateInUtc(Long date) {
        DateFormat dateFormat  = new SimpleDateFormat("yyyy-MM-d HH:mm:ss", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return dateFormat.format(new Date(date));
    }

    public static int equals(String date1, String date2) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-d HH:mm:ss");
        long date1long = 0;
        long date2long = 0;
        try {
            date1long = formatter.parse(date1).getTime();
            date2long = formatter.parse(date2).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date1long > date2long ? -1 : date1long < date2long ? 1 : 0;
    }

    public static Long getCurrentDate() {
        return (new Date()).getTime();
    }

    public static Observable getUpdater() {
        return Observable.interval(59L, TimeUnit.SECONDS)
                .timeInterval()
                .observeOn(AndroidSchedulers.mainThread());
    }
}
