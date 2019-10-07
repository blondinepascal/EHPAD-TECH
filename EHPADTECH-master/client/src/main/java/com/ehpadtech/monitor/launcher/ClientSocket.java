package com.ehpadtech.monitor.launcher;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ResourceBundle;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class ClientSocket {

	/**
	 * Different parameters used
	 */
	private int port;
	private String requestType;
	private String response;
	private String table;
	private String host;
	private static String jsonString;
	private Socket sock = null;
	private PrintWriter writer = null;
	private BufferedReader reader = null;
	private static final Logger logger = LogManager.getLogger(ClientSocket.class);
	private ResourceBundle rs = ResourceBundle.getBundle("config");

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
			logger.log(Level.DEBUG, "Command OPEN connection send to server");
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
				case "GET ALERT":
					response = "{ \"request\" : \"GET ALERT\", \"table\" : \"" + this.table + "\" }";
					break;
				default:
					response = "";
				}
				writer.println(response);
				logger.log(Level.DEBUG, "Request Type Send to server");
				response = reader.readLine();

				/**
				 * Send to server the jsonString coded in JSON
				 */
				if (!response.equals("ERROR")) {
					response = jsonString;
					writer.println(response);
					logger.log(Level.DEBUG, "Request Send to server");
					response = reader.readLine();
					/**
					 * Receive the data in JSON string after the execution by server
					 */
					if (!response.equals("ERROR")) {
						ClientSocket.setJsonString(response);
						logger.log(Level.DEBUG, "Datas received on client");
						response = "CLOSE";
						writer.println(response);
						logger.log(Level.DEBUG, "Command CLOSE connection send to server");
						writer.close();
						logger.log(Level.DEBUG, "Connection Closed by client");
					} else {
						response = "CLOSE";
						writer.println(response);
						logger.log(Level.DEBUG, "Command CLOSE connection send to server");
						writer.close();
						logger.log(Level.DEBUG, "Connection Closed by client");
					}
				} else {
					/**
					 * If the request are not recognized or can't be execute then we close the
					 * socket
					 */
					response = "CLOSE";
					writer.println(response);
					logger.log(Level.DEBUG, "Command CLOSE connection send to server");
					writer.close();
					logger.log(Level.DEBUG, "Connection Closed by client");
				}
			} else {
				logger.log(Level.DEBUG, "ERROR, Impossible to Receive datas");
				response = "CLOSE";
				writer.println(response);
				logger.log(Level.DEBUG, "Command CLOSE connection send to server");
				writer.close();
				logger.log(Level.DEBUG, "Connection Closed by client");
			}
		} catch (UnknownHostException e) {
			logger.log(Level.WARN, "IP Host dont find " + e.getClass().getCanonicalName());
		} catch (IOException e) {
			logger.log(Level.WARN, "Impossible create the socket " + e.getClass().getCanonicalName());
		} catch (NullPointerException e) {
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
}