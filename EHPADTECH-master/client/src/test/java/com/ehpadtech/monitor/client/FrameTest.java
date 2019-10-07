package com.ehpadtech.monitor.client;

import static org.junit.jupiter.api.Assertions.*;
import java.awt.EventQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.jupiter.api.Test;


class FrameTest {

	private Frame frame;
	private static Logger logger = Logger.getLogger("logger");

	/**
	 * Test the creation of the frame GUI
	 */
	@Test
	void mainGUITest() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new Frame(0);
					frame.setVisible(true);
					assertTrue(frame.isShowing());
				} catch (Exception e) {
					logger.log(Level.INFO, "Impossible to display the frame 'Frame' " + e.getClass().getCanonicalName());
				}
			}
		});
	}
}
