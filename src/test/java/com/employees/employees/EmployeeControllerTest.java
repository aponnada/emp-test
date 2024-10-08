package com.employees.employees;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.employees.employees.bean.TaxDeductionResponse;
import com.employees.employees.controller.EmployeeController;
import com.employees.employees.model.Employee;
import com.employees.employees.service.EmployeeService;
import com.employees.employees.util.TaxDeductionServiceUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

public class EmployeeControllerTest {

	@InjectMocks
	private EmployeeController employeeController;

	@Mock
	private EmployeeService employeeService;

	@Mock
	private HttpServletRequest request;

	private ObjectMapper objectMapper;
	private Employee employee;
	@Mock
	private TaxDeductionServiceUtil taxDeductionServiceUtil;
	private TaxDeductionResponse taxDeductionResponse;
	
	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
		objectMapper = new ObjectMapper();
		employee = new Employee();
		employee.setEmployeeId("E123");
		employee.setFirstName("John");
		employee.setLastName("Doe");
		employee.setEmail("john.doe@example.com");
		employee.setPhoneNumbers(Arrays.asList("1234567890", "0987654321"));
		employee.setDoj("2023-05-16");
		employee.setSalary(50000d);
	}

	@Test
	public void testCreateEmployee_Success() {
		// Mocking service methods
		when(employeeService.findByEmployeeId(employee.getEmployeeId())).thenReturn(null);
		when(employeeService.findByEmail(employee.getEmail())).thenReturn(null);
		when(employeeService.createEmployee(employee)).thenReturn(employee);

		// Call the createEmployee method
		ResponseEntity<?> response = employeeController.createEmployee(employee, request);

		// Validate the response
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertEquals(employee, response.getBody());
	}

	@Test
	public void testCreateEmployee_ConflictById() {
		// Mock existing employee by ID
		when(employeeService.findByEmployeeId(employee.getEmployeeId())).thenReturn(employee);

		// Call the createEmployee method
		ResponseEntity<?> response = employeeController.createEmployee(employee, request);

		// Validate the response
		assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
		assertEquals("Employee with ID E123 already exists.", response.getBody());
	}

	@Test
	public void testCreateEmployee_ConflictByEmail() {
		// Mock existing employee by email
		when(employeeService.findByEmployeeId(anyString())).thenReturn(null);
		when(employeeService.findByEmail(employee.getEmail())).thenReturn(employee);

		// Call the createEmployee method
		ResponseEntity<?> response = employeeController.createEmployee(employee, request);

		// Validate the response
		assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
		assertEquals("Email john.doe@example.com is already associated with another employee.", response.getBody());
	}

	@Test
	public void testCreateEmployee_ConstraintViol() {
		// Mocking service methods
		when(employeeService.findByEmployeeId(employee.getEmployeeId())).thenReturn(null);
		when(employeeService.findByEmail(employee.getEmail())).thenReturn(null);
		when(employeeService.createEmployee(employee))
				.thenThrow(new ConstraintViolationException("Constraint violation", null));

		// Call the createEmployee method
		ResponseEntity<?> response = employeeController.createEmployee(employee, request);

		// Validate the response
		assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
		assertEquals("Database constraint violation: Constraint violation", response.getBody());
	}

	@Test
	public void testCreateEmployee_InternalServerErr() {
		// Mocking service methods
		when(employeeService.findByEmployeeId(employee.getEmployeeId())).thenReturn(null);
		when(employeeService.findByEmail(employee.getEmail())).thenReturn(null);
		when(employeeService.createEmployee(employee)).thenThrow(new RuntimeException("Internal error"));

		// Call the createEmployee method
		ResponseEntity<?> response = employeeController.createEmployee(employee, request);

		// Validate the response
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
		assertEquals("Internal error", response.getBody());
	}

	@Test
	public void testCreateEmployee_ConstraintViolation() {
		employee.setEmployeeId("E123");
		employee.setEmail("test@example.com");

		when(employeeService.findByEmployeeId("E123")).thenReturn(null);
		when(employeeService.findByEmail("test@example.com")).thenReturn(null);
		when(employeeService.createEmployee(employee))
				.thenThrow(new ConstraintViolationException("Constraint violation", null));

		ResponseEntity<?> response = employeeController.createEmployee(employee, request);

		assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
		assertEquals("Database constraint violation: Constraint violation", response.getBody());
	}

	@Test
	public void testCreateEmployee_InternalServerError() {
		employee.setEmployeeId("E123");
		employee.setEmail("test@example.com");

		when(employeeService.findByEmployeeId("E123")).thenReturn(null);
		when(employeeService.findByEmail("test@example.com")).thenReturn(null);
		when(employeeService.createEmployee(employee)).thenThrow(new RuntimeException("Internal error"));

		ResponseEntity<?> response = employeeController.createEmployee(employee, request);

		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
		assertEquals("Internal error", response.getBody());
	}

	// Tests for getTaxDeductions
	@BeforeEach
	public void setUpTax() {
		MockitoAnnotations.openMocks(this);
		objectMapper = new ObjectMapper(); // Initialize ObjectMapper for JSON conversion

		// Sample Employee object
		employee = new Employee();
		employee.setEmployeeId("E123");
		
		// Sample TaxDeductionResponse object
		taxDeductionResponse = new TaxDeductionResponse();
		taxDeductionResponse.setEmployeeId(employee.getEmployeeId());
		taxDeductionResponse.setYearlySalary(employee.getSalary());
		taxDeductionResponse.setTaxAmount(37500);
		taxDeductionResponse.setCessAmount(6000);
	}

	@Test
	public void testGetTaxDeductions_Success() {
		// Mock the employee retrieval
		when(employeeService.findByEmployeeId(employee.getEmployeeId())).thenReturn(employee);
		// Mock the tax deduction calculation
		when(taxDeductionServiceUtil.calculateTaxDeductions(employee)).thenReturn(taxDeductionResponse);

		// Call the getTaxDeductions method
		ResponseEntity<?> response = employeeController.getTaxDeductions(employee.getEmployeeId(),request);

		// Validate the response
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(taxDeductionResponse, response.getBody());
	}

	@Test
	public void testGetTaxDeductions_NotFound() {
		// Mock the employee retrieval to return null
		when(employeeService.findByEmployeeId(employee.getEmployeeId())).thenReturn(null);

		// Call the getTaxDeductions method
		ResponseEntity<?> response = employeeController.getTaxDeductions(employee.getEmployeeId(),request);

		// Validate the response
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertEquals(null, response.getBody());
	}
}
