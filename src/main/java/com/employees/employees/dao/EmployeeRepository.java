package com.employees.employees.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.employees.employees.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long>{
	
	public Employee findByEmployeeId(String employeeId);
	boolean existsByEmployeeId(String employeeId);
	public Employee findByEmail(String email);

}
