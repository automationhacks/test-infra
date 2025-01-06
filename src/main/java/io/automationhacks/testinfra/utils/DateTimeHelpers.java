package io.automationhacks.testinfra.utils;

public class DateTimeHelpers {
    public long getEpochSecondsForCurrentTimeInUTC() {
        return System.currentTimeMillis() / 1000;
    }



}
