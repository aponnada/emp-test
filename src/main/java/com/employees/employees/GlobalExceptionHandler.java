package com.employees.employees;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.employees.employees.bean.EmployeeCustomErrorResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<EmployeeCustomErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
	    Map<String, String> errors = new HashMap<>();

	    for (FieldError error : ex.getBindingResult().getFieldErrors()) {
	        errors.put(error.getField(), error.getDefaultMessage());
	    }

	    EmployeeCustomErrorResponse response = new EmployeeCustomErrorResponse(
	        HttpStatus.BAD_REQUEST.value(),
	        "Validation Error",
	        "One or more validation errors occurred.",
	        "VALIDATION_ERROR",
	        errors
	    );

	    return ResponseEntity.badRequest().body(response);
	}

    @ExceptionHandler(Exception.class)
    public ResponseEntity<EmployeeCustomErrorResponse> handleGenericExceptions(Exception ex) {
        EmployeeCustomErrorResponse response = new EmployeeCustomErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                ex.getMessage(),
                "INTERNAL_SERVER_ERROR",
                null
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
