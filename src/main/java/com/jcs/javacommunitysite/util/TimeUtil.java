package com.jcs.javacommunitysite.util;

import java.time.Duration;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;

public class TimeUtil {
    public static String calculateTimeText(OffsetDateTime createdAt) {
        var now = OffsetDateTime.now();
        var yearsBetween = ChronoUnit.YEARS.between(createdAt, now);
        var daysBetween = ChronoUnit.DAYS.between(createdAt, now);
        var hoursBetween = ChronoUnit.HOURS.between(createdAt, now);
        var minutesBetween = ChronoUnit.MINUTES.between(createdAt, now);

        if (yearsBetween > 0) {
            return yearsBetween == 1 ? "1 year ago" : yearsBetween + " years ago";
        } else if (daysBetween > 0) {
            return daysBetween == 1 ? "1 day ago" : daysBetween + " days ago";
        } else if (hoursBetween > 0) {
            return hoursBetween == 1 ? "1 hour ago" : hoursBetween + " hours ago";
        } else if (minutesBetween > 0) {
            return minutesBetween == 1 ? "1 minute ago" : minutesBetween + " minutes ago";
        } else {
            return "Just now";
        }
    }
}
