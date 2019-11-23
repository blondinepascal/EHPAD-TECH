package com.ehpadtech.monitor.mocker.launcher;

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


public class MockSocket {

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
	private static final Logger logger = LogManager.getLogger(MockSocket.class);

	/**
	 * Constructor
	 */
	public MockSocket() {
	}

	/**
	 * Constructor
	 * 
	 * @param requestType
	 * @param jsonString
	 * @param table
	 */
	public MockSocket(String requestType, String jsonString, String table) {
		this.requestType = requestType;
		this.table = table;

		ResourceBundle rs = ResourceBundle.getBundle("config");
		this.host = rs.getString("server.host");
		this.port = Integer.parseInt(rs.getString("server.port"));

		MockSocket.setJsonString(jsonString);
		/**
		 * Create a new socket and send to host
		 */
		try {
			sock = new Socket(host, port);
			writer = new PrintWriter(sock.getOutputStream(), true);
			reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));

			writer.println("OPEN");
			response = reader.readLine();
			/**
			 * Send to server the type of request and the table where we do the request
			 * coded in JSON
			 */
			if (response.equals("OK FOR EXCHANGE")) {
				switch (this.requestType) {
				case "CREATE":
					response = "{ \"request\" : \"CREATE\", \"table\" : \"" + this.table + "\" }";
					break;
				case "ALERT":
					response = "{ \"request\" : \"ALERT\", \"table\" : \"" + this.table + "\" }";
					break;
				case "READ":
					response = "{ \"request\" : \"READ\", \"table\" : \"" + this.table + "\" }";
					break;
				case "READ ALL":
					response = "{ \"request\" : \"READ ALL\", \"table\" : \"" + this.table + "\" }";
					break;
				default:
					response = "";
				}
				writer.println(response);
				response = reader.readLine();
				/**
				 * Send to server the jsonString coded in JSON
				 */
				if (!response.equals("ERROR")) {
					response = jsonString;
					writer.println(response);
					logger.log(Level.DEBUG, "Message Send to server");

					response = reader.readLine();
					/**
					 * Receive the data in JSON string after the execution by server
					 */
					if (!response.equals("ERROR")) {
						MockSocket.setJsonString(response);
						response = "CLOSE";
						writer.println(response);
						writer.close();
					} else {
						response = "CLOSE";
						writer.println(response);
						writer.close();
					}
				} else {
					/**
					 * If the request are not recognized or can't be execute then we close the
					 * socket
					 */
					response = "CLOSE";
					writer.println(response);
					writer.close();
				}
			} else {
				logger.log(Level.WARN, "ERROR, Impossible to Receive datas");
				response = "CLOSE";
				writer.println(response);
				writer.close();
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
		MockSocket.jsonString = jsonString;
	}
}