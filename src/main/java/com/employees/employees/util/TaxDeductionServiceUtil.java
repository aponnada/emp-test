package com.employees.employees.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

import com.employees.employees.bean.TaxDeductionResponse;
import com.employees.employees.model.Employee;

@Component
public class TaxDeductionServiceUtil {

	public TaxDeductionResponse calculateTaxDeductions(Employee employee) {
		double yearlySalary = employee.getSalary() * getWorkingMonths(employee.getDoj());
		double taxAmount = calculateTax(yearlySalary);
		double cessAmount = calculateCess(yearlySalary);
		return new TaxDeductionResponse(employee.getEmployeeId(), employee.getFirstName(), employee.getLastName(),
				yearlySalary, taxAmount, cessAmount);
	}

	private double getWorkingMonths(String doj) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate joiningDate = LocalDate.parse(doj, formatter);
		LocalDate currentDate = LocalDate.now();
		int totalMonths = 12;
		if (joiningDate.isAfter(currentDate.minusMonths(totalMonths))) {
			totalMonths = currentDate.getMonthValue() - joiningDate.getMonthValue() + 1;
		}
		return totalMonths;
	}

	private double calculateTax(double yearlySalary) {
		double tax = 0.0;
		if (yearlySalary > 250000) {
			tax += (Math.min(yearlySalary, 500000) - 250000) * 0.05;
		}
		if (yearlySalary > 500000) {
			tax += (Math.min(yearlySalary, 1000000) - 500000) * 0.10;
		}
		if (yearlySalary > 1000000) {
			tax += (yearlySalary - 1000000) * 0.20;
		}
		return tax;
	}

	private double calculateCess(double yearlySalary) {
		double cess = 0.0;
		if (yearlySalary > 2500000) {
			cess = (yearlySalary - 2500000) * 0.02;
		}
		return cess;
	}
}
