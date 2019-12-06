package com.ehpadtech.monitor.connection.launcher;


import java.util.ResourceBundle;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.ehpadtech.monitor.connection.socket.Server;


public class MainServerGUI  {

	/**
	 * Initialization of different parameters
	 */
	private static final long serialVersionUID = 1L;
	private String host;
	private int port;
	private static int nbServer = 0;

	private Server ts;
	
	private static final Logger logger = LogManager.getLogger(MainServerGUI.class);

	/**
	 * Main to open the Server
	 * 
	 */
	public MainServerGUI() {
		ResourceBundle rs = ResourceBundle.getBundle("config");
		this.host = rs.getString("server.host");
		this.port = Integer.parseInt(rs.getString("server.port"));
		ts = new Server(host, port);	
		
				ts.load();
				if (nbServer == 0) {

					ts.open();
					nbServer++;
					logger.log(Level.INFO, "Server Initialized");
					System.out.println("server start");
					ts.treatment();
				} else {
					logger.log(Level.INFO, "Server not Initialized");
				}
			}
		

	/**
	 * Main to open the Server
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		MainServerGUI frame = new MainServerGUI();
		
	}
}