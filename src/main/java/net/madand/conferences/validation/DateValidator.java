package net.madand.conferences.validation;

import java.time.LocalDate;

public class DateValidator {
    private String attributeName;
    private LocalDate min;

    public DateValidator(String attributeName, LocalDate min) {
        this.attributeName = attributeName;
        this.min = min;
    }

    public LocalDate validate(String value) {
        return null;
    }
}
