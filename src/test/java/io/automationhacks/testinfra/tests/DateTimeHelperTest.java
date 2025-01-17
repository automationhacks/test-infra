package io.automationhacks.testinfra.tests;

import io.automationhacks.testinfra.utils.DateTimeHelpers;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;

class DateTimeHelpersTest {
    @Test
    public void getEpochSecondsForCurrentTimeInUTC_ReturnsNonNegativeValue() {
        DateTimeHelpers dateTimeHelpers = new DateTimeHelpers();
        long epochSeconds = dateTimeHelpers.getEpochSecondsForCurrentTimeInUTC();
        Assert.assertTrue(epochSeconds >= 0);
    }

    @Test
    public void getEpochSecondsForCurrentTimeInUTC_ReturnsCurrentTimeInSeconds() {
        DateTimeHelpers dateTimeHelpers = new DateTimeHelpers();
        long epochSeconds = dateTimeHelpers.getEpochSecondsForCurrentTimeInUTC();
        long currentTimeSeconds = System.currentTimeMillis() / 1000;
        Assert.assertTrue(Math.abs(currentTimeSeconds - epochSeconds) < 2);
    }

    @Test
    void addDaysToCurrentDate_ReturnsCorrectFutureDate() {
        DateTimeHelpers dateTimeHelpers = new DateTimeHelpers();
        String result = dateTimeHelpers.addDaysToCurrentDate("yyyy-MM-dd", 5);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, 5);
        String expectedDate = sdf.format(c.getTime());
        Assert.assertEquals(expectedDate, result);
    }

    @Test
    void addDaysToCurrentDate_ReturnsCorrectPastDate() {
        DateTimeHelpers dateTimeHelpers = new DateTimeHelpers();
        String result = dateTimeHelpers.addDaysToCurrentDate("yyyy-MM-dd", -5);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -5);
        String expectedDate = sdf.format(c.getTime());
        Assert.assertEquals(expectedDate, result);
    }

    @Test
    void addDaysToCurrentDate_ThrowsExceptionForInvalidDateFormat() {
        DateTimeHelpers dateTimeHelpers = new DateTimeHelpers();
        Assert.assertThrows(
                IllegalArgumentException.class,
                () -> {
                    dateTimeHelpers.addDaysToCurrentDate("invalid-format", 5);
                });
    }
}
