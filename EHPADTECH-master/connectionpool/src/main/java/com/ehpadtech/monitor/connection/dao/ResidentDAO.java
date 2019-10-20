package com.ehpadtech.monitor.connection.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.ehpadtech.monitor.commons.entity.Resident;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


public class ResidentDAO extends DAO<Resident> {

	/**
	 * Initialization of parameters
	 */
	private String request;
	private Resident resident;
	private Resident resid;;
	private ResultSet result = null;
	private StringBuilder requestSB;
	private Statement st;
	private PreparedStatement prepareStatement;
	private static final Logger logger = LogManager.getLogger(ResidentDAO.class);

	public ResidentDAO(Connection con) {
		super(con);
	}

	/**
	 * Convert the JSON string in Object and create a request to insert values in
	 * table 'resident' return a boolean
	 */
	@Override
	public boolean create(String jsonString) {
		boolean result = false;
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			resid = objectMapper.readValue(jsonString, Resident.class);
			prepareStatement = con
					.prepareStatement("INSERT INTO resident (nom_resident, prenom_resident) values (?,?)");
			prepareStatement.setString(1, resid.getLastnameResident());
			prepareStatement.setString(2, resid.getNameResident());
			result = prepareStatement.execute();
			logger.log(Level.DEBUG, "Resident succesfully inserted in BDD");
		} catch (IOException | SQLException e) {
			logger.log(Level.WARN, "Impossible to insert resident datas in BDD " + e.getClass().getCanonicalName());
		}
		return result;
	}

	/**
	 * Convert the JSON string in Object and create a request to delete values in
	 * table 'resident' return a boolean
	 */
	@Override
	public boolean delete(String jsonString) {
		boolean result = false;
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			resid = objectMapper.readValue(jsonString, Resident.class);
			requestSB = new StringBuilder("DELETE FROM resident where id_resident= ");
			requestSB.append(resid.getIdResident());
			st = con.createStatement();
			result = st.execute(requestSB.toString());
			logger.log(Level.DEBUG, "Resident succesfully deleted in BDD");
		} catch (SQLException | IOException e) {
			logger.log(Level.WARN, "Impossible to delete resident datas in BDD " + e.getClass().getCanonicalName());
		}
		return result;
	}

	/**
	 * Convert the JSON string in Object and create a request to update values in
	 * table 'resident' return a boolean
	 */
	@Override
	public boolean update(String jsonString) {
		boolean result = false;
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			resid = objectMapper.readValue(jsonString, Resident.class);
			prepareStatement = con.prepareStatement(
					"UPDATE resident SET nom_resident = ?, prenom_resident = ? where id_resident = ?");
			prepareStatement.setString(1, resid.getLastnameResident());
			prepareStatement.setString(2, resid.getNameResident());
			prepareStatement.setInt(3, resid.getIdResident());
			result = prepareStatement.execute();
			logger.log(Level.DEBUG, "Resident succesfully update in BDD");
		} catch (SQLException | IOException e) {
			logger.log(Level.WARN, "Impossible to update resident datas in BDD" + e.getClass().getCanonicalName());
		}
		return result;
	}

	/**
	 * Convert the JSON string in Object and create a request to read (select)
	 * values in table 'resident' return a JSON string
	 */
	@Override
	public String read(String jsonString) {
		ObjectMapper objectMapper = new ObjectMapper();
		ObjectMapper objWriter = new ObjectMapper();
		try {
			resid = objectMapper.readValue(jsonString, Resident.class);
			requestSB = new StringBuilder("SELECT id_resident,nom_resident, prenom_resident ");
			requestSB.append("FROM resident where id_resident=");
			requestSB.append(resid.getIdResident());
			st = con.createStatement();
			result = st.executeQuery(requestSB.toString());
			result.next();
			convertDatas(result);
			jsonString = objWriter.writeValueAsString(resident);
			logger.log(Level.DEBUG, "Resident succesfully find in BDD");
		} catch (SQLException | IOException e) {
			logger.log(Level.WARN, "Impossible to get resident datas from BDD " + e.getClass().getCanonicalName());
			jsonString = "ERROR";
		}
		return jsonString;
	}

	/**
	 * Convert the JSON string in Object and create a request to read (select) all
	 * values in table 'resident' return a JSON string
	 */
	@Override
	public String readAll(String jsonString) {
		ObjectMapper objWriter = new ObjectMapper();
		List<Resident> listResident = new ArrayList<>();
		try {
			request = "SELECT id_resident,nom_resident,prenom_resident FROM resident";
			st = con.createStatement();
			result = st.executeQuery(request);
			while (result.next()) {
				convertDatas(result);
				listResident.add(resident);
			}
			jsonString = objWriter.writeValueAsString(listResident);
			logger.log(Level.DEBUG, "Residents succesfully find in BDD");
		} catch (SQLException | IOException e) {
			logger.log(Level.WARN, "Impossible to get resident datas from BDD " + e.getClass().getCanonicalName());
			jsonString = "ERROR";
		}
		return jsonString;
	}

	/**
	 * Convert the JSON string in Object and create a request to read (select) all
	 * values in table 'resident' by the name or lastName
	 * 
	 * @param jsonString
	 * @return jsonString
	 */
	public String findByName(String jsonString) {
		ObjectMapper objWriter = new ObjectMapper();
		ObjectMapper objectMapper = new ObjectMapper();
		List<Resident> listResident = new ArrayList<>();
		try {
			resid = objectMapper.readValue(jsonString, Resident.class);
			prepareStatement = con.prepareStatement(
					"SELECT id_resident,nom_resident, prenom_resident FROM resident where ((nom_resident LIKE ?) or (prenom_resident LIKE ?) or (prenom_resident LIKE ?))");
			prepareStatement.setString(1, "%" + resid.getLastnameResident().toUpperCase() + "%");
			prepareStatement.setString(2, "%" + resid.getLastnameResident().toLowerCase() + "%");
			prepareStatement.setString(3, "%" + resid.getLastnameResident().toUpperCase() + "%");
			result = prepareStatement.executeQuery();
			while (result.next()) {
				convertDatas(result);
				listResident.add(resident);
			}
			jsonString = objWriter.writeValueAsString(listResident);
			logger.log(Level.DEBUG, "Residents succesfully find in BDD");
		} catch (SQLException | IOException e) {
			logger.log(Level.WARN, "Impossible to get residents datas from BDD " + e.getClass().getCanonicalName());
			jsonString = "ERROR";
		}
		return jsonString;
	}

	/**
	 * Insert on table 'badger' when the resident badge in the elevator
	 * 
	 * @param idResident
	 * @param idSensor
	 * @return result
	 */
	public boolean badger(int idResident, int idSensor) {
		boolean result = false;
		Date currentDate = new Date();
		Format formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			requestSB = new StringBuilder("INSERT INTO badger (id_resident, id_capteur, date_badger) values (");
			requestSB.append(idResident);
			requestSB.append(",");
			requestSB.append(idSensor);
			requestSB.append(",'");
			requestSB.append(formater.format(currentDate));
			requestSB.append("')");
			st = con.createStatement();
			result = st.execute(requestSB.toString());
			logger.log(Level.DEBUG, "Resident succesfully inserted in BDD");
		} catch (SQLException e) {
			logger.log(Level.WARN, "Impossible to insert resident datas in BDD" + e.getClass().getCanonicalName());
		}
		return result;
	}

	/**
	 * Execute the request send by the Analysis GUI
	 
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
	 * Transform the result of the request in one Resident object
	 * 
	 * @param result
	 * @throws NumberFormatException
	 * @throws SQLException
	 */
	public void convertDatas(ResultSet result) throws NumberFormatException, SQLException {
		resident = new Resident();
		resident.setIdResident(result.getInt("id_resident"));
		resident.setLastnameResident(result.getString("nom_resident"));
		resident.setNameResident(result.getString("prenom_resident"));
		logger.log(Level.DEBUG, "Convertion resultSet into resident java object succeed");
	}
}