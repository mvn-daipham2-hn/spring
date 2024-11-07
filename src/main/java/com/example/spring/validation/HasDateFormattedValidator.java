package com.example.spring.validation;

import com.example.spring.helper.StringHelper;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.util.Optional;

public class HasDateFormattedValidator implements ConstraintValidator<HasDateFormatted, String> {
    @Override
    public void initialize(HasDateFormatted constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String dateStr, ConstraintValidatorContext constraintValidatorContext) {
        Optional<LocalDate> dateOptional = StringHelper.toLocalDate(dateStr);
        return dateOptional.isPresent();
    }
}
