package com.ehpadtech.monitor.connection.dao;

import java.sql.Connection;


public abstract class DAO<T> {

	/**
	 * Initialization of parameters
	 */
	protected Connection con = null;

	/**
	 * Constructor
	 * 
	 * @param connection
	 */
	public DAO(Connection connection) {
		this.con = connection;
	}

	/**
	 * Creation method
	 * 
	 * @param obj
	 * @return boolean
	 */
	public abstract boolean create(String jsonString);

	/**
	 * Delete method
	 * 
	 * @param obj
	 * @return boolean
	 */
	public abstract boolean delete(String jsonString);

	/**
	 * Update method
	 * 
	 * @param obj
	 * @return boolean
	 */
	public abstract boolean update(String jsonString);

	/**
	 * Find informations with the id
	 * 
	 * @param obj
	 * @return String
	 */
	public abstract String read(String jsonString);

	/**
	 * Give all lines from table
	 * 
	 * @param jsonString
	 * @return String
	 */
	public abstract String readAll(String jsonString);

}