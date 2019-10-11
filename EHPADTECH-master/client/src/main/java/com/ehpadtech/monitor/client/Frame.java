package com.ehpadtech.monitor.client;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.ehpadtech.monitor.commons.entity.Alert;
import com.ehpadtech.monitor.commons.entity.Employee;
import com.ehpadtech.monitor.launcher.ClientSocket;
import com.fasterxml.jackson.databind.ObjectMapper;


public class Frame extends JFrame {

	/**
	 * Different parameters used
	 */
	private static final long serialVersionUID = 1L;
	private static JTabbedPane tab;
	private TabEmployes tabEmployes;
	private TabCommonArea tabCommonArea;
	private TabSensor tabSensor;
	private TabResident tabResident;
	private TabHistorical tabHistorical;
	private TabProfile tabProfile;
	private TabMapSensor tabMapSensor;
	private int idEmployee;
	private String requestType;
	private String table;
	private String jsonString;
	private Employee employee;
	private ObjectMapper objectMapper;
	private Thread threadFrame;
	private Thread threadAlert;
	private List<Alert> listAlert;
	private static final Logger logger = LogManager.getLogger(Frame.class);
	private ResourceBundle rs = ResourceBundle.getBundle("parameters");

	/**
	 * Constructor
	 * 
	 * @param idEmployee
	 */
	public Frame(int idEmployee) {
		this.idEmployee = idEmployee;
		this.listAlert = new ArrayList<Alert>();

		///////////////////////// Thread/////////////////////////////////////////////////
		setThreadFrame(new Thread(new Runnable() {
			/**
			 * Loop and update every 30 seconds the list of employees If the employee is'nt
			 * in Data base they will be disconnected
			 */
			@Override
			public void run() {
				while (true) {
					verificationUser(idEmployee);
					if (employee.getLastnameEmployee().equals("")) {
						logger.log(Level.INFO, "Disconnection, user don't find");
						JOptionPane.showMessageDialog(null, "You will be disconnect", "Error",
								JOptionPane.ERROR_MESSAGE);
						System.exit(ABORT);
					}
					try {
						Thread.sleep(Integer.parseInt(rs.getString("time_threadSleep")));
					} catch (InterruptedException e) {
						logger.log(Level.WARN,
								"Impossible to sleep the thread Frame " + e.getClass().getCanonicalName());
					}
				}
			}
		}));

		
		/**
		 * Creation of different tabs
		 */
		tabEmployes = new TabEmployes(Color.PINK, this.idEmployee, "Tab Employees");
		tabCommonArea = new TabCommonArea(Color.PINK, this.idEmployee, -1, "Tab Common Areas");
		tabSensor = new TabSensor(Color.PINK, this.idEmployee, "Tab Sensors", 0);
		tabResident = new TabResident(Color.PINK, this.idEmployee, "Tab Residents");
		tabHistorical = new TabHistorical(Color.PINK, this.idEmployee, "Tab Historical");
		tabProfile = new TabProfile(Color.PINK, this.idEmployee, "Tab Profile");
		tabMapSensor = new TabMapSensor(Color.PINK, this.idEmployee, "Tab Map", 0);

		/**
		 * Add of the title of tabs
		 */
		tab = new JTabbedPane();
		String tabOfTab[] = { "Employees", "Common Areas", "Sensors", "Residents", "Historical", "Profile", "Map" };

		/**
		 * Add of tabs on the window
		 */
		tab.add("Tab " + tabOfTab[0], tabEmployes);
		tab.add("Tab " + tabOfTab[1], tabCommonArea);
		tab.add("Tab " + tabOfTab[2], tabSensor);
		//tab.add("Tab " + tabOfTab[3], tabResident);
		tab.add("Tab " + tabOfTab[4], tabHistorical);
		//tab.add("Tab " + tabOfTab[5], tabProfile);
		//tab.add("Tab " + tabOfTab[6], tabMapSensor);

		///////////////////////// ALERT/////////////////////////////////////////////////
		setThreadAlert(new Thread(new Runnable() {
			/**
			 * Loop and update every second, if the listAlert is'nt empty they actualize the
			 * listSensor with the different states contained in the listAlert
			 */
			@Override
			public void run() {
				while (true) {
					getAlert();
					if (!listAlert.isEmpty())
						// TODO RK Make the method actualizationListSensor
						// tabSensor.actualizationListSensor(listAlert);
						logger.log(Level.DEBUG, "Actualization of listAlert succeed");
					try {
						Thread.sleep(Integer.parseInt(rs.getString("time_threadAlert")));
					} catch (InterruptedException e) {
						logger.log(Level.WARN,
								"Impossible to sleep the thread Alert " + e.getClass().getCanonicalName());
					}
				}
			}
		}));

		/**
		 * Launch the threads
		 */
		// TODO Modify the thread to decomment
		// threadAlert.start();
		threadFrame.start();
		//tabCommonArea.threadLauncher();
		tabEmployes.threadLauncher();
		tabHistorical.threadLauncher();
		//tabProfile.threadLauncher();
		//tabResident.threadLauncher();
		tabSensor.threadLauncher();

		///////////////////////// FRAME/////////////////////////////////////////////////
		/**
		 * Different parameters of the window
		 */
		this.setTitle("Ehpadtech");
		this.setSize((int) getToolkit().getScreenSize().getWidth(), (int) getToolkit().getScreenSize().getHeight());
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getContentPane().add(tab);
	}

	/**
	 * Verify if the employee exist in the data base Send the id inserted by user,
	 * and wait the response with all the informations about this user
	 * 
	 * @param id
	 */
	public void verificationUser(int id) {
		requestType = "READ";
		employee = new Employee();
		table = "Employee";
		employee.setIdEmployee(id);
		objectMapper = new ObjectMapper();
		try {
			jsonString = objectMapper.writeValueAsString(employee);
			new ClientSocket(requestType, jsonString, table);
			jsonString = ClientSocket.getJson();
			employee = objectMapper.readValue(jsonString, Employee.class);
			logger.log(Level.DEBUG, "Find Employee data succed");
		} catch (Exception e1) {
			logger.log(Level.WARN, "Impossible to parse in JSON Employee datas " + e1.getClass().getCanonicalName());
		}
	}

	/**
	 * Get all Alert stocked on server and add on listAlert Send the request to get
	 * all alerts and receive an list of alert
	 */
	public void getAlert() {
		requestType = "GET ALERT";
		table = "Alert";
		objectMapper = new ObjectMapper();
		try {
			jsonString = "GET ALERT";
			new ClientSocket(requestType, jsonString, table);
			jsonString = ClientSocket.getJson();
			Alert[] alerts = objectMapper.readValue(jsonString, Alert[].class);
			listAlert = Arrays.asList(alerts);
			logger.log(Level.DEBUG, "Find Alerts datas succed");
		} catch (Exception e1) {
			logger.log(Level.WARN, "Impossible to parse in JSON Alerts datas " + e1.getClass().getCanonicalName());
		}
	}

	/**
	 * Method to switch to an other tab
	 * 
	 * @param index
	 */
	public static void goToTab(int index) {
		tab.setSelectedIndex(index);
	}

	/**
	 * Method to get the actual tab of frame
	 * 
	 * @return tab
	 */
	public static JTabbedPane getTab() {
		return tab;
	}

	/**
	 * Method to add a new tab on frame
	 * 
	 * @param tab
	 */
	public void setTab(JTabbedPane tab) {
		Frame.tab = tab;
	}

	/**
	 * @return the threadFrame
	 */
	public Thread getThreadFrame() {
		return threadFrame;
	}

	/**
	 * @param threadFrame the threadFrame to set
	 */
	public void setThreadFrame(Thread threadFrame) {
		this.threadFrame = threadFrame;
	}

	/**
	 * @return the threadAlert
	 */
	public Thread getThreadAlert() {
		return threadAlert;
	}

	/**
	 * @param threadAlert the threadAlert to set
	 */
	public void setThreadAlert(Thread threadAlert) {
		this.threadAlert = threadAlert;
	}
}