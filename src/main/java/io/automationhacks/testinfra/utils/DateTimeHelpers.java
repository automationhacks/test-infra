package io.automationhacks.testinfra.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateTimeHelpers {
    // Copilot can generate docs for you by using inline chat
    /**
     * Returns the current time in UTC as the number of seconds since the Unix epoch.
     *
     * @return the current time in seconds since the Unix epoch (January 1, 1970, 00:00:00 GMT)
     */
    public long getEpochSecondsForCurrentTimeInUTC() {
        return System.currentTimeMillis() / 1000;
    }

    // Copilot can generate a function by taking a comment as input
    // write a java function that takes a date and time format and no of days and can return the
    // date after adding the no of days
    public String addDaysToCurrentDate(String dateFormat, int days) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, days);
        return sdf.format(c.getTime());
    }
}
