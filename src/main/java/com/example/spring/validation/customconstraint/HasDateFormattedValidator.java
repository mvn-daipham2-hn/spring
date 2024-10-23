package com.example.spring.validation.customconstraint;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class HasDateFormattedValidator implements ConstraintValidator<HasDateFormatted, String> {
    @Override
    public void initialize(HasDateFormatted constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String string, ConstraintValidatorContext constraintValidatorContext) {
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        try {
            LocalDate.parse(string, formatter1);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
