package com.employees.employees.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

import com.employees.employees.util.ValidPhoneNumberList;

@Entity
@Table(name = "employee")
public class Employee {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "employee_id", nullable = false, unique = true)
	@Pattern(regexp = "^[E][0-9]{3}$", message = "Employee ID must follow the pattern 'E' followed by 3 digits.")
	private String employeeId;

	@Column(nullable = false)
	@NotBlank(message = "First name is mandatory.")
	private String firstName;

	@Column(nullable = false)
	@NotBlank(message = "Last name is mandatory.")
	private String lastName;

	@Column(nullable = false, unique = true)
	@Email(message = "Email should be valid.")
	@NotBlank(message = "Email is mandatory.")
	private String email;

	@Column(nullable = false)
	@ElementCollection
	@NotEmpty(message = "Phone numbers cannot be empty.")
	@ValidPhoneNumberList
	private List<@Pattern(regexp = "^\\d{10}$", message = "Each phone number must be 10 digits.") String> phoneNumbers;
	
	@Column(nullable = false)
	@Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Date of joining must be in YYYY-MM-DD format.")
	private String doj;

	@Column(nullable = false)
	@Positive(message = "Salary must be a positive number.")
	private Double salary;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<String> getPhoneNumbers() {
		return phoneNumbers;
	}

	public void setPhoneNumbers(List<String> phoneNumbers) {
		this.phoneNumbers = phoneNumbers;
	}

	public String getDoj() {
		return doj;
	}

	public void setDoj(String doj) {
		this.doj = doj;
	}

	public Double getSalary() {
		return salary;
	}

	public void setSalary(Double salary) {
		this.salary = salary;
	}

	@Override
	public String toString() {
		return "Employee [id=" + id + ", employeeId=" + employeeId + ", firstName=" + firstName + ", lastName="
				+ lastName + ", email=" + email + ", phoneNumbers=" + phoneNumbers + ", doj=" + doj + ", salary="
				+ salary + "]";
	}

}
