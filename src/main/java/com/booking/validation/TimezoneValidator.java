package com.booking.validation;

import java.time.ZoneId;

/**
 * Simple utility validator for IANA timezone IDs.
 * Use TimezoneValidator.isValid(zoneId) to verify input before creating offerings or converting times.
 */
public final class TimezoneValidator {
    private TimezoneValidator() {}

    public static boolean isValid(String zoneId) {
        if (zoneId == null || zoneId.isBlank()) return false;
        try {
            ZoneId.of(zoneId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
