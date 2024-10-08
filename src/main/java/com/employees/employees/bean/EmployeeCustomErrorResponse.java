package com.employees.employees.bean;

import java.util.Map;

public class EmployeeCustomErrorResponse {

	private int status;
	private String error;
	private String message;
	private String errorCode;
	private Map<String, String> fieldErrors;


	public EmployeeCustomErrorResponse(int status, String error, String message, String errorCode,
			Map<String, String> fieldErrors) {
		super();
		this.status = status;
		this.error = error;
		this.message = message;
		this.errorCode = errorCode;
		this.fieldErrors = fieldErrors;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public Map<String, String> getFieldErrors() {
		return fieldErrors;
	}

	public void setFieldErrors(Map<String, String> fieldErrors) {
		this.fieldErrors = fieldErrors;
	}

}
