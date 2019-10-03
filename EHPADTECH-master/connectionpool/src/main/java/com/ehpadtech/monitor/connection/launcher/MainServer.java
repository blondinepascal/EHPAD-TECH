package com.ehpadtech.monitor.connection.launcher;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javax.swing.Timer;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.ehpadtech.monitor.connection.socket.Server;


public class MainServer {

	/**
	 * Initialization of different parameters
	 */
	private static String host;
	private static int port;
	private static final Logger logger = LogManager.getLogger(MainServer.class);
	private static int hour = 0;
	private static int minute = 0;
	private static int seconde = 0;
	private static ActionListener tache_timer;
	private static Server ts;
	static Timer timer1;

	/**
	 * Main to open the Server
	 * 
	 * @param args
	 * @throws SQLException
	 */
	public static void main(String[] args) throws SQLException {

		ResourceBundle rs = ResourceBundle.getBundle("config");
		host = rs.getString("server.host");
		port = Integer.parseInt(rs.getString("server.port"));
		ts = new Server(host, port);
		ts.open();

		
		/**
		 * Creation of Timer to know how many time the server was launch
		 */
		int delais = 1000;
		tache_timer = new ActionListener() {
			public void actionPerformed(ActionEvent e1) {
				seconde++;
				if (seconde == 60) {
					seconde = 0;
					minute++;
				}
				if (minute == 60) {
					minute = 0;
					setHour(getHour() + 1);
				}
			}
		};
		timer1 = new Timer(delais, tache_timer);
		timer1.start();
		ts.treatment();
	}

	/**
	 * @return the hour
	 */
	public static int getHour() {
		return hour;
	}

	/**
	 * @param heure the hour to set
	 */
	public static void setHour(int hour) {
		MainServer.hour = hour;
	}
}