package com.ehpadtech.monitor.utils;

import java.util.Comparator;
import com.ehpadtech.monitor.commons.entity.Employee;


public class SortByIdEmployee implements Comparator<Employee> {
	/**
	 * Compare the id Employee
	 */
	@Override
	public int compare(Employee e1, Employee e2) {
		return e1.getIdEmployee() - e2.getIdEmployee();
	}
}
