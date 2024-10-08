package com.employees.employees.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.employees.employees.bean.EmployeeCustomErrorResponse;
import com.employees.employees.bean.TaxDeductionResponse;
import com.employees.employees.model.Employee;
import com.employees.employees.service.EmployeeService;
import com.employees.employees.util.JwtUtil;
import com.employees.employees.util.TaxDeductionServiceUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/employees")
@Tag(name = "Employee Management", description = "Endpoints for managing employee details")

public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private TaxDeductionServiceUtil taxDeductionServiceUtil;

	@Autowired
	private JwtUtil jwtUtil;

	@PostMapping("/createEmployee")
	@Operation(summary = "Store Employee Details", description = "Creates a new employee with the provided details.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Employee created successfully", 
					content = @Content(schema = @Schema(implementation = EmployeeCustomErrorResponse.class))),
			@ApiResponse(responseCode = "400", description = "Invalid input"),
			@ApiResponse(responseCode = "409", description = "Conflict: Employee already exists"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	public ResponseEntity<?> createEmployee(@Validated @RequestBody Employee employee, HttpServletRequest request) {
		String username = validateJwtToken(request);
		if (username == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired JWT token.");
		}
		Employee existingEmployeeById = employeeService.findByEmployeeId(employee.getEmployeeId());
		if (existingEmployeeById != null) {
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body("Employee with ID " + employee.getEmployeeId() + " already exists.");
		}

		Employee existingEmployeeByEmail = employeeService.findByEmail(employee.getEmail());
		if (existingEmployeeByEmail != null) {
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body("Email " + employee.getEmail() + " is already associated with another employee.");
		}

		try {
			employee.setId(null);
			Employee savedEmployee = employeeService.createEmployee(employee);
			return ResponseEntity.status(HttpStatus.CREATED).body(savedEmployee);
		} catch (ConstraintViolationException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Database constraint violation: " + e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}

	}

	@GetMapping("/{employeeId}/tax-deductions")
	@Operation(summary = "Get tax deductions for an employee", description = "Returns the tax deduction details for an employee for the current financial year (April to March).")
	public ResponseEntity<?> getTaxDeductions(@PathVariable String employeeId, HttpServletRequest request) {

		String username = validateJwtToken(request);
		if (username == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired JWT token.");
		}
		Employee employee = employeeService.findByEmployeeId(employeeId);

		if (employee == null) {
			return ResponseEntity.notFound().build();
		}

		TaxDeductionResponse taxDeduction = taxDeductionServiceUtil.calculateTaxDeductions(employee);
		return ResponseEntity.ok(taxDeduction);
	}

	private String validateJwtToken(HttpServletRequest request) {
		String authorizationHeader = request.getHeader("Authorization");
		String jwt = null;
		String username = null;

		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			jwt = authorizationHeader.substring(7);
			username = jwtUtil.extractUsername(jwt);
		}

		// Validate the token
		if (jwt != null && username != null && jwtUtil.validateToken(jwt, username)) {
			return username;
		}

		return null;
	}
}
