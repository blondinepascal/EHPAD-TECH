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
import com.ehpadtech.monitor.commons.entity.CommonArea;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


public class CommonAreaDAO extends DAO<CommonArea> {

	/**
	 * Initialization of parameters
	 */
	private CommonArea commonArea;
	private CommonArea area;
	private ResultSet result = null;
	private String request;
	private PreparedStatement prepareStatement;
	private Statement st;
	private StringBuilder requestSB;
	private static final Logger logger = LogManager.getLogger(CommonAreaDAO.class);

	/**
	 * Constructor
	 * 
	 * @param con
	 */
	public CommonAreaDAO(Connection con) {
		super(con);
	}

	/**
	 * Convert the JSON string in Object and create a request to insert values in
	 * table 'partie_commune' return a boolean
	 */
	@Override
	public boolean create(String jsonString) {
		boolean result = false;
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			area = objectMapper.readValue(jsonString, CommonArea.class);
			prepareStatement = con.prepareStatement(
					"INSERT INTO partie_commune (nom_partie_commune, etage_partie_commune,surface,max_capteur) values (?,?,?,?)");
			prepareStatement.setString(1, area.getNameCommonArea());
			prepareStatement.setInt(2, area.getFloorCommonArea());
			prepareStatement.setInt(3, area.getArea());
			prepareStatement.setInt(4, area.getMaxSensor());
			result = prepareStatement.execute();
			logger.log(Level.DEBUG, "CommonArea succesfully inserted in BDD");
		} catch (IOException | SQLException e) {
			logger.log(Level.WARN, "Impossible to insert commonArea datas in BDD " + e.getClass().getCanonicalName());
		}
		return result;
	}

	/**
	 * Convert the JSON string in Object and create a request to delete values in
	 * table 'partie_commune' return a boolean
	 */
	@Override
	public boolean delete(String jsonString) {
		boolean result = false;
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			area = objectMapper.readValue(jsonString, CommonArea.class);
			requestSB = new StringBuilder("DELETE FROM partie_commune where id_partie_commune=");
			requestSB.append(area.getIdCommonArea());
			st = con.createStatement();
			result = st.execute(requestSB.toString());
			logger.log(Level.DEBUG, "CommonArea succesfully deleted in BDD");
		} catch (SQLException | IOException e) {
			logger.log(Level.WARN, "Impossible to delete commonArea datas in BDD " + e.getClass().getCanonicalName());
		}
		return result;
	}

	/**
	 * Convert the JSON string in Object and create a request to update values in
	 * table 'partie_commune' return a boolean
	 */
	@Override
	public boolean update(String jsonString) {
		boolean result = false;
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			area = objectMapper.readValue(jsonString, CommonArea.class);
			prepareStatement = con.prepareStatement(
					"UPDATE partie_commune SET etage_partie_commune = ?, nom_partie_commune = ?, surface= ?, max_capteur= ? where id_partie_commune = ?");
			prepareStatement.setInt(1, area.getFloorCommonArea());
			prepareStatement.setString(2, area.getNameCommonArea());
			prepareStatement.setInt(3, area.getArea());
			prepareStatement.setInt(4, area.getMaxSensor());
			prepareStatement.setInt(5, area.getIdCommonArea());
			result = prepareStatement.execute();
			logger.log(Level.DEBUG, "CommonArea succesfully update in BDD");
		} catch (SQLException | IOException e) {
			logger.log(Level.WARN, "Impossible to update commonArea datas in BDD " + e.getClass().getCanonicalName());
		}
		return result;
	}

	/**
	 * Convert the JSON string in Object and create a request to read (select)
	 * values in table 'partie_commune' return a JSON string
	 */
	@Override
	public String read(String jsonString) {
		ObjectMapper objWriter = new ObjectMapper();
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			area = objectMapper.readValue(jsonString, CommonArea.class);
			requestSB = new StringBuilder(
					"SELECT id_partie_commune,nom_partie_commune, etage_partie_commune,surface,max_capteur ");
			requestSB.append("FROM partie_commune where id_partie_commune=");
			requestSB.append(area.getIdCommonArea());
			st = con.createStatement();
			result = st.executeQuery(requestSB.toString());
			result.next();
			convertDatas(result);
			jsonString = objWriter.writeValueAsString(commonArea);
			logger.log(Level.DEBUG, "CommonArea succesfully find in BDD");
		} catch (SQLException | IOException e) {
			logger.log(Level.WARN, "Impossible to get commonArea datas from BDD " + e.getClass().getCanonicalName());
			jsonString = "ERROR";
		}
		return jsonString;
	}

	/**
	 * Convert the JSON string in Object and create a request to read (select) all
	 * values in table 'partie_commune' return a JSON string
	 */
	@Override
	public String readAll(String jsonString) {
		ObjectMapper objWriter = new ObjectMapper();
		List<CommonArea> listCommonArea = new ArrayList<>();
		try {
			request = "SELECT id_partie_commune,nom_partie_commune, etage_partie_commune,surface,max_capteur FROM partie_commune;";
			st = con.createStatement();
			result = st.executeQuery(request);
			while (result.next()) {
				convertDatas(result);
				listCommonArea.add(commonArea);
			}
			jsonString = objWriter.writeValueAsString(listCommonArea);
			logger.log(Level.DEBUG, "CommonAreas succesfully find in BDD");
		} catch (SQLException | IOException e) {
			logger.log(Level.WARN, "Impossible to get commonArea datas from BDD " + e.getClass().getCanonicalName());
			jsonString = "ERROR";
		}
		return jsonString;
	}

	/**
	 * Convert the JSON string in Object and create a request to read (select) all
	 * values in table 'partie_commune' by the name or the stage of 'partie_commune'
	 * 
	 * @param jsonString
	 * @return
	 */
	public String findByName(String jsonString) {
		ObjectMapper objWriter = new ObjectMapper();
		ObjectMapper objectMapper = new ObjectMapper();
		List<CommonArea> listCommonArea = new ArrayList<>();
		try {
			area = objectMapper.readValue(jsonString, CommonArea.class);
			if (!(area.getNameCommonArea().equals(""))) {
				prepareStatement = con.prepareStatement(
						"SELECT id_partie_commune,nom_partie_commune, etage_partie_commune,surface,max_capteur FROM partie_commune where nom_partie_commune LIKE ?");
				prepareStatement.setString(1, "%" + area.getNameCommonArea().toUpperCase() + "%");
			} else {
				prepareStatement = con.prepareStatement(
						"SELECT id_partie_commune,nom_partie_commune, etage_partie_commune,surface,max_capteur FROM partie_commune where etage_partie_commune = ?");
				prepareStatement.setInt(1, area.getFloorCommonArea());
			}
			result = prepareStatement.executeQuery();
			while (result.next()) {
				convertDatas(result);
				listCommonArea.add(commonArea);
			}
			jsonString = objWriter.writeValueAsString(listCommonArea);
			logger.log(Level.DEBUG, "CommonAreas succesfully find in BDD");
		} catch (SQLException | IOException e) {
			logger.log(Level.WARN, "Impossible to get commonArea datas from BDD " + e.getClass().getCanonicalName());
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
	 * Convert the resultSet from the request into a CommonArea java object
	 * 
	 * @param result
	 * @throws NumberFormatException
	 * @throws SQLException
	 */
	public void convertDatas(ResultSet result) throws NumberFormatException, SQLException {
		commonArea = new CommonArea();
		commonArea.setIdCommonArea(result.getInt("id_partie_commune"));
		commonArea.setNameCommonArea(result.getString("nom_partie_commune"));
		commonArea.setFloorCommonArea(result.getInt("etage_partie_commune"));
		commonArea.setArea(result.getInt("surface"));
		commonArea.setMaxSensor(result.getInt("max_capteur"));
		commonArea.setListSensor(null);
		logger.log(Level.DEBUG, "Convertion resultSet into commonArea java object succeed");
	}
}