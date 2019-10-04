package com.ehpadtech.monitor.connection.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.ehpadtech.monitor.commons.entity.AlertState;
import com.ehpadtech.monitor.commons.entity.SensorHistorical;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;



public class SensorHistoricalDAO extends DAO<SensorHistorical> {
	
	/**
	 * Initialization of parameters
	 */
	private ResultSet result = null;
	private ResultSetMetaData metadata;
	private String request;
	private SensorHistorical sensorHistorical;
	private SensorHistorical sensorH;
	private StringBuilder requestSB;
	private Statement st;
	private PreparedStatement prepareStatement;
	private static final Logger logger = LogManager.getLogger(SensorHistoricalDAO.class);

	/**
	 * Constructor
	 * 
	 * @param con
	 */
	public SensorHistoricalDAO(Connection con) {
		super(con);
	}

	/**
	 * Convert the JSON string in Object and create a request to insert values in
	 * table 'historical' return a boolean
	 */
	@Override
	public boolean create(String jsonString) {
		boolean result = false;
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			sensorH = objectMapper.readValue(jsonString, SensorHistorical.class);
			prepareStatement = con.prepareStatement(
					"INSERT INTO historique (date_historique, etat_capteur, type_alerte, id_capteur) values (?,?,?,?)");
			prepareStatement.setTimestamp(1, new java.sql.Timestamp(sensorH.getDate().getTime()));
			if (sensorH.getSensorState())
				prepareStatement.setString(2, "ON");
			else
				prepareStatement.setString(2, "OFF");
			prepareStatement.setString(3, sensorH.getAlertState().name());
			prepareStatement.setInt(4, sensorH.getIdSensor());
			result = prepareStatement.execute();
			logger.log(Level.DEBUG, "SensorHistorical succesfully inserted in BDD");
		} catch (IOException | SQLException e) {
			logger.log(Level.WARN,
					"Impossible to insert sensorHistorical datas in BDD " + e.getClass().getCanonicalName());
		}
		return result;
	}

	/**
	 * Convert the JSON string in Object and create a request to delete values in
	 * table 'historical' return a boolean
	 */
	@Override
	public boolean delete(String jsonString) {
		boolean result = false;
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			sensorH = objectMapper.readValue(jsonString, SensorHistorical.class);
			requestSB = new StringBuilder("DELETE FROM historique where id_historique = ");
			requestSB.append(sensorH.getIdHistorical());
			st = con.createStatement();
			result = st.execute(requestSB.toString());
			logger.log(Level.DEBUG, "SensorHistorical succesfully deleted in BDD");
		} catch (SQLException | IOException e) {
			logger.log(Level.WARN,
					"Impossible to delete sensorHistorical datas  in BDD " + e.getClass().getCanonicalName());
		}
		return result;
	}

	/**
	 * Convert the JSON string in Object and create a request to update values in
	 * table 'historical' return a boolean
	 */
	@Override
	public boolean update(String jsonString) {
		boolean result = false;
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			sensorH = objectMapper.readValue(jsonString, SensorHistorical.class);
			prepareStatement = con.prepareStatement(
					"UPDATE historique SET id_capteur = ?, date_historique = ?, etat_capteur= ?, type_alerte= ? where id_historique = ?");
			prepareStatement.setInt(1, sensorH.getIdSensor());
			prepareStatement.setTimestamp(2, new java.sql.Timestamp(sensorH.getDate().getTime()));
			if (sensorH.getSensorState())
				prepareStatement.setString(3, "ON");
			else
				prepareStatement.setString(3, "OFF");
			prepareStatement.setString(4, sensorH.getAlertState().name());
			prepareStatement.setInt(5, sensorH.getIdHistorical());
			result = prepareStatement.execute();
			logger.log(Level.DEBUG, "Message succesfully update in BDD");
		} catch (SQLException | IOException e) {
			logger.log(Level.WARN, "Impossible to update message datas in BDD " + e.getClass().getCanonicalName());
		}
		return result;
	}

	/**
	 * Convert the JSON string in Object and create a request to read (select)
	 * values in table 'historical' return a JSON string
	 */
	@Override
	public String read(String jsonString) {
		ObjectMapper objWriter = new ObjectMapper();
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			sensorH = objectMapper.readValue(jsonString, SensorHistorical.class);
			requestSB = new StringBuilder(
					"SELECT id_historique,date_historique, etat_capteur, type_alerte, id_capteur ");
			requestSB.append("FROM historique where id_historique=");
			requestSB.append(sensorH.getIdHistorical());
			st = con.createStatement();
			result = st.executeQuery(requestSB.toString());
			result.next();
			convertDatas(result);
			jsonString = objWriter.writeValueAsString(sensorHistorical);
			logger.log(Level.DEBUG, "SensorHistorical succesfully find in BDD");
		} catch (SQLException | IOException | ParseException e) {
			logger.log(Level.WARN,
					"Impossible to get sensorHistorical datas from BDD " + e.getClass().getCanonicalName());
			jsonString = "ERROR";
		}
		return jsonString;
	}

	/**
	 * Convert the JSON string in Object and create a request to read (select) all
	 * values in table 'historical' return a JSON string
	 */
	@Override
	public String readAll(String jsonString) {
		ObjectMapper objWriter = new ObjectMapper();
		List<SensorHistorical> listSensorHistorical = new ArrayList<>();
		try {
			request = "SELECT id_historique,date_historique, etat_capteur, type_alerte, id_capteur FROM historique";
			st = con.createStatement();
			result = st.executeQuery(request);
			while (result.next()) {
				convertDatas(result);
				listSensorHistorical.add(sensorHistorical);
			}
			jsonString = objWriter.writeValueAsString(listSensorHistorical);
			logger.log(Level.DEBUG, "SensorHistorical succesfully find in BDD");
		} catch (SQLException | IOException | ParseException e) {
			logger.log(Level.WARN,
					"Impossible to get sensorHistorical datas from BDD " + e.getClass().getCanonicalName());
			jsonString = "ERROR";
		}
		return jsonString;
	}

	/**
	 * Convert the JSON string in Object and create a request to read (select) all
	 * values in table 'historical' by the id of commonArea or the type of sensor or
	 * the state of sensor
	 * 
	 * @param jsonString
	 * @return jsonString
	 */
	public String findBySensor(String jsonString) {
		ObjectMapper objWriter = new ObjectMapper();
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			sensorH = objectMapper.readValue(jsonString, SensorHistorical.class);
			requestSB = new StringBuilder(
					"SELECT id_historique,date_historique, etat_capteur, type_alerte, id_capteur ");
			requestSB.append("FROM historique where id_capteur=");
			requestSB.append(sensorH.getIdSensor());
			st = con.createStatement();
			result = st.executeQuery(requestSB.toString());
			result.next();
			convertDatas(result);
			jsonString = objWriter.writeValueAsString(sensorHistorical);
			logger.log(Level.DEBUG, "Sensor succesfully find in BDD");
		} catch (SQLException | IOException | ParseException e) {
			logger.log(Level.WARN, "Impossible to get sensor datas from BDD " + e.getClass().getCanonicalName());
			jsonString = "ERROR";
		}
		return jsonString;
	}

	/**
	 * Execute the request send by the Analysis GUI
	 * 
	 * @return
	 */
	public String count(String str) {
		int columns = 1;
		int fisrtColumn = 1;
		ObjectMapper objWriter = new ObjectMapper();
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
	 * Transform the result of the request in one SensorHistorique object
	 * 
	 * @param result
	 * @throws NumberFormatException
	 * @throws SQLException
	 * @throws ParseException
	 */
	public void convertDatas(ResultSet result) throws NumberFormatException, SQLException, ParseException {
		sensorHistorical = new SensorHistorical();
		sensorHistorical.setIdHistorical(result.getInt("id_historique"));
		sensorHistorical.setDate(result.getTimestamp("date_historique"));
		if (result.getString("etat_capteur").equals("ON"))
			sensorHistorical.setSensorState(true);
		else
			sensorHistorical.setSensorState(false);
		sensorHistorical.setAlertState(AlertState.valueOf(result.getString("type_alerte")));
		sensorHistorical.setIdSensor(result.getInt("id_capteur"));
		logger.log(Level.DEBUG, "Convertion resultSet into SensorHistorique java object succeed");
	}
}