package io.automationhacks.testinfra.tests;

import io.automationhacks.testinfra.utils.DateTimeHelpers;

import org.testng.Assert;
import org.testng.annotations.Test;

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
}
