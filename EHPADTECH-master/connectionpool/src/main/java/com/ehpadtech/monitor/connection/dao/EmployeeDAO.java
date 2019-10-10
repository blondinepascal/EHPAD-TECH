package com.ehpadtech.monitor.connection.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.ehpadtech.monitor.commons.entity.Employee;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;



public class EmployeeDAO extends DAO<Employee> {

	/**
	 * Initialization of parameters
	 */
	private Employee employee;
	private Employee emp;
	private ResultSet result = null;
	private StringBuilder requestSB;
	private String request;
	private Statement st;
	private PreparedStatement prepareStatement;
	private static final Logger logger = LogManager.getLogger(EmployeeDAO.class);

	/**
	 * Constructor
	 * 
	 * @param con
	 */
	public EmployeeDAO(Connection con) {
		super(con);
	}

	/**
	 * Convert the JSON string in Object and create a request to insert values in
	 * table 'employee' return a boolean
	 */
	@Override
	public boolean create(String jsonString) {
		boolean result = false;
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			emp = objectMapper.readValue(jsonString, Employee.class);
			prepareStatement = con.prepareStatement(
					"INSERT INTO employee (nom_employee, prenom_employee, mot_de_passe, poste) values (?,?,?,?)");
			prepareStatement.setString(1, emp.getLastnameEmployee());
			prepareStatement.setString(2, emp.getNameEmployee());
			prepareStatement.setString(3, emp.getPassword());
			prepareStatement.setString(4, emp.getFunction());
			result = prepareStatement.execute();
			logger.log(Level.DEBUG, "Employee succesfully inserted in BDD");
		} catch (IOException | SQLException e) {
			logger.log(Level.WARN, "Impossible to insert employee datas in BDD " + e.getClass().getCanonicalName());
		}
		return result;
	}

	/**
	 * Convert the JSON string in Object and create a request to delete values in
	 * table 'employee' return a boolean
	 */
	@Override
	public boolean delete(String jsonString) {
		boolean result = false;
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			emp = objectMapper.readValue(jsonString, Employee.class);
			requestSB = new StringBuilder("DELETE FROM employee where id_employee= ");
			requestSB.append(emp.getIdEmployee());
			st = con.createStatement();
			result = st.execute(requestSB.toString());
			logger.log(Level.DEBUG, "Employee succesfully deleted in BDD");
		} catch (SQLException | IOException e) {
			logger.log(Level.WARN, "Impossible to delete employee datas  in BDD " + e.getClass().getCanonicalName());
		}
		return result;
	}

	/**
	 * Convert the JSON string in Object and create a request to update values in
	 * table 'employee' return a boolean
	 */
	@Override
	public boolean update(String jsonString) {
		boolean result = false;
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			emp = objectMapper.readValue(jsonString, Employee.class);
			prepareStatement = con.prepareStatement(
					"UPDATE employee SET prenom_employee = ?, nom_employee = ?,poste = ?,mot_de_passe = ? where id_employee = ?");
			prepareStatement.setString(1, emp.getNameEmployee());
			prepareStatement.setString(2, emp.getLastnameEmployee());
			prepareStatement.setString(3, emp.getFunction());
			prepareStatement.setString(4, emp.getPassword());
			prepareStatement.setInt(5, emp.getIdEmployee());
			result = prepareStatement.execute();
			logger.log(Level.DEBUG, "Employee succesfully update in BDD");
		} catch (SQLException | IOException e) {
			logger.log(Level.WARN, "Impossible to update employee datas in BDD " + e.getClass().getCanonicalName());
		}
		return result;
	}

	/**
	 * Convert the JSON string in Object and create a request to read (select)
	 * values in table 'employee' with the id and the password return a JSON string
	 * 
	 * @param jsonString
	 * @return jsonString
	 */
	public String connection(String jsonString) {
		ObjectMapper objWriter = new ObjectMapper();
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			emp = objectMapper.readValue(jsonString, Employee.class);
			prepareStatement = con.prepareStatement(
					"SELECT id_employee,nom_employee, prenom_employee, mot_de_passe, poste FROM Employee where id_employee= ? and mot_de_passe= ?");
			prepareStatement.setInt(1, emp.getIdEmployee());
			prepareStatement.setString(2, emp.getPassword());
			result = prepareStatement.executeQuery();
			result.next();
			convertDatas(result);
			jsonString = objWriter.writeValueAsString(employee);
			logger.log(Level.DEBUG, "Employee succesfully recognized in BDD");
		} catch (SQLException | IOException e) {
			logger.log(Level.WARN, "Impossible to get employee datas from BDD " + e.getClass().getCanonicalName());
			jsonString = "ERROR";
		}
		return jsonString;
	}

	/**
	 * Convert the JSON string in Object and create a request to read (select)
	 * values in table 'employee' return a JSON string
	 */
	@Override
	public String read(String jsonString) {
		ObjectMapper objWriter = new ObjectMapper();
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			emp = objectMapper.readValue(jsonString, Employee.class);
			requestSB = new StringBuilder("SELECT id_employee,nom_employee, prenom_employee, mot_de_passe, poste ");
			requestSB.append("FROM Employee where id_employee=");
			requestSB.append(emp.getIdEmployee());
			st = con.createStatement();
			result = st.executeQuery(requestSB.toString());
			result.next();
			convertDatas(result);
			jsonString = objWriter.writeValueAsString(employee);
			logger.log(Level.DEBUG, "Employee succesfully find in BDD");
		} catch (SQLException | IOException e) {
			logger.log(Level.WARN, "Impossible to get employee datas from BDD " + e.getClass().getCanonicalName());
			jsonString = "ERROR";
		}
		return jsonString;
	}

	/**
	 * Convert the JSON string in Object and create a request to read (select) all
	 * values in table 'employee' return a JSON string
	 */
	@Override
	public String readAll(String jsonString) {
		ObjectMapper objWriter = new ObjectMapper();
		List<Employee> listEmployee = new ArrayList<>();
		try {
			request = "SELECT id_employee,nom_employee, prenom_employee, mot_de_passe, poste FROM Employee";
			st = con.createStatement();
			result = st.executeQuery(request);
			while (result.next()) {
				convertDatas(result);
				listEmployee.add(employee);
			}
			jsonString = objWriter.writeValueAsString(listEmployee);
			logger.log(Level.DEBUG, "Employees succesfully find in BDD");
		} catch (SQLException | IOException e) {
			logger.log(Level.WARN, "Impossible to get employee datas from BDD " + e.getClass().getCanonicalName());
			jsonString = "ERROR";
		}
		return jsonString;
	}

	/**
	 * Convert the JSON string in Object and create a request to read (select) all
	 * values in table 'employee' by the name or lastName or function
	 * 
	 * @param jsonString
	 * @return jsonString
	 */
	public String findByName(String jsonString) {
		ObjectMapper objWriter = new ObjectMapper();
		ObjectMapper objectMapper = new ObjectMapper();
		List<Employee> listEmployee = new ArrayList<>();
		try {
			emp = objectMapper.readValue(jsonString, Employee.class);
			prepareStatement = con.prepareStatement(
					"SELECT id_employee,nom_employee, prenom_employee, mot_de_passe, poste FROM employee where ((nom_employee LIKE ?) or (prenom_employee LIKE ?) or (prenom_employee LIKE ?) or (poste LIKE ?) or (poste LIKE ?));");
			prepareStatement.setString(1, "%" + emp.getLastnameEmployee().toUpperCase() + "%");
			prepareStatement.setString(2, "%" + emp.getLastnameEmployee().toLowerCase() + "%");
			prepareStatement.setString(3, "%" + emp.getLastnameEmployee().toUpperCase() + "%");
			prepareStatement.setString(4, "%" + emp.getLastnameEmployee().toLowerCase() + "%");
			prepareStatement.setString(5, "%" + emp.getLastnameEmployee().toUpperCase() + "%");
			result = prepareStatement.executeQuery();
			while (result.next()) {
				convertDatas(result);
				listEmployee.add(employee);
			}
			jsonString = objWriter.writeValueAsString(listEmployee);
			logger.log(Level.DEBUG, "Employees succesfully find in BDD");
		} catch (SQLException | IOException e) {
			logger.log(Level.WARN, "Impossible to get employee datas from BDD " + e.getClass().getCanonicalName());
			jsonString = "ERROR";
		}
		return jsonString;
	}

	/**
	 * Execute the request send by the BIAlalysis GUI
	 * 
	 * @return
	 */
	public String count(String str) {
		int columns = 1;
		int fisrtColumn = 1;
		ObjectMapper objWriter = new ObjectMapper();
		ResultSetMetaData metadata;
		String jsonString = "";
		try {
			st = con.createStatement();
			result = st.executeQuery(str);
			metadata = result.getMetaData();
			while (result.next()) {
				while (columns <= metadata.getColumnCount()) {
					if (fisrtColumn == 1) {
						jsonString = result.getObject(columns).toString();
						fisrtColumn++;
					} else {
						jsonString = jsonString + "," + result.getObject(columns).toString();
					}
					columns++;
				}
				columns = 1;
			}
			jsonString = objWriter.writeValueAsString(jsonString);
		} catch (SQLException | JsonProcessingException e) {
			logger.log(Level.DEBUG, "Impossible to get Sensor datas from BDD " + e.getClass().getCanonicalName());
		}
		return jsonString;
	}

	/**
	 * Transform the result of the request in one Employee object
	 * 
	 * @param result
	 * @throws NumberFormatException
	 * @throws SQLException
	 */
	public void convertDatas(ResultSet result) throws NumberFormatException, SQLException {
		employee = new Employee();
		employee.setIdEmployee(result.getInt("id_employee"));
		employee.setLastnameEmployee(result.getString("nom_employee"));
		employee.setNameEmployee(result.getString("prenom_employee"));
		employee.setPassword(result.getString("mot_de_passe"));
		employee.setFunction(result.getString("poste"));
		logger.log(Level.DEBUG, "Convertion resultSet into employee java object succeed");
	}
}