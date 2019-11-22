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
			logger.log(Level.INFO, "Connection successful ");
		} catch (Exception e) {
			logger.log(Level.INFO, "SGBD connection is impossible " + e.getClass().getCanonicalName());
		}
	}

	/**
	 * Test insertion in SGBD
	 */
	/*@Test
	void testInsertData() {
		JDBCConnectionPool p;
		try {
			p = new JDBCConnectionPool(false);
			Connection con = DataSource.getConnectionFromJDBC(p);
			Statement st = con.createStatement();
			String sql = "insert into employee (id_employee,nom_employee,prenom_employee, mot_de_passe,poste) values (33,'BATIST','SYLVAIN','sylvain','DEVELOPPEUR')";
			assertNotNull(st.execute(sql));
			logger.log(Level.INFO, "Insertion in SGBD successful ");
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
			logger.log(Level.INFO, "Data recovery in SGBD successfully ");
		} catch (Exception e) {
			logger.log(Level.INFO, "Data recovery in SGBD failed " + e.getClass().getCanonicalName());
		}
	}

	/**
	 * Test update data in SGBD
	 */
	/*@Test
	void testUpdateData() {
		JDBCConnectionPool p;
		try {
			p = new JDBCConnectionPool(false);
			Connection con = DataSource.getConnectionFromJDBC(p);
			Statement st = con.createStatement();
			String sql = "update employee set poste = 'DIRECTEUR' where nom_employee ='PIPARD'";
			assertNotNull(st.execute(sql));
			logger.log(Level.INFO, "Update in SGBD succed ");
		} catch (Exception e) {
			logger.log(Level.INFO, "Update in SGBD failed " + e.getClass().getCanonicalName());
		}
	}

	/**
	 * Test delete data in SGBD
	 */
	/*@Test
	void testDeleteData() {
		JDBCConnectionPool p;
		try {
			p = new JDBCConnectionPool(false);
			Connection con = DataSource.getConnectionFromJDBC(p);
			Statement st = con.createStatement();
			String sql = "delete from employee where nom_employee = 'PAULON'";
			assertNotNull(st.execute(sql));
			logger.log(Level.INFO, "Delete in SGBD successful ");
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
			logger.log(Level.INFO, "Return Connection successful ");
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
	 * Test if we reach the limit of connection, with unlimited creation of
	 * connections and a nbMaxConnection define on Configuration.properties but
	 * this test can be used if we change the nbMaxConnection
	 */
	@Test
	void testMaxConnectionsPossible() {
		JDBCConnectionPool p;
		int nbconnexionscreated = 0;
		try {
			p = new JDBCConnectionPool(false);
			while (nbconnexionscreated < 11) {
				nbconnexionscreated++;
				Connection con = DataSource.getConnectionFromJDBC(p);
				assertNotNull(con);
				logger.log(Level.INFO, "Connection " + nbconnexionscreated + " done");
			}
		} catch (Exception e) {
			logger.log(Level.INFO, "Limit reached, the pool is saturated " + e.getClass().getCanonicalName());
		}
	}
}