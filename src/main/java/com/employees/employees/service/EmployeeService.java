package com.employees.employees.service;

import com.employees.employees.model.Employee;

public interface EmployeeService {
	
	public Employee createEmployee(Employee employee);

	public Employee findByEmployeeId(String employeeId);

	public Employee findByEmail(String email);
}
