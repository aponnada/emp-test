package com.employees.employees.util;

import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PhoneNumberListValidator implements ConstraintValidator<ValidPhoneNumberList, List<String>> {
	@Override
	public boolean isValid(List<String> phoneNumbers, ConstraintValidatorContext context) {
		if (phoneNumbers == null || phoneNumbers.isEmpty()) {
			return false; // List must not be empty
		}

		for (String phoneNumber : phoneNumbers) {
			// Validate each phone number (10-digit pattern)
			if (!phoneNumber.matches("^\\d{10}$")) {
				return false; // Invalid phone number format
			}
		}

		return true; // All phone numbers are valid
	}
}
