package com.booking.util;

import java.time.*;

public class TimezoneUtil {
    public static Instant toUtc(String localDateTimeIso, String zoneId) {
        LocalDateTime ldt = LocalDateTime.parse(localDateTimeIso);
        ZonedDateTime zdt = ldt.atZone(ZoneId.of(zoneId));
        return zdt.toInstant();
    }

    public static String toZoneString(Instant instant, String zoneId) {
        ZonedDateTime zdt = instant.atZone(ZoneId.of(zoneId));
        return zdt.toString();
    }
}
