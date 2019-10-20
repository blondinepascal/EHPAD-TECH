package com.ehpadtech.monitor.connection.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Connection;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.ehpadtech.monitor.connection.dao.CommonAreaDAO;
import com.ehpadtech.monitor.connection.dao.DAO;
import com.ehpadtech.monitor.connection.dao.EmployeeDAO;
import com.ehpadtech.monitor.connection.dao.MessageDAO;
import com.ehpadtech.monitor.connection.dao.ResidentDAO;
import com.ehpadtech.monitor.connection.dao.SensorDAO;
import com.ehpadtech.monitor.connection.dao.SensorHistoricalDAO;
import com.ehpadtech.monitor.connection.monitoring.MonitoringAlert;
import com.ehpadtech.monitor.commons.entity.CommonArea;
import com.ehpadtech.monitor.commons.entity.Employee;
import com.ehpadtech.monitor.commons.entity.Message;
import com.ehpadtech.monitor.commons.entity.Resident;
import com.ehpadtech.monitor.commons.entity.Sensor;
import com.ehpadtech.monitor.commons.entity.SensorHistorical;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;



public class RequestHandler implements Runnable {

	/**
	 * Initialization of differents parameters
	 */
	private Socket sock;
	private PrintWriter writer = null; 
	private BufferedReader reader = null;
	private String response;
	private String jsonString;
	private static final Logger logger = LogManager.getLogger(RequestHandler.class);
	private ObjectMapper objectMapper;
	private JsonNode jsonNode;
	private boolean result;
	private Connection connection;
	private MonitoringAlert monitoringAlert;

	/**
	 * Constructor
	 * 
	 * @param pSock
	 * @param connection
	 * @param monitoringAlert
	 */
	public RequestHandler(Socket pSock, Connection connection, MonitoringAlert monitoringAlert) {
		this.sock = pSock;
		this.connection = connection;
		this.monitoringAlert = monitoringAlert;
	}

	/**
	 * Run the Thread while the client connection is active they loop when
	 * connection is not closed
	 */
	public void run() {
		logger.log(Level.DEBUG, "Launch of treatement of client connection");
		while (!sock.isClosed()) {
			try {
				writer = new PrintWriter(sock.getOutputStream(), true);
				reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));

				objectMapper = new ObjectMapper();

				/**
				 * wait the request from client
				 */
				response = reader.readLine();
				if (response.equals("OPEN")) {
					response = "OK FOR EXCHANGE";
					writer.println(response);
					/**
					 * wait the requestType and the table from client and choose witch case they
					 * will execute with the requestType and the table
					 */
					response = reader.readLine();
					if (!response.equals("")) {
						jsonNode = objectMapper.readTree(response);
						switch (jsonNode.get("request").asText()) {
						case "COUNT":
							response = "OK FOR REQUEST COUNT";
							writer.println(response);
							logger.log(Level.DEBUG, "Request Type accepted by server");
							response = reader.readLine();
							switch (jsonNode.get("table").asText()) {
							case "CommonArea":
								if (!response.equals("")) {
									DAO<CommonArea> commonAreaDao = new CommonAreaDAO(connection);
									jsonString = ((CommonAreaDAO) commonAreaDao).count(response);
									writer.println(jsonString);
									logger.log(Level.DEBUG, "Response send to client");
								} else {
									logger.log(Level.WARN, "Request not recognized");
								}
								break;
							case "SensorHistorical":
								if (!response.equals("")) {
									DAO<SensorHistorical> sensorHistoricalDao = new SensorHistoricalDAO(connection);
									jsonString = ((SensorHistoricalDAO) sensorHistoricalDao).count(response);
									writer.println(jsonString);
									logger.log(Level.DEBUG, "Response send to client");
								} else {
									logger.log(Level.WARN, "Request not recognized");
								}
								break;
							case "Message":
								if (!response.equals("")) {
									DAO<Message> messageDao = new MessageDAO(connection);
									jsonString = ((MessageDAO) messageDao).count(response);
									writer.println(jsonString);
									logger.log(Level.DEBUG, "Response send to client");
								} else {
									logger.log(Level.WARN, "Request not recognized");
								}
								break;
							case "Sensor":
								if (!response.equals("")) {
									DAO<Sensor> sensorDao = new SensorDAO(connection);
									jsonString = ((SensorDAO) sensorDao).count(response);
									writer.println(jsonString);
									logger.log(Level.DEBUG, "Response send to client");
								} else {
									logger.log(Level.WARN, "Request not recognized");
								}
								break;
							}
							break;
						case "GET ALERT":
							response = "OK FOR REQUEST GET ALERT";
							writer.println(response);
							logger.log(Level.DEBUG, "Request Type accepted by server");
							response = reader.readLine();
							if (!response.equals("")) {
								objectMapper = new ObjectMapper();
								jsonString = objectMapper.writeValueAsString(monitoringAlert.getListAlert());
								writer.println(jsonString);
								logger.log(Level.DEBUG, "Response send to client");
							} else {
								logger.log(Level.WARN, "Request not recognized");
							}
							break;
						case "ALERT":
							response = "OK FOR REQUEST ALERT";
							writer.println(response);
							response = reader.readLine();
							if (!response.equals("")) {
								objectMapper = new ObjectMapper();
								Message messages = objectMapper.readValue(response, Message.class);
								monitoringAlert.addListMessage(messages, monitoringAlert.getListMessage());
								logger.log(Level.INFO, "One message Received from sensor : " + messages.getIdSensor());
								jsonString = "ADD";
								writer.println(jsonString);
							} else {
								logger.log(Level.WARN, "Request not recognized");
							}
							break;
						case "FIND ALL":
							response = "OK FOR REQUEST FIND ALL";
							writer.println(response);
							logger.log(Level.DEBUG, "Request Type accepted by server");

							response = reader.readLine();
							switch (jsonNode.get("table").asText()) {
							case "Sensor":
								if (!response.equals("")) {
									logger.log(Level.DEBUG, "Request received on server");
									DAO<Sensor> sensorDao = new SensorDAO(connection);
									jsonString = ((SensorDAO) sensorDao).findAll(response);
									writer.println(jsonString);
									logger.log(Level.DEBUG, "Response send to client");
								} else {
									logger.log(Level.WARN, "Request not recognized");
								}
								break;
							case "Employee":
								if (!response.equals("")) {
									logger.log(Level.DEBUG, "Request received on server");
									DAO<Employee> employeeDao = new EmployeeDAO(connection);
									jsonString = ((EmployeeDAO) employeeDao).findByName(response);
									writer.println(jsonString);
									logger.log(Level.DEBUG, "Response send to client");
								} else {
									logger.log(Level.WARN, "Request not recognized");
								}
								break;
							case "Resident":
								if (!response.equals("")) {
									logger.log(Level.DEBUG, "Request received on server");
									DAO<Resident> residentDao = new ResidentDAO(connection);
									jsonString = ((ResidentDAO) residentDao).findByName(response);
									writer.println(jsonString);
									logger.log(Level.DEBUG, "Response send to client");
								} else {
									logger.log(Level.WARN, "Request not recognized");
								}
								break;
							case "CommonArea":
								if (!response.equals("")) {
									logger.log(Level.DEBUG, "Request received on server");
									DAO<CommonArea> commonAreaDao = new CommonAreaDAO(connection);
									jsonString = ((CommonAreaDAO) commonAreaDao).findByName(response);
									writer.println(jsonString);
									logger.log(Level.DEBUG, "Response send to client");
								} else {
									logger.log(Level.WARN, "Request not recognized");
								}
								break;
							case "SensorHistorical":
								if (!response.equals("")) {
									logger.log(Level.DEBUG, "Request received on server");
									DAO<SensorHistorical> sensorHistoricalDao = new SensorHistoricalDAO(connection);
									jsonString = ((SensorHistoricalDAO) sensorHistoricalDao).findBySensor(response);
									writer.println(jsonString);
									logger.log(Level.DEBUG, "Response send to client");
								} else {
									logger.log(Level.WARN, "Request not recognized");
								}
								break;
							}
							break;
						case "CONNECTION":
							response = "OK FOR REQUEST CONNECTION";
							writer.println(response);
							logger.log(Level.DEBUG, "Request Type accepted by server");

							response = reader.readLine();
							if (!response.equals("")) {
								logger.log(Level.DEBUG, "Request received on server");
								DAO<Employee> employeeDao = new EmployeeDAO(connection);
								jsonString = ((EmployeeDAO) employeeDao).connection(response);
								writer.println(jsonString);
								logger.log(Level.DEBUG, "Response send to client");
							} else {
								logger.log(Level.WARN, "Request not recognized");
							}
							break;
						case "CREATE":
							response = "OK FOR REQUEST CREATE";
							writer.println(response);
							logger.log(Level.DEBUG, "Request Type accepted by server");

							response = reader.readLine();
							switch (jsonNode.get("table").asText()) {
							case "Employee":
								if (!response.equals("")) {
									logger.log(Level.DEBUG, "Request received on server");
									DAO<Employee> employeeDao = new EmployeeDAO(connection);
									setResult(((EmployeeDAO) employeeDao).create(response));
									jsonString = "INSERTED";
									writer.println(jsonString);
									logger.log(Level.DEBUG, "Response send to client");
								} else {
									logger.log(Level.WARN, "Request not recognized");
								}
								break;
							case "Resident":
								if (!response.equals("")) {
									logger.log(Level.DEBUG, "Request received on server");
									DAO<Resident> residentDao = new ResidentDAO(connection);
									setResult(((ResidentDAO) residentDao).create(response));
									jsonString = "INSERTED";
									writer.println(jsonString);
									logger.log(Level.DEBUG, "Response send to client");
								} else {
									logger.log(Level.WARN, "Request not recognized");
								}
								break;
							case "Sensor":
								if (!response.equals("")) {
									logger.log(Level.DEBUG, "Request received on server");
									DAO<Sensor> sensorDao = new SensorDAO(connection);
									setResult(((SensorDAO) sensorDao).create(response));
									jsonString = "INSERTED";
									writer.println(jsonString);
									logger.log(Level.DEBUG, "Response send to client");
								} else {
									logger.log(Level.WARN, "Request not recognized");
								}
								break;
							case "CommonArea":
								if (!response.equals("")) {
									logger.log(Level.DEBUG, "Request received on server");
									DAO<CommonArea> commonAreaDao = new CommonAreaDAO(connection);
									setResult(((CommonAreaDAO) commonAreaDao).create(response));
									jsonString = "INSERTED";
									writer.println(jsonString);
									logger.log(Level.DEBUG, "Response send to client");
								} else {
									logger.log(Level.WARN, "Request not recognized");
								}
								break;
							case "SensorHistorical":
								if (!response.equals("")) {
									logger.log(Level.DEBUG, "Request received on server");
									DAO<SensorHistorical> sensorHistoricalDao = new SensorHistoricalDAO(connection);
									setResult(((SensorHistoricalDAO) sensorHistoricalDao).create(response));
									jsonString = "INSERTED";
									writer.println(jsonString);
									logger.log(Level.DEBUG, "Response send to client");
								} else {
									logger.log(Level.WARN, "Request not recognized");
								}
								break;
							case "Message":
								if (!response.equals("")) {
									logger.log(Level.DEBUG, "Request received on server");
									DAO<Message> messageDao = new MessageDAO(connection);
									setResult(((MessageDAO) messageDao).create(response));
									jsonString = "INSERTED";
									writer.println(jsonString);
									logger.log(Level.DEBUG, "Response send to client");
								} else {
									logger.log(Level.WARN, "Request not recognized");
								}
								break;
							}
							break;
						case "UPDATE":
							response = "OK FOR REQUEST UPDATE";
							writer.println(response);
							logger.log(Level.DEBUG, "Request Type accepted by server");

							response = reader.readLine();
							switch (jsonNode.get("table").asText()) {
							case "Employee":
								if (!response.equals("")) {
									logger.log(Level.DEBUG, "Request received on server");
									DAO<Employee> employeeDao = new EmployeeDAO(connection);
									setResult(((EmployeeDAO) employeeDao).update(response));
									jsonString = "UPDATED";
									writer.println(jsonString);
									logger.log(Level.DEBUG, "Response send to client");
								} else {
									logger.log(Level.WARN, "Request not recognized");
								}
								break;
							case "Resident":
								if (!response.equals("")) {
									logger.log(Level.DEBUG, "Request received on server");
									DAO<Resident> residentDao = new ResidentDAO(connection);
									setResult(((ResidentDAO) residentDao).update(response));
									jsonString = "UPDATED";
									writer.println(jsonString);
									logger.log(Level.DEBUG, "Response send to client");
								} else {
									logger.log(Level.WARN, "Request not recognized");
								}
								break;
							case "Sensor":
								if (!response.equals("")) {
									logger.log(Level.DEBUG, "Request received on server");
									DAO<Sensor> sensorDao = new SensorDAO(connection);
									setResult(((SensorDAO) sensorDao).update(response));
									jsonString = "UPDATED";
									writer.println(jsonString);
									logger.log(Level.DEBUG, "Response send to client");
									monitoringAlert.addHistorical(response);
								} else {
									logger.log(Level.WARN, "Request not recognized");
								}
								monitoringAlert.deleteAlert(response);
								break;
							case "CommonArea":
								if (!response.equals("")) {
									logger.log(Level.DEBUG, "Request received on server");
									DAO<CommonArea> commonAreaDao = new CommonAreaDAO(connection);
									setResult(((CommonAreaDAO) commonAreaDao).update(response));
									jsonString = "UPDATED";
									writer.println(jsonString);
									logger.log(Level.DEBUG, "Response send to client");
								} else {
									logger.log(Level.WARN, "Request not recognized");
								}
								break;
							case "SensorHistorical":
								if (!response.equals("")) {
									logger.log(Level.DEBUG, "Request received on server");
									DAO<SensorHistorical> sensorHistoricalDao = new SensorHistoricalDAO(connection);
									setResult(((SensorHistoricalDAO) sensorHistoricalDao).update(response));
									jsonString = "UPDATED";
									writer.println(jsonString);
									logger.log(Level.DEBUG, "Response send to client");
								} else {
									logger.log(Level.WARN, "Request not recognized");
								}
								break;
							case "Message":
								if (!response.equals("")) {
									logger.log(Level.DEBUG, "Request received on server");
									DAO<Message> messageDao = new MessageDAO(connection);
									setResult(((MessageDAO) messageDao).update(response));
									jsonString = "UPDATED";
									writer.println(jsonString);
									logger.log(Level.DEBUG, "Response send to client");
								} else {
									logger.log(Level.WARN, "Request not recognized");
								}
								break;
							}
							break;
						case "DELETE":
							response = "OK FOR REQUEST DELETE";
							writer.println(response);
							logger.log(Level.DEBUG, "Request Type accepted by server");

							response = reader.readLine();
							switch (jsonNode.get("table").asText()) {
							case "Employee":
								if (!response.equals("")) {
									logger.log(Level.DEBUG, "Request received on server");
									DAO<Employee> employeeDao = new EmployeeDAO(connection);
									setResult(((EmployeeDAO) employeeDao).delete(response));
									jsonString = "DELETED";
									writer.println(jsonString);
									logger.log(Level.DEBUG, "Response send to client");
								} else {
									logger.log(Level.WARN, "Request not recognized");
								}
								break;
							case "Resident":
								if (!response.equals("")) {
									logger.log(Level.DEBUG, "Request received on server");
									DAO<Resident> residentDao = new ResidentDAO(connection);
									setResult(((ResidentDAO) residentDao).delete(response));
									jsonString = "DELETED";
									writer.println(jsonString);
									logger.log(Level.DEBUG, "Response send to client");
								} else {
									logger.log(Level.WARN, "Request not recognized");
								}
								break;
							case "Sensor":
								if (!response.equals("")) {
									logger.log(Level.DEBUG, "Request received on server");
									DAO<Sensor> sensorDao = new SensorDAO(connection);
									setResult(((SensorDAO) sensorDao).delete(response));
									jsonString = "DELETED";
									writer.println(jsonString);
									logger.log(Level.DEBUG, "Response send to client");
								} else {
									logger.log(Level.WARN, "Request not recognized");
								}
								monitoringAlert.deleteAlert(response);
								break;
							case "CommonArea":
								if (!response.equals("")) {
									logger.log(Level.DEBUG, "Request received on server");
									DAO<CommonArea> commonAreaDao = new CommonAreaDAO(connection);
									setResult(((CommonAreaDAO) commonAreaDao).delete(response));
									jsonString = "DELETED";
									writer.println(jsonString);
									logger.log(Level.DEBUG, "Response send to client");
								} else {
									logger.log(Level.WARN, "Request not recognized");
								}
								break;
							case "SensorHistorical":
								if (!response.equals("")) {
									logger.log(Level.DEBUG, "Request received on server");
									DAO<SensorHistorical> sensorHistoricalDao = new SensorHistoricalDAO(connection);
									setResult(((SensorHistoricalDAO) sensorHistoricalDao).delete(response));
									jsonString = "DELETED";
									writer.println(jsonString);
									logger.log(Level.DEBUG, "Response send to client");
								} else {
									logger.log(Level.WARN, "Request not recognized");
								}
								break;
							case "Message":
								if (!response.equals("")) {
									logger.log(Level.DEBUG, "Request received on server");
									DAO<Message> messageDao = new MessageDAO(connection);
									setResult(((MessageDAO) messageDao).delete(response));
									jsonString = "DELETED";
									writer.println(jsonString);
									logger.log(Level.DEBUG, "Response send to client");
								} else {
									logger.log(Level.WARN, "Request not recognized");
								}
								break;
							}
							break;
						case "READ":
							response = "OK FOR REQUEST READ";
							writer.println(response);
							logger.log(Level.DEBUG, "Request Type accepted by server");

							response = reader.readLine();
							switch (jsonNode.get("table").asText()) {
							case "Employee":
								if (!response.equals("")) {
									logger.log(Level.DEBUG, "Request received on server");
									DAO<Employee> employeeDao = new EmployeeDAO(connection);
									jsonString = ((EmployeeDAO) employeeDao).read(response);
									writer.println(jsonString);
									logger.log(Level.DEBUG, "Response send to client");
								} else {
									logger.log(Level.WARN, "Request not recognized");
								}
								break;
							case "Resident":
								if (!response.equals("")) {
									logger.log(Level.DEBUG, "Request received on server");
									DAO<Resident> residentDao = new ResidentDAO(connection);
									jsonString = ((ResidentDAO) residentDao).read(response);
									writer.println(jsonString);
									logger.log(Level.DEBUG, "Response send to client");
								} else {
									logger.log(Level.WARN, "Request not recognized");
								}
								break;
							case "Sensor":
								if (!response.equals("")) {
									logger.log(Level.DEBUG, "Request received on server");
									DAO<Sensor> sensorDao = new SensorDAO(connection);
									jsonString = ((SensorDAO) sensorDao).read(response);
									writer.println(jsonString);
									logger.log(Level.DEBUG, "Response send to client");
								} else {
									logger.log(Level.WARN, "Request not recognized");
								}
								break;
							case "CommonArea":
								if (!response.equals("")) {
									logger.log(Level.DEBUG, "Request received on server");
									DAO<CommonArea> commonAreaDao = new CommonAreaDAO(connection);
									jsonString = ((CommonAreaDAO) commonAreaDao).read(response);
									writer.println(jsonString);
									logger.log(Level.DEBUG, "Response send to client");
								} else {
									logger.log(Level.WARN, "Request not recognized");
								}
								break;
							case "SensorHistorical":
								if (!response.equals("")) {
									logger.log(Level.DEBUG, "Request received on server");
									DAO<SensorHistorical> sensorHistoricalDao = new SensorHistoricalDAO(connection);
									jsonString = ((SensorHistoricalDAO) sensorHistoricalDao).read(response);
									writer.println(jsonString);
									logger.log(Level.DEBUG, "Response send to client");
								} else {
									logger.log(Level.WARN, "Request not recognized");
								}
								break;
							case "Message":
								if (!response.equals("")) {
									logger.log(Level.DEBUG, "Request received on server");
									DAO<Message> messageDao = new MessageDAO(connection);
									jsonString = ((MessageDAO) messageDao).read(response);
									writer.println(jsonString);
									logger.log(Level.DEBUG, "Response send to client");
								} else {
									logger.log(Level.WARN, "Request not recognized");
								}
								break;
							}
							break;
						case "READ ALL":
							response = "OK FOR REQUEST READ ALL";
							writer.println(response);
							logger.log(Level.DEBUG, "Request Type accepted by server");

							response = reader.readLine();
							switch (jsonNode.get("table").asText()) {
							case "Employee":
								if (!response.equals("")) {
									logger.log(Level.DEBUG, "Request received on server");
									DAO<Employee> employeeDao = new EmployeeDAO(connection);
									jsonString = ((EmployeeDAO) employeeDao).readAll(response);
									writer.println(jsonString);
									logger.log(Level.DEBUG, "Response send to client");
								} else {
									logger.log(Level.WARN, "Request not recognized");
								}
								break;
							case "Resident":
								if (!response.equals("")) {
									logger.log(Level.DEBUG, "Request received on server");
									DAO<Resident> residentDao = new ResidentDAO(connection);
									jsonString = ((ResidentDAO) residentDao).readAll(response);
									writer.println(jsonString);
									logger.log(Level.DEBUG, "Response send to client");
								} else {
									logger.log(Level.WARN, "Request not recognized");
								}
								break;
							case "Sensor":
								if (!response.equals("")) {
									logger.log(Level.DEBUG, "Request received on server");
									DAO<Sensor> sensorDao = new SensorDAO(connection);
									jsonString = ((SensorDAO) sensorDao).readAll(response);
									writer.println(jsonString);
									logger.log(Level.DEBUG, "Response send to client");
								} else {
									logger.log(Level.WARN, "Request not recognized");
								}
								break;
							case "CommonArea":
								if (!response.equals("")) {
									logger.log(Level.DEBUG, "Request received on server");
									DAO<CommonArea> commonAreaDao = new CommonAreaDAO(connection);
									jsonString = ((CommonAreaDAO) commonAreaDao).readAll(response);
									writer.println(jsonString);
									logger.log(Level.DEBUG, "Response send to client");
								} else {
									logger.log(Level.WARN, "Request not recognized");
								}
								break;
							case "SensorHistorical":
								if (!response.equals("")) {
									logger.log(Level.DEBUG, "Request received on server");
									DAO<SensorHistorical> sensorHistoricalDao = new SensorHistoricalDAO(connection);
									jsonString = ((SensorHistoricalDAO) sensorHistoricalDao).readAll(response);
									writer.println(jsonString);
									logger.log(Level.DEBUG, "Response send to client");
								} else {
									logger.log(Level.WARN, "Request not recognized");
								}
								break;
							case "Message":
								if (!response.equals("")) {
									logger.log(Level.DEBUG, "Request received on server");
									DAO<Message> messageDao = new MessageDAO(connection);
									jsonString = ((MessageDAO) messageDao).readAll(response);
									writer.println(jsonString);
									logger.log(Level.DEBUG, "Response send to client");
								} else {
									logger.log(Level.WARN, "Request not recognized");
								}
								break;
							}
							break;
						}
						response = reader.readLine();
						if (response.equals("CLOSE")) {
							sock.close();
							logger.log(Level.DEBUG, "Socket Closed by Server");
						}
					} else {
						response = "ERROR";
						writer.println(response);
						logger.log(Level.WARN, "Resquest not recognized");

						response = reader.readLine();
						if (response.equals("CLOSE")) {
							sock.close();
							logger.log(Level.DEBUG, "Socket Closed by Server");
						}
					}
				} else {
					response = "ERROR";
					writer.println(response);

					response = reader.readLine();
					if (response.equals("CLOSE")) {
						sock.close();
						logger.log(Level.DEBUG, "Socket Closed by Server");
					}
				}
			} catch (IOException | NullPointerException e) {
			}
		}
		if (sock.isClosed())
			logger.log(Level.DEBUG, "Connection Closed");
	}

	/**
	 * return the result of the execution of request
	 */
	public boolean isResult() {
		return result;
	}

	/**
	 * 
	 * @param result set the result
	 */
	public void setResult(boolean result) {
		this.result = result;
	}
}