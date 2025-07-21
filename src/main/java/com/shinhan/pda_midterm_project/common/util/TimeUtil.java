package com.shinhan.pda_midterm_project.common.util;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

public class TimeUtil {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static Date localDateTimeToDate(LocalDateTime localDateTime, Clock clock) {
        return Date.from(localDateTime.atZone(clock.getZone()).toInstant());
    }

    public static LocalDateTime parseToLocalDateTime(String dateTime) {
        return LocalDateTime.parse(dateTime, FORMATTER);
    }

    public static LocalDate stringToLocalDate(String localDateString) {
        // null 또는 빈 문자열 체크
        if (localDateString == null || localDateString.trim().isEmpty()) {
            throw new IllegalArgumentException("날짜 문자열이 null이거나 비어있습니다.");
        }

        try {
            return LocalDate.parse(localDateString.trim());
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("잘못된 날짜 형식: " + localDateString, e);
        }
    }
}
