package com.jcs.javacommunitysite.util;

import java.time.Duration;
import java.time.Instant;

public class TimeUtil {
    public static String getRelativeTime(String iso8601time) {
        Instant then = Instant.parse(iso8601time);
        Instant now = Instant.now();

        Duration duration = Duration.between(then, now);
        long seconds = duration.getSeconds();

        if (seconds < 60) {
            return seconds + " seconds ago";
        } else if (seconds < 3600) {
            long minutes = duration.toMinutes();
            return minutes + " minutes ago";
        } else if (seconds < 86400) {
            long hours = duration.toHours();
            return hours + " hours ago";
        } else if (seconds < 604800) {
            long days = duration.toDays();
            return days + " days ago";
        } else {
            return then.toString(); // TODO make human readable with correct timezone
        }


    }
}
