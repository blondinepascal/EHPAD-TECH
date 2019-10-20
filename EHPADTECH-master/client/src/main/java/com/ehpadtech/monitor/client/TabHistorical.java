package com.ehpadtech.monitor.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.ehpadtech.monitor.commons.entity.Sensor;
import com.ehpadtech.monitor.commons.entity.SensorHistorical;
import com.ehpadtech.monitor.launcher.ClientSocket;
import com.ehpadtech.monitor.utils.SortByIdSensorHistorical;
import com.fasterxml.jackson.databind.ObjectMapper;


public class TabHistorical extends JPanel {

	/**
	 * Different parameters used
	 */
	private static final long serialVersionUID = 1L;
	private int idemployee;
	private int index;
	private String requestType;
	private String table;
	private String jsonString;
	private JPanel bar;
	private JLabel labelIdEmployee;
	private JButton checkSensor;
	private JButton checkCommonArea;
	private JButton delete;
	private Font police;
	private JScrollPane sc;
	private Sensor sensor;
	private SensorHistorical sensorHistorical;
	private TabSensor tabSensor;
	private TabCommonArea tabCommonArea;
	private JTabbedPane tab;
	private JList<String> list;
	private DefaultListModel<String> listM;
	private JButton disconnection;
	private Thread threadListSensorHistorical;
	private List<SensorHistorical> listSensorHistorical;
	private static final Logger logger = LogManager.getLogger(TabHistorical.class);
	private ResourceBundle rs = ResourceBundle.getBundle("parameters");

	/**
	 * Constructor
	 */
	public TabHistorical() {
	}

	/**
	 * Constructor
	 * 
	 * @param color
	 * @param idemployee
	 * @param title
	 */
	public TabHistorical(Color color, int idemployee, String title) {
		this.idemployee = idemployee;
		this.listSensorHistorical = new ArrayList<SensorHistorical>();

		///////////////////////// Thread/////////////////////////////////////////////////
		setThreadListSensorHistorical(new Thread(new Runnable() {
			/**
			 * Loop and update every 30 seconds the list of Sensors
			 */
			@Override
			public void run() {
				while (true) {
					updateListSensorHistorical();
					logger.log(Level.DEBUG, "Thread Historical do with success");
					try {
						Thread.sleep(Integer.parseInt(rs.getString("time_threadSleep")));
					} catch (InterruptedException e) {
						logger.log(Level.WARN,
								"Impossible to sleep the thread Historical " + e.getClass().getCanonicalName());
					}
				}
			}
		}));

		/**
		 * Definition of the structure of this tab
		 */
		bar = new JPanel();
		bar.setBackground(Color.BLUE);
		bar.setPreferredSize(new Dimension((int) getToolkit().getScreenSize().getWidth(), 80));
		bar.setLayout(new BorderLayout());
		bar.setBorder(BorderFactory.createMatteBorder(20, 100, 20, 100, bar.getBackground()));

		///////////////////////// BAR/////////////////////////////////////////////////
		/**
		 * Definition of label LOGIN on header bar
		 */
		labelIdEmployee = new JLabel("Login :   " + this.idemployee + "    ");
		police = new Font("Arial", Font.BOLD, 16);
		labelIdEmployee.setForeground(Color.WHITE);
		labelIdEmployee.setFont(police);
		bar.add(labelIdEmployee, BorderLayout.WEST);

		/**
		 * Definition of the button and the different action after pressed the button
		 */
		disconnection = new JButton("Disconnect");
		bar.add(disconnection, BorderLayout.EAST);
		disconnection.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				logger.log(Level.INFO, "Application closed, after disconnection");
				System.exit(ABORT);
			}
		});

		///////////////////////// TABLE/////////////////////////////////////////////////
		listM = new DefaultListModel<String>();
		list = new JList<String>(listM);
		updateListSensorHistorical();
		sensorHistorical = new SensorHistorical();
		/**
		 * Add a scrollBar on list
		 */
		sc = new JScrollPane(list);
		sc.setBounds((int) getToolkit().getScreenSize().getWidth() * 3 / 10,
				(int) getToolkit().getScreenSize().getHeight() * 2 / 10,
				(int) getToolkit().getScreenSize().getWidth() * 1 / 2,
				(int) getToolkit().getScreenSize().getHeight() * 1 / 2);
		this.add(sc);

		/**
		 * Get the id of the sensor selected by the user to be used after
		 */
		MouseListener mouseListener = new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				index = list.locationToIndex(e.getPoint());
				String substring = listM.getElementAt(index).toString();
				int position = substring.indexOf("ID Sensor : ");
				int position2 = substring.indexOf(" State : ");
				if (position > -1 && position2 > -1) {
					sensorHistorical.setIdSensor(Integer.parseInt(substring.substring(position + 12, position2)));
				}
				position = substring.indexOf(" ");
				if (position > -1) {
					sensorHistorical.setIdHistorical(Integer.parseInt(substring.substring(0, position)));
				}
				logger.log(Level.DEBUG, "SensorHistorical : " + sensorHistorical.getIdHistorical() + " selected");
			}
		};
		list.addMouseListener(mouseListener);

		///////////////////////// BUTTON/////////////////////////////////////////////////
		/**
		 * Definition of Button CheckSensor
		 */
		checkSensor = new JButton("Display Sensor");
		checkSensor.setBounds(((int) getToolkit().getScreenSize().getWidth() * 1 / 10),
				(int) getToolkit().getScreenSize().getHeight() * 5 / 20, 150, 40);
		this.add(checkSensor);
		checkSensor.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (index != -9999) {
					if (sensorHistorical.getIdSensor() == 0) {
						JOptionPane.showMessageDialog(null, "Please select an line", "Error",
								JOptionPane.INFORMATION_MESSAGE);
					} else {
						tab = new JTabbedPane();
						tab = Frame.getTab();
						try {
							if (tab.isEnabledAt(2)) {
								tab.remove(2);
								tabSensor = new TabSensor(color, idemployee, "Tab Sensors",
										sensorHistorical.getIdSensor());
								tabSensor.threadLauncher();
								tab.add(tabSensor, 2);
								tab.setTitleAt(2, "Tab Sensors");
								Frame.goToTab(2);
							}
						} catch (IndexOutOfBoundsException e1) {
							logger.log(Level.WARN,
									"Impossible to create the tab Sensor " + e1.getClass().getCanonicalName());
						}
					}
				}
			}
		});

		/**
		 * Definition of Button newCommonArea
		 */
		checkCommonArea = new JButton("Display Common area");
		checkCommonArea.setBounds(((int) getToolkit().getScreenSize().getWidth() * 1 / 10),
				(int) getToolkit().getScreenSize().getHeight() * 8 / 20, 200, 40);
		this.add(checkCommonArea);
		checkCommonArea.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (sensorHistorical.getIdSensor() == 0 && index != -9999) {
					JOptionPane.showMessageDialog(null, "Please select an line", "Error",
							JOptionPane.INFORMATION_MESSAGE);
				} else {
					getSensor(sensorHistorical.getIdSensor());
					tab = new JTabbedPane();
					tab = Frame.getTab();
					try {
						if (tab.isEnabledAt(1)) {
							tab.remove(1);
							tabCommonArea = new TabCommonArea(color, idemployee, sensor.getIdCommonArea(),
									"Tab Common Areas");
							tab.add(tabCommonArea, 1);
							tab.setTitleAt(1, "Tab Common Areas");
							Frame.goToTab(1);
						}
					} catch (IndexOutOfBoundsException e1) {
						logger.log(Level.WARN,
								"Impossible to create the tab CommonArea " + e1.getClass().getCanonicalName());
					}
				}
			}
		});

		/**
		 * Definition of Button Delete the line of historical
		 */
		delete = new JButton("Delete line");
		delete.setBounds(((int) getToolkit().getScreenSize().getWidth() * 1 / 10),
				(int) getToolkit().getScreenSize().getHeight() * 11 / 20, 200, 40);
		this.add(delete);
		delete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (index != -9999) {
					requestType = "DELETE";
					table = "SensorHistorical";
					ObjectMapper objectMapper = new ObjectMapper();
					try {
						jsonString = objectMapper.writeValueAsString(sensorHistorical);
						new ClientSocket(requestType, jsonString, table);
						jsonString = ClientSocket.getJson();
						if (!jsonString.equals("DELETED")) {
							JOptionPane.showMessageDialog(null, "Deletion failed", "Error", JOptionPane.ERROR_MESSAGE);
							logger.log(Level.WARN, "Impossible to delete this SensorHistorical");
						} else {
							JOptionPane.showMessageDialog(null, "Deletion succeeded", "Information",
									JOptionPane.INFORMATION_MESSAGE);
							logger.log(Level.DEBUG, "Deletion of SensorHistorical succeeded");
						}
						listM.removeElementAt(index);
						index = -9999;
					} catch (Exception e1) {
						logger.log(Level.WARN,
								"Impossible to parse in JSON SensorHistorical " + e1.getClass().getCanonicalName());
					}
				} else {
					JOptionPane.showMessageDialog(null, "Please select an line to be deleted", "Error",
							JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});

		///////////////////////// FRAME/////////////////////////////////////////////////
		/**
		 * Different parameters of the window
		 */
		this.setLayout(new BorderLayout());
		this.add(bar, BorderLayout.NORTH);
		this.setBackground(color);
	}

	/**
	 * Launch the thread
	 */
	public void threadLauncher() {
		threadListSensorHistorical.start();
		logger.log(Level.DEBUG, "Thread Historical started");
	}

	/**
	 * They find all the Sensors present in the CommonArea with the id sent by the
	 * TabCommonArea And add all this sensor in a list do be displayed
	 */
	public void updateListSensorHistorical() {
		requestType = "READ ALL";
		table = "SensorHistorical";
		ObjectMapper readMapper = new ObjectMapper();
		try {
			new ClientSocket(requestType, jsonString, table);
			jsonString = ClientSocket.getJson();
			SensorHistorical[] listSensorHistoricals = readMapper.readValue(jsonString, SensorHistorical[].class);
			listSensorHistorical = Arrays.asList(listSensorHistoricals);
			Collections.sort(listSensorHistorical, new SortByIdSensorHistorical());
			logger.log(Level.DEBUG, "Find all SensorHistorical datas succeeded");
		} catch (Exception e1) {
			logger.log(Level.WARN,
					"Impossible to parse in JSON SensorHistorical datas" + e1.getClass().getCanonicalName());
		}
		listM.clear();
		listM = new DefaultListModel<>();
		listM.addElement("ID Historical, Date, ID Sensor, State, Alert State");
		for (SensorHistorical sensorHistoricals : listSensorHistorical) {
			listM.addElement(Integer.toString(sensorHistoricals.getIdHistorical()) + " Date : "
					+ sensorHistoricals.getDate() + " ID Sensor : " + sensorHistoricals.getIdSensor() + " State : "
					+ sensorHistoricals.getSensorState() + " Alert State :" + sensorHistoricals.getAlertState());
		}
		list.setModel(listM);
	}

	/**
	 * Get info about a specific sensor with the id_sensor
	 * 
	 * @param id
	 */
	public void getSensor(int id) {
		requestType = "READ";
		sensor = new Sensor();
		table = "Sensor";
		sensor.setIdSensor(id);
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			jsonString = objectMapper.writeValueAsString(sensor);
			new ClientSocket(requestType, jsonString, table);
			jsonString = ClientSocket.getJson();
			sensor = objectMapper.readValue(jsonString, Sensor.class);
		} catch (Exception e1) {
			logger.log(Level.WARN, "Impossible to parse in JSON " + e1.getClass().getCanonicalName());
		}
	}

	/**
	 * Paint the background
	 */
	public void paintComponent(Graphics g) {
		try {
			BufferedImage backGroundImage = ImageIO.read(getClass().getClassLoader().getResource("images/background.jpg"));
			g.drawImage(backGroundImage, 0, 0, this.getWidth(), this.getHeight(), this);
		} catch (IOException e) {
			logger.log(Level.WARN, "Impossible to load the background" + e.getClass().getCanonicalName());
		}
	}

	/**
	 * @return the threadListSensorHistorical
	 */
	public Thread getThreadListSensorHistorical() {
		return threadListSensorHistorical;
	}

	/**
	 * @param threadListSensorHistorical the threadListSensorHistorical to set
	 */
	public void setThreadListSensorHistorical(Thread threadListSensorHistorical) {
		this.threadListSensorHistorical = threadListSensorHistorical;
	}
}