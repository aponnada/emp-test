package com.employees.employees.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.employees.employees.dao.EmployeeRepository;
import com.employees.employees.model.Employee;
import com.employees.employees.service.EmployeeService;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired
	private EmployeeRepository employeeRepository;

	@Override
	public Employee createEmployee(Employee employee) {
		if (employeeRepository.existsByEmployeeId(employee.getEmployeeId())) {
			throw new IllegalArgumentException("Employee with ID " + employee.getEmployeeId() + " already exists.");
		}
		return employeeRepository.save(employee);
	}

	@Override
	public Employee findByEmployeeId(String employeeId) {
		return employeeRepository.findByEmployeeId(employeeId);
	}

	@Override
	public Employee findByEmail(String email) {
		return employeeRepository.findByEmail(email);
	}

}
