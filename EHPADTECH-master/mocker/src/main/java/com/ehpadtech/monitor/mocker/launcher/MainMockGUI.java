package com.ehpadtech.monitor.mocker.launcher;

import java.awt.EventQueue;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ehpadtech.monitor.mocker.client.MockFrame;

public class MainMockGUI {
	
	/**
	 * Different parameters used
	 */
	private static MockFrame mockFrame;
	private static Logger logger = LogManager.getLogger(MainMockGUI.class);

	/**
	 * Method Main
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					mockFrame = new MockFrame();
					mockFrame.setVisible(true);
					logger.log(Level.INFO, "Application Mock runned");
				} catch (Exception e) {
					logger.log(Level.WARN,
							"Impossible to create the window 'mockFrame' " + e.getClass().getCanonicalName());
					System.exit(0);
				}
			}
		});
	}
}