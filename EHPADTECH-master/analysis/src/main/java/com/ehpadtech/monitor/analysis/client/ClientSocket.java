package com.ehpadtech.monitor.analysis.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Properties;
import java.util.ResourceBundle;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.ehpadtech.monitor.commons.entity.Employee;


public class ClientSocket {

	/**
	 * Different parameters used
	 */
	private Socket sock = null;
	private PrintWriter writer = null;
	private BufferedReader reader = null;
	private int port;
	private String requestType;
	private String response;
	private String table;
	private String host;
	private static String jsonString;
	private static Employee emp = new Employee();
	private final Properties prop = new Properties();
	private static final Logger logger = LogManager.getLogger(ClientSocket.class);

	/**
	 * Constructor
	 */
	public ClientSocket() {
	}

	/**
	 * Constructor
	 * 
	 * @param requestType
	 * @param jsonString
	 * @param table
	 */
	public ClientSocket(String requestType, String jsonString, String table) {
		this.requestType = requestType;
		this.table = table;

		ResourceBundle rs = ResourceBundle.getBundle("config");
		this.host = rs.getString("server.host");
		this.port = Integer.parseInt(rs.getString("server.port"));

		ClientSocket.setJsonString(jsonString);
		/**
		 * Create a new socket and send to host
		 */
		try {
			sock = new Socket(host, port);
			writer = new PrintWriter(sock.getOutputStream(), true);
			reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));

			writer.println("OPEN");
			logger.log(Level.INFO, "Command OPEN connection send to server");

			response = reader.readLine();

			/**
			 * Send to server the type of request and the table where we do the request
			 * coded in JSON
			 */
			if (response.equals("OK FOR EXCHANGE")) {
				switch (this.requestType) {
				case "CONNECTION":
					response = "{ \"request\" : \"CONNECTION\", \"table\" : \"" + this.table + "\" }";
					break;
				case "CREATE":
					response = "{ \"request\" : \"CREATE\", \"table\" : \"" + this.table + "\" }";
					break;
				case "UPDATE":
					response = "{ \"request\" : \"UPDATE\", \"table\" : \"" + this.table + "\" }";
					break;
				case "DELETE":
					response = "{ \"request\" : \"DELETE\", \"table\" : \"" + this.table + "\" }";
					break;
				case "READ":
					response = "{ \"request\" : \"READ\", \"table\" : \"" + this.table + "\" }";
					break;
				case "READ ALL":
					response = "{ \"request\" : \"READ ALL\", \"table\" : \"" + this.table + "\" }";
					break;
				case "FIND ALL":
					response = "{ \"request\" : \"FIND ALL\", \"table\" : \"" + this.table + "\" }";
					break;
				case "COUNT":
					response = "{ \"request\" : \"COUNT\", \"table\" : \"" + this.table + "\" }";
					break;
				case "COUNT OTHER":
					response = "{ \"request\" : \"COUNT OTHER\", \"table\" : \"" + this.table + "\" }";
					break;
				case "COUNTING":
					response = "{ \"request\" : \"COUNTING\", \"table\" : \"" + this.table + "\" }";
					break;
				case "COUNTING OTHER":
					response = "{ \"request\" : \"COUNTING OTHER\", \"table\" : \"" + this.table + "\" }";
					break;
					
				default:
					response = "";
				}
				writer.println(response);
				logger.log(Level.INFO, "Request Type Send to server");

				response = reader.readLine();

				/**
				 * Send to server the jsonString coded in JSON
				 */
				if (!response.equals("ERROR")) {
					response = jsonString;
					writer.println(response);
					logger.log(Level.INFO, "Request Send to server");

					response = reader.readLine();
					/**
					 * Receive the data in JSON string after the execution by server
					 */
					if (!response.equals("ERROR")) {
						ClientSocket.setJsonString(response);
						logger.log(Level.INFO, "Datas received on client");
						response = "CLOSE";
						writer.println(response);
						logger.log(Level.INFO, "Command CLOSE connection send to server");
						writer.close();
						logger.log(Level.INFO, "Connection Closed by client");
					} else {
						response = "CLOSE";
						writer.println(response);
						logger.log(Level.INFO, "Command CLOSE connection send to server");
						writer.close();
						logger.log(Level.INFO, "Connection Closed by client");
					}
				} else {
					/**
					 * If the request are not recognized or can't be execute then we close the
					 * socket
					 */
					response = "CLOSE";
					writer.println(response);
					logger.log(Level.INFO, "Command CLOSE connection send to server");
					writer.close();
					logger.log(Level.INFO, "Connection Closed by client");
				}
			} else {
				logger.log(Level.INFO, "ERROR, Impossible to Receive datas");
				response = "CLOSE";
				writer.println(response);
				logger.log(Level.INFO, "Command CLOSE connection send to server");
				writer.close();
				logger.log(Level.INFO, "Connection Closed by client");
			}
		} catch (UnknownHostException e) {
			logger.log(Level.INFO, "IP Host dont find " + e.getClass().getCanonicalName());
		} catch (IOException e) {
			logger.log(Level.INFO, "Impossible create the socket " + e.getClass().getCanonicalName());
		}
	}

	/**
	 * Have the JSON response from server
	 * 
	 * @return JsonString
	 */
	public static String getJson() {
		return jsonString;
	}

	/**
	 * Change the content of jsonString
	 * 
	 * @param jsonString
	 */
	public static void setJsonString(String jsonString) {
		ClientSocket.jsonString = jsonString;
	}

	public Properties getProp() {
		return prop;
	}

	public static Employee getEmp() {
		return emp;
	}

	public static void setEmp(Employee emp) {
		ClientSocket.emp = emp;
	}
}
