package com.ehpadtech.monitor.commons.entity;


public class Employee {

	/**
	 * Different parameters used
	 */
	private int idEmployee = 0;
	private String lastnameEmployee = "";
	private String nameEmployee = "";
	private String password = "";
	private String function = "";

	/**
	 * Constructor
	 * 
	 * @param idEmployee
	 * @param lastnameEmployee
	 * @param nameEmployee
	 * @param password
	 * @param function
	 */
	public Employee(int idEmployee, String lastnameEmployee, String nameEmployee, String password, String function) {
		super();
		this.idEmployee = idEmployee;
		this.lastnameEmployee = lastnameEmployee;
		this.nameEmployee = nameEmployee;
		this.password = password;
		this.function = function;
	}

	/**
	 * Constructor
	 */
	public Employee() {
	}

	/**
	 * @return the idEmployee
	 */
	public int getIdEmployee() {
		return idEmployee;
	}

	/**
	 * @param idEmployee the idEmployee to set
	 */
	public void setIdEmployee(int idEmployee) {
		this.idEmployee = idEmployee;
	}

	/**
	 * @return the lastnameEmployee
	 */
	public String getLastnameEmployee() {
		return lastnameEmployee;
	}

	/**
	 * @param lastnameEmployee the lastnameEmployee to set
	 */
	public void setLastnameEmployee(String lastnameEmployee) {
		this.lastnameEmployee = lastnameEmployee;
	}

	/**
	 * @return the nameEmployee
	 */
	public String getNameEmployee() {
		return nameEmployee;
	}

	/**
	 * @param nameEmployee the nameEmployee to set
	 */
	public void setNameEmployee(String nameEmployee) {
		this.nameEmployee = nameEmployee;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the function
	 */
	public String getFunction() {
		return function;
	}

	/**
	 * @param poste the function to set
	 */
	public void setFunction(String function) {
		this.function = function;
	}
}