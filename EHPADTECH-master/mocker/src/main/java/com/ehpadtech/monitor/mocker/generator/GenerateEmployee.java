package com.ehpadtech.monitor.mocker.generator;

import java.util.Random;
import java.util.ResourceBundle;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.ehpadtech.monitor.commons.entity.Employee;
import com.ehpadtech.monitor.mocker.launcher.MockSocket;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GenerateEmployee {

	/**
	 * Different parameters used
	 */
	private int nbGeneration;
	private Random random;
	private Employee employee;
	private String table;
	private String requestType;
	private String jsonString;
	private ObjectMapper objectMapper;
	private ResourceBundle rsLastname = ResourceBundle.getBundle("lastname_employee_list");
	private ResourceBundle rsFirstname = ResourceBundle.getBundle("firstname_employee_list");
	private ResourceBundle rsFunction = ResourceBundle.getBundle("function_employee_list");
	private ResourceBundle rsPassword = ResourceBundle.getBundle("password_employee_list");
	private static final Logger logger = LogManager.getLogger(GenerateEmployee.class);

	/**
	 * Constructor
	 */
	public GenerateEmployee() {
	}

	/**
	 * Constructor
	 * 
	 * @param nbGeneration
	 */
	public GenerateEmployee(int nbGeneration) {
		this.nbGeneration = nbGeneration;
	}

	/**
	 * Generate an random employee and add it into data base
	 */
	public void generate() {
		for (int i = 0; i < nbGeneration; i++) {
			employee = new Employee();
			random = new Random();
			employee.setLastnameEmployee(rsLastname.getString(Integer.toString(random.nextInt(1000))));
			employee.setNameEmployee(rsFirstname.getString(Integer.toString(random.nextInt(1000))));
			employee.setFunction(rsFunction.getString(Integer.toString(random.nextInt(1000))));
			employee.setPassword(rsPassword.getString(Integer.toString(random.nextInt(1000))));

			requestType = "CREATE";
			table = "Employee";
			objectMapper = new ObjectMapper();
			try {
				jsonString = objectMapper.writeValueAsString(employee);
				new MockSocket(requestType, jsonString, table);
			} catch (Exception e1) {
				logger.log(Level.WARN, "Impossible to parse in JSON Employee data " + e1.getClass().getCanonicalName());
			}
		}
	}
}
