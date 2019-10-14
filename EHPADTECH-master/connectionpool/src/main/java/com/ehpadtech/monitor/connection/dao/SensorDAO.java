package com.ehpadtech.monitor.connection.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.ehpadtech.monitor.commons.entity.AlertState;
import com.ehpadtech.monitor.commons.entity.Sensitivity;
import com.ehpadtech.monitor.commons.entity.Sensor;
import com.ehpadtech.monitor.commons.entity.SensorHistorical;
import com.ehpadtech.monitor.commons.entity.SensorType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
    

public class SensorDAO extends DAO<Sensor> {

	/**
	 * Initialization of parameters
	 */
	private Sensor sensor;
	private SensorHistorical sensorHistorical;
	private String request;
	private StringBuilder requestSB;
	private ResultSet result = null;
	private ResultSetMetaData metadata;
	private Calendar currentDate;
	private Statement st;
	private PreparedStatement prepareStatement;
	private static final Logger logger = LogManager.getLogger(SensorDAO.class);

	/**
	 * Constructor
	 * 
	 * @param con
	 */
	public SensorDAO(Connection con) {
		super(con);
	}

	/**
	 * Convert the JSON string in Object and create a request to insert values in
	 * table 'capteur' return a boolean
	 */
	@Override
	public boolean create(String jsonString) {
		boolean result = false;
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			currentDate = Calendar.getInstance(Locale.FRENCH);
			sensor = objectMapper.readValue(jsonString, Sensor.class);
			System.out.println("données en cours de serialisation");
			prepareStatement = con.prepareStatement(
					"INSERT INTO capteur (type_capteur, etat, id_partie_commune,type_alert,sensibilite,heure_debut,heure_fin,seuil_min,mise_a_jour,seuil_max) values (?,?,?,?,?,?,?,?,?,?)");
			prepareStatement.setString(1, sensor.getTypeSensor().name());
			prepareStatement.setString(2, "ON");
			if (sensor.getSensorState())
				prepareStatement.setString(2, "ON");
			else
				prepareStatement.setString(2, "OFF");
			prepareStatement.setInt(3, sensor.getIdCommonArea());
			prepareStatement.setString(4, sensor.getAlertState().name());
			prepareStatement.setString(5, sensor.getSensitivity().name());
			prepareStatement.setTime(6, sensor.getStartActivity());
			prepareStatement.setTime(7, sensor.getEndActivity());
			prepareStatement.setInt(8, sensor.getThresholdMin());
			prepareStatement.setTimestamp(9, new java.sql.Timestamp(currentDate.getTime().getTime()));
			prepareStatement.setInt(10, sensor.getThresholdMax());
			result = prepareStatement.execute();
			System.out.println("données sérialisées");
			logger.log(Level.DEBUG, "Sensor succesfully inserted in BDD");
		} catch (IOException | SQLException e) {
			logger.log(Level.WARN, "Impossible to insert sensor datas in BDD" + e.getClass().getCanonicalName());
		}
		return result;
	}

	/**
	 * Convert the JSON string in Object and create a request to delete values in
	 * table 'capteur' return a boolean
	 */
	@Override
	public boolean delete(String jsonString) {
		boolean result = false;
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			sensor = objectMapper.readValue(jsonString, Sensor.class);
			System.out.println("données en cours de sérialisation");
			requestSB = new StringBuilder("DELETE FROM capteur where id_capteur=");
			requestSB.append(sensor.getIdSensor());
			st = con.createStatement();
			result = st.execute(requestSB.toString());
			logger.log(Level.DEBUG, "Sensor succesfully deleted in BDD");
			System.out.println("données sérialisées");
		} catch (SQLException | IOException e) {
			logger.log(Level.WARN, "Impossible to delete sensor data in BDD" + e.getClass().getCanonicalName());
		}
		return result;
	}

	/**
	 * Convert the JSON string in Object and create a request to update values in
	 * table 'capteur' return a boolean
	 */
	@Override
	public boolean update(String jsonString) {
		boolean result = false;
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			currentDate = Calendar.getInstance(Locale.FRENCH);
			System.out.println("données en cours de sérialisation");
			sensor = objectMapper.readValue(jsonString, Sensor.class);
			prepareStatement = con.prepareStatement(
					"UPDATE capteur SET id_partie_commune = ?,type_capteur = ?,type_alert= ?,sensibilite= ?,heure_debut= ?,heure_fin= ?,seuil_min= ?,seuil_max= ?,mise_a_jour= ?,etat= ?  where id_capteur = ?");
			prepareStatement.setInt(1, sensor.getIdCommonArea());
			prepareStatement.setString(2, sensor.getTypeSensor().name());
			prepareStatement.setString(3, sensor.getAlertState().name());
			prepareStatement.setString(4, sensor.getSensitivity().name());
			prepareStatement.setTime(5, sensor.getStartActivity());
			prepareStatement.setTime(6, sensor.getEndActivity());
			prepareStatement.setInt(7, sensor.getThresholdMin());
			prepareStatement.setInt(8, sensor.getThresholdMax());
			prepareStatement.setTimestamp(9, new java.sql.Timestamp(currentDate.getTime().getTime()));
			if (sensor.getSensorState())
				prepareStatement.setString(10, "ON");
			else
				prepareStatement.setString(10, "OFF");
			prepareStatement.setInt(11, sensor.getIdSensor());
			result = prepareStatement.execute();
			logger.log(Level.DEBUG, "Sensor succesfully update in BDD");
			System.out.println("données sérialisées");

			sensorHistorical = new SensorHistorical();
			sensorHistorical.setIdSensor(sensor.getIdSensor());
			sensorHistorical.setSensorState(sensor.getSensorState());
			sensorHistorical.setAlertState(sensor.getAlertState());
			sensorHistorical.setDate(currentDate.getTime());
			jsonString = objectMapper.writeValueAsString(sensorHistorical);
			DAO<SensorHistorical> sensorHistoricalDao = new SensorHistoricalDAO(con);
			((SensorHistoricalDAO) sensorHistoricalDao).create(jsonString);
			logger.log(Level.DEBUG, "SensorHistorical succesfully inserted in BDD");
		} catch (SQLException | IOException e) {
			logger.log(Level.WARN, "Impossible to update sensor datas in BDD" + e.getClass().getCanonicalName());
		}
		return result;
	}

	/**
	 * Convert the JSON string in Object and create a request to read (select)
	 * values in table 'capteur' return a JSON string
	 */
	@Override
	public String read(String jsonString) {
		ObjectMapper objectMapper = new ObjectMapper();
		ObjectMapper objWriter = new ObjectMapper();
		try {
			sensor = objectMapper.readValue(jsonString, Sensor.class);
			requestSB = new StringBuilder(
					"SELECT id_capteur,type_capteur, etat, id_partie_commune,type_alert,sensibilite,heure_debut,heure_fin,seuil_min,mise_a_jour,seuil_max ");
			requestSB.append("FROM capteur where id_capteur=");
			requestSB.append(sensor.getIdSensor());
			st = con.createStatement();
			result = st.executeQuery(requestSB.toString());
			result.next();
			convertDatas(result);
			jsonString = objWriter.writeValueAsString(sensor);
			logger.log(Level.DEBUG, "Sensor succesfully find in BDD");
		} catch (SQLException | IOException | NumberFormatException | ParseException e) {
			logger.log(Level.WARN, "Impossible to get sensor datas from BDD " + e.getClass().getCanonicalName());
			jsonString = "ERROR";
		}
		return jsonString;
	}

	/**
	 * Convert the JSON string in Object and create a request to read (select) all
	 * values in table 'capteur' return a JSON string
	 */
	@Override
	public String readAll(String jsonString) {
		ObjectMapper obj = new ObjectMapper();
		List<Sensor> listSensor = new ArrayList<>();
		try {
			request = "SELECT id_capteur,type_capteur, etat, id_partie_commune,type_alert,sensibilite,heure_debut,heure_fin,seuil_min,mise_a_jour,seuil_max FROM capteur";
			st = con.createStatement();
			result = st.executeQuery(request);
			while (result.next()) {
				convertDatas(result);
				listSensor.add(sensor);
			}
			jsonString = obj.writeValueAsString(listSensor);
			logger.log(Level.DEBUG, "Sensors succesfully find in BDD");
		} catch (SQLException | IOException | NumberFormatException | ParseException e) {
			logger.log(Level.WARN, "Impossible to get sensor datas from BDD " + e.getClass().getCanonicalName());
			jsonString = "ERROR";
		}
		return jsonString;
	}

	/**
	 * Convert the JSON string in Object and create a request to read (select) all
	 * values in table 'capteur' by the type of sensor or the id of commonArea
	 * 
	 * @param jsonString
	 * @return jsonString
	 */
	public String findAll(String jsonString) {
		ObjectMapper objectMapper = new ObjectMapper();
		ObjectMapper objWriter = new ObjectMapper();
		List<Sensor> listSensor = new ArrayList<>();
		try {
			sensor = objectMapper.readValue(jsonString, Sensor.class);
			requestSB = new StringBuilder(
					"SELECT id_capteur,type_capteur, etat, id_partie_commune,type_alert,sensibilite,heure_debut,heure_fin,seuil_min,mise_a_jour,seuil_max ");
			if (sensor.getTypeSensor() != null) {
				requestSB.append("FROM capteur where type_capteur ='");
				requestSB.append(sensor.getTypeSensor());
				requestSB.append("'");
			} else {
				requestSB.append("FROM capteur where id_partie_commune =");
				requestSB.append(sensor.getIdCommonArea());
			}
			st = con.createStatement();
			result = st.executeQuery(requestSB.toString());
			while (result.next()) {
				convertDatas(result);
				listSensor.add(sensor);
			}
			jsonString = objWriter.writeValueAsString(listSensor);
			logger.log(Level.DEBUG, "Sensors succesfully find in BDD");
		} catch (SQLException | IOException | NumberFormatException | ParseException e) {
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
	 * Transform the result of the request in one Sensor object
	 * 
	 * @param result
	 * @throws NumberFormatException
	 * @throws SQLException
	 * @throws ParseException
	 */
	public void convertDatas(ResultSet result) throws NumberFormatException, SQLException, ParseException {
		sensor = new Sensor();
		sensor.setIdSensor(result.getInt("id_capteur"));
		sensor.setTypeSensor(SensorType.valueOf(result.getString("type_capteur")));
		if (result.getString("etat").equals("ON")) {
			sensor.setSensorState(true);
		} else if (result.getString("etat").toString().equals("OFF")) {
			sensor.setSensorState(false);
		}
		sensor.setIdCommonArea(result.getInt("id_partie_commune"));
		sensor.setAlertState(AlertState.valueOf(result.getString("type_alert")));
		sensor.setSensitivity(Sensitivity.valueOf(result.getString("sensibilite")));
		sensor.setStartActivity(result.getTime("heure_debut"));
		sensor.setEndActivity(result.getTime("heure_fin"));
		sensor.setThresholdMin(result.getInt("seuil_min"));
		sensor.setLastUpdate(result.getTimestamp("mise_a_jour"));
		sensor.setThresholdMax(result.getInt("seuil_max"));

		if (sensor.getIdCommonArea() != 0
				&& (!sensor.getEndActivity().equals(Time.valueOf("00:00:00"))
						|| sensor.getStartActivity().equals(Time.valueOf("00:00:00")))
				&& sensor.getSensitivity() != null && sensor.getTypeSensor() != null
				&& (sensor.getThresholdMin() != 0 || sensor.getThresholdMax() != 0)) {
			sensor.setConfigured(true);
		}
		logger.log(Level.DEBUG, "Convertion resultSet into Sensor java object succeed");
	}
}