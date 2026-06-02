package com.booking.util;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class SessionOverlapUtilTest {

    @Test
    void returnsTrueWhenRangesOverlap() {
        Instant start = Instant.parse("2026-06-02T10:00:00Z");
        Instant end = Instant.parse("2026-06-02T11:00:00Z");

        assertThat(SessionOverlapUtil.overlaps(
                start,
                end,
                Instant.parse("2026-06-02T10:30:00Z"),
                Instant.parse("2026-06-02T11:30:00Z")
        )).isTrue();
    }

    @Test
    void returnsFalseWhenRangesOnlyTouch() {
        assertThat(SessionOverlapUtil.overlaps(
                Instant.parse("2026-06-02T10:00:00Z"),
                Instant.parse("2026-06-02T11:00:00Z"),
                Instant.parse("2026-06-02T11:00:00Z"),
                Instant.parse("2026-06-02T12:00:00Z")
        )).isFalse();
    }
}
