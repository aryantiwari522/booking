package com.booking.util;

import java.time.Instant;

public final class SessionOverlapUtil {
    private SessionOverlapUtil() {}

    public static boolean overlaps(Instant aStart, Instant aEnd, Instant bStart, Instant bEnd) {
        return aStart.isBefore(bEnd) && bStart.isBefore(aEnd);
    }
}
