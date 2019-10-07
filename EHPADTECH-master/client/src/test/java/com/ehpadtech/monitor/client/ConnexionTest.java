package com.ehpadtech.monitor.client;

import static org.junit.jupiter.api.Assertions.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.jupiter.api.Test;


class ConnexionTest {

	private Connexion popupConnection;
	private static Logger logger = Logger.getLogger("logger");

	/**
	 * Test the creation of the frame Connection
	 */
	@Test
	void connectionTest() {
		try {
			popupConnection = new Connexion();
			popupConnection.setVisible(true);
			assertTrue(popupConnection.isShowing());
		} catch (Exception e) {
			logger.log(Level.INFO, "Impossible to display the frame 'Connexion' " + e.getClass().getCanonicalName());
		}
	}

}
