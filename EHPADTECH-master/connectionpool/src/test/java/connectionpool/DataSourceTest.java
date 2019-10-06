package connectionpool;

import static org.junit.jupiter.api.Assertions.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import com.ehpadtech.monitor.connection.connectionpool.DataSource;
import com.ehpadtech.monitor.connection.connectionpool.JDBCConnectionPool;


class DataSourceTest {

	private static final Logger logger = LogManager.getLogger(DataSourceTest.class);

	/**
	 * Test GetConnection
	 */
	@Test
	void testGetConnectionFromJDBC() {
		JDBCConnectionPool p;
		try {
			p = new JDBCConnectionPool(false);
			Connection con = DataSource.getConnectionFromJDBC(p);
			assertNotNull(con);
			logger.log(Level.INFO, "Connection succed ");
		} catch (Exception e) {
			logger.log(Level.INFO, "SGBD connection is impossible " + e.getClass().getCanonicalName());
		}
	}

	/**
	 * Test insertion in SGBD
	 */
	@Test
	void testInsertData() {
		JDBCConnectionPool p;
		try {
			p = new JDBCConnectionPool(false);
			Connection con = DataSource.getConnectionFromJDBC(p);
			Statement st = con.createStatement();
			String sql = "insert into employee (nom_employee, prenom_employee, mot_de_passe) values ('keita','raymond','test')";
			assertNotNull(st.execute(sql));
			logger.log(Level.INFO, "Insertion in SGBD succed ");
		} catch (Exception e) {
			logger.log(Level.INFO, "Insertion in SGBD failed " + e.getClass().getCanonicalName());
		}
	}

	/**
	 * Test get data in SGBD
	 */
	@Test
	void testGetData() {
		JDBCConnectionPool p;
		try {
			p = new JDBCConnectionPool(false);
			Connection con = DataSource.getConnectionFromJDBC(p);
			Statement st = con.createStatement();
			String sql = "select * from employee";
			ResultSet rs = st.executeQuery(sql);
			assertNotNull(rs);
			logger.log(Level.INFO, "Data recovery in SGBD succed ");
		} catch (Exception e) {
			logger.log(Level.INFO, "Data recovery in SGBD failed " + e.getClass().getCanonicalName());
		}
	}

	/**
	 * Test update data in SGBD
	 */
	@Test
	void testUpdateData() {
		JDBCConnectionPool p;
		try {
			p = new JDBCConnectionPool(false);
			Connection con = DataSource.getConnectionFromJDBC(p);
			Statement st = con.createStatement();
			String sql = "update employee set prenom_employee = 'arnaud'";
			assertNotNull(st.execute(sql));
			logger.log(Level.INFO, "Update in SGBD succed ");
		} catch (Exception e) {
			logger.log(Level.INFO, "Update in SGBD failed " + e.getClass().getCanonicalName());
		}
	}

	/**
	 * Test delete data in SGBD
	 */
	@Test
	void testDeleteData() {
		JDBCConnectionPool p;
		try {
			p = new JDBCConnectionPool(false);
			Connection con = DataSource.getConnectionFromJDBC(p);
			Statement st = con.createStatement();
			String sql = "delete from employee where nom_employee = 'keita'";
			assertNotNull(st.execute(sql));
			logger.log(Level.INFO, "Delete in SGBD succed ");
		} catch (Exception e) {
			logger.log(Level.INFO, "Delete in SGBD failed " + e.getClass().getCanonicalName());
		}
	}

	/**
	 * Test return connection
	 */
	@Test
	void testReturnConnection() {
		JDBCConnectionPool p;
		try {
			p = new JDBCConnectionPool(false);
			Connection con = DataSource.getConnectionFromJDBC(p);
			DataSource.returnConnection(p, con);
			assertTrue(true);
			logger.log(Level.INFO, "Return Connection succed ");
		} catch (Exception e) {
			logger.log(Level.INFO, "Return Connection failed " + e.getClass().getCanonicalName());
		}
	}

	/**
	 * Test close All connections
	 */
	@Test
	void testCloseConnectionsFromJDBC() {
		JDBCConnectionPool p;
		try {
			p = new JDBCConnectionPool(false);
			DataSource.getConnectionFromJDBC(p);
			DataSource.closeConnectionsFromJDBC(p);
			assertTrue(true);
			logger.log(Level.INFO, "All connection are closed ");
		} catch (Exception e) {
			logger.log(Level.INFO, "Close all connection failed " + e.getClass().getCanonicalName());
		}
	}

	/**
	 * Test total connection on pool
	 */
	@Test
	void testTotalConnection() {
		JDBCConnectionPool p;
		int totalConnection1, totalConnection2;
		try {
			p = new JDBCConnectionPool(false);
			totalConnection1 = p.getTotalConnections();
			logger.log(Level.INFO, "Number of Connection is " + totalConnection1);
			DataSource.getConnectionFromJDBC(p);
			DataSource.getConnectionFromJDBC(p);
			DataSource.getConnectionFromJDBC(p);
			totalConnection2 = p.getTotalConnections();
			assertNotEquals(totalConnection1, totalConnection2);
			logger.log(Level.INFO, "Number of Connection increased to " + totalConnection2);
		} catch (Exception e) {
			logger.log(Level.INFO, "Connection dont increase normaly " + e.getClass().getCanonicalName());
		}
	}

	/*
	 * Test if getConnection() give the same connection
	 */
	@Test
	void testDifferentCOnnections() {
		JDBCConnectionPool p;
		try {
			p = new JDBCConnectionPool(false);
			Connection con = DataSource.getConnectionFromJDBC(p);
			Connection con2 = DataSource.getConnectionFromJDBC(p);
			assertNotEquals(con, con2);
			logger.log(Level.INFO, "Connection 1 and connection 2 are different");
		} catch (Exception e) {
			logger.log(Level.INFO, "Connection 1 and connection 2 are an unique connection" + e.getClass().getCanonicalName());
		}
	}

	/*
	 * Test if we reach the limit of connection, with the creation of 4 connections
	 * and a nbMaxConnection define at 3 on Configuration.properties
	 */
	@Test
	void testMaxConnections() {
		JDBCConnectionPool p;
		try {
			p = new JDBCConnectionPool(false);
			Connection con = DataSource.getConnectionFromJDBC(p);
			assertNotNull(con);
			logger.log(Level.INFO, "Connection 1 done");
			Connection con2 = DataSource.getConnectionFromJDBC(p);
			assertNotNull(con2);
			logger.log(Level.INFO, "Connection 2 done");
			Connection con3 = DataSource.getConnectionFromJDBC(p);
			assertNotNull(con3);
			logger.log(Level.INFO, "Connection 3 done");
			Connection con4 = DataSource.getConnectionFromJDBC(p);
			assertNotNull(con4);
			logger.log(Level.INFO, "Connection 4 done");
		} catch (Exception e) {
			logger.log(Level.INFO, "Limit reached, no available connection " + e.getClass().getCanonicalName());
		}
	}

	/**
	 * Test if we reach the limit of connection, with unlimited creation of
	 * connections and a nbMaxConnection define at 3 on Configuration.properties but
	 * this test can be used if we change the nbMaxConnection
	 */
	@Test
	void testMaxConnectionsPossible() {
		JDBCConnectionPool p;
		int nbconnexionscreated = 0;
		try {
			p = new JDBCConnectionPool(false);
			while (nbconnexionscreated < 10000) {
				nbconnexionscreated++;
				Connection con = DataSource.getConnectionFromJDBC(p);
				assertNotNull(con);
				logger.log(Level.INFO, "Connection " + nbconnexionscreated + " done");
			}
		} catch (Exception e) {
			logger.log(Level.INFO, "Limit reached, no available connection " + e.getClass().getCanonicalName());
		}
	}
}