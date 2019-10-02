package com.ehpadtech.monitor.connection.connectionpool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class JDBCConnectionPool {

	/**
	 * Definition of different fields
	 */
	private String driver;
	private String url;
	private String user;
	private String password;
	private int maxConnection;
	private int numberConnection;
	private boolean busy;
	private ResourceBundle rsConfig;
	private static final Logger logger = LogManager.getLogger(JDBCConnectionPool.class);
	private List<Connection> availableConnections;
	private List<Connection> availableConnectionsArrayList;

	/**
	 * Get informations from confirugation.propreties Initialization of fields with
	 * the content of this same file configuration.properties Create two Lists
	 * synchronized Initialization of the available list connection
	 * 
	 * @param busy
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws Exception
	 */
	public JDBCConnectionPool(boolean busy) throws SQLException {
		int initialConnections = 0;
		rsConfig = ResourceBundle.getBundle("config");
		numberConnection = 0;
		this.busy = busy;
		this.driver = rsConfig.getString("db.driver");
		this.url = rsConfig.getString("db.url");
		this.user = rsConfig.getString("db.username");
		this.password = rsConfig.getString("db.password");
		this.maxConnection = Integer.parseInt(rsConfig.getString("db.maxConnections"));
		initialConnections = Integer.parseInt(rsConfig.getString("db.initialConnections"));

		if (initialConnections > maxConnection) {
			initialConnections = maxConnection;
		}
		availableConnectionsArrayList = new ArrayList<Connection>(initialConnections);
		availableConnections = Collections.synchronizedList(availableConnectionsArrayList);
		for (int i = 0; i < initialConnections; i++) {
			availableConnections.add(newConnection());
		}
	}

	/**
	 * Return a connection if is possible, and if the limit of connection are
	 * reached they look at the maxConnection and add one if the number of
	 * connection initialize is inferior Else they catch an exception
	 * 
	 * @return return the connection
	 * @throws SQLException
	 */
	public synchronized Connection getConnection() throws SQLException {
		if (!availableConnections.isEmpty()) {
			Connection existingConnection = (Connection) availableConnections.get(availableConnections.size() - 1);
			int lastIndex = availableConnections.size() - 1;
			availableConnections.remove(lastIndex);
			if (existingConnection.isClosed()) {
				numberConnection--;
				notifyAll();
				return (getConnection());
			} else {
				numberConnection++;
				return (existingConnection);
			}
		} else {
			if ((numberConnection < maxConnection)) {
				Connection connection = newConnection();
				synchronized (this) {
					availableConnections.add(connection);
					notifyAll();
				}
			} else if (!busy) {
				throw new SQLException("Connection limit reached");
			}
			return (getConnection());
		}
	}

	/**
	 * Creation of a new connection available to be added on available List of
	 * connection
	 * 
	 * @return Connection
	 * @throws ClassNotFoundException
	 */
	private Connection newConnection() throws SQLException {
		Connection connection = null;
		try {
			Class.forName(driver);
			connection = DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			throw new SQLException("ConnectionPool: SQLException encountered: " + e.getMessage());
		} catch (ClassNotFoundException e) {
			logger.log(Level.ERROR, "Driver JDBC inconnu", e.getClass().getCanonicalName());
		}
		return (connection);
	}

	/**
	 * free a connection and add it on the available connection list
	 * 
	 * @param connection
	 */
	public synchronized void free(Connection connection) {
		availableConnections.add(connection);
		numberConnection--;
		notifyAll();
	}

	/**
	 * Return the total number of available connection on availableConnection List
	 * 
	 * @return
	 */
	public synchronized int getTotalConnections() {
		return (availableConnections.size());
	}

	/**
	 * CLose all connection of the available connection list
	 */
	public synchronized void closeAllConnections() {
		closeConnections(availableConnections);
		availableConnections = new ArrayList<Connection>();
		numberConnection = 0;
	}

	/**
	 * Close all connection of the list
	 * 
	 * @param List of connections
	 */
	private void closeConnections(List<Connection> connections) {
		try {
			for (int i = 0; i < connections.size(); i++) {
				Connection connection = (Connection) connections.get(i);
				if (!connection.isClosed()) {
					connection.close();
				}
			}
		} catch (SQLException e) {
			logger.log(Level.ERROR, e.getClass().getCanonicalName());
		}
	}

	/**
	 * Getter to the MaxConnection value
	 * 
	 * @return
	 */
	public int getMaxConnection() {
		return maxConnection;
	}

	/**
	 * Setter to the MaxConnection value
	 * 
	 * @param maxConnection
	 */
	public void setMaxConnection(int maxConnection) {
		this.maxConnection = maxConnection;
	}
}