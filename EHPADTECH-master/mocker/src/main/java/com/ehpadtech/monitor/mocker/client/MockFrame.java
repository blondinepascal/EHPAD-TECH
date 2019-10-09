package com.ehpadtech.monitor.mocker.client;

import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MockFrame extends JFrame {

	/**
	 * Different parameters used
	 */
	private static final long serialVersionUID = 1L;
	private static JTabbedPane tab;
	private TabMockMessage tabMockMessage;
	private TabMockOther tabMockEmployee;
	private TabMockSensor tabMockSensor;
	private static final Logger logger = LogManager.getLogger(MockFrame.class);

	/**
	 * Constructor
	 */
	public MockFrame() {

		
		/**
		 * Creation of different tabs
		 */
		tabMockMessage = new TabMockMessage(Color.PINK, "Tab Message");
		tabMockSensor = new TabMockSensor(Color.PINK, "Tab Sensor");
		tabMockEmployee = new TabMockOther(Color.PINK, "Tab Other");

		/**
		 * Add of the title of tabs
		 */
		tab = new JTabbedPane();
		String tabOfTab[] = { "Message", "Sensor", "Other" };

		/**
		 * Add of tabs on the window
		 */
		tab.add("Tab " + tabOfTab[0], tabMockMessage);
		tab.add("Tab " + tabOfTab[1], tabMockSensor);
		tab.add("Tab " + tabOfTab[2], tabMockEmployee);

		///////////////////////// FRAME/////////////////////////////////////////////////
		/**
		 * Different parameters of the window
		 */
		this.setTitle("Monitor Mock");
		this.setSize(850, 600);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getContentPane().add(tab);
	}

	/**
	 * 
	 * @return the width of the frame
	 */
	public int getWidthMockFrame() {
		return this.getWidth();
	}
}