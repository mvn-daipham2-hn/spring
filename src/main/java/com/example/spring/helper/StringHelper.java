package com.example.spring.helper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class StringHelper {
    public static boolean isNullOrEmpty(String target) {
        return target == null || target.isEmpty();
    }

    private static final List<DateTimeFormatter> formatters = Arrays.asList(
            DateTimeFormatter.ofPattern("dd-MM-yyyy"),
            DateTimeFormatter.ofPattern("dd/MM/yyyy"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd"),
            DateTimeFormatter.ofPattern("yyyy/MM/dd")
    );

    public static Optional<LocalDate> toLocalDate(String dateStr) {
        for (DateTimeFormatter formatter : formatters) {
            try {
                return Optional.of(LocalDate.parse(dateStr, formatter));
            } catch (DateTimeParseException e) {
                // Continue to next format
            }
        }
        return Optional.empty();
    }
}
