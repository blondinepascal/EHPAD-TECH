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
import com.ehpadtech.monitor.commons.entity.CommonArea;
import com.ehpadtech.monitor.commons.entity.Sensor;
import com.ehpadtech.monitor.launcher.ClientSocket;
import com.ehpadtech.monitor.utils.SortByIdSensor;
import com.fasterxml.jackson.databind.ObjectMapper;


public class TabListSensor extends JPanel {

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
	private Font police;
	private JButton disconnection;
	private JButton checkSensor;
	private JButton newCommonArea;
	private CommonArea commonArea;
	private CommonArea area;
	private JScrollPane sc;
	private JTabbedPane tab;
	private TabSensor tabSensor;
	private Thread threadListSensor;
	private JList<String> tableau;
	private DefaultListModel<String> tableModel;
	private List<Sensor> listSensor;
	private static final Logger logger = LogManager.getLogger(TabListSensor.class);
	private ResourceBundle rs = ResourceBundle.getBundle("parameters");

	/**
	 * Constructor
	 */
	public TabListSensor() {
	}

	/**
	 * Constructor
	 * 
	 * @param area
	 * @param idemployee
	 * @param title
	 */
	public TabListSensor(CommonArea area, int idemployee, String title) {
		this.idemployee = idemployee;
		this.area = area;
		this.listSensor = new ArrayList<Sensor>();

		///////////////////////// Thread/////////////////////////////////////////////////
		setThreadListSensor(new Thread(new Runnable() {
			/**
			 * Loop and update every 30 seconds the list of Sensors
			 */
			@Override
			public void run() {
				while (true) {
					updateListSensor();
					try {
						Thread.sleep(Integer.parseInt(rs.getString("time_threadSleep")));
					} catch (InterruptedException e) {
						logger.log(Level.WARN,
								"Impossible to sleep the thread ListSensor " + e.getClass().getCanonicalName());
					}
				}
			}
		}));

		/**
		 * Definition of the structure of this tab
		 */
		bar = new JPanel();
		bar.setBackground(Color.DARK_GRAY);
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
			/**
			 * Close the application
			 */
			public void actionPerformed(ActionEvent e) {
				logger.log(Level.INFO, "Application closed, after disconnection");
				System.exit(ABORT);
			}
		});

		///////////////////////// TABLE/////////////////////////////////////////////////
		tableModel = new DefaultListModel<String>();
		tableau = new JList<String>(tableModel);
		updateListSensor();

		/**
		 * Add a scrollBar on list
		 */
		sc = new JScrollPane(tableau);
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
				index = tableau.locationToIndex(e.getPoint());
				String substring = tableModel.getElementAt(index).toString();
				int position = substring.indexOf(" ");
				index = Integer.parseInt(substring.substring(0, position));
			}
		};
		tableau.addMouseListener(mouseListener);

		///////////////////////// BUTTON/////////////////////////////////////////////////
		/**
		 * Definition of Button CheckSensor
		 */
		checkSensor = new JButton("Display Sensor");
		checkSensor.setBounds(((int) getToolkit().getScreenSize().getWidth() * 5 / 10),
				(int) getToolkit().getScreenSize().getHeight() * 15 / 20, 150, 40);
		this.add(checkSensor);
		checkSensor.addActionListener(new ActionListener() {
			/**
			 * If we pressed the CheckSensorButton we go to the TabSensor to watch the
			 * informations about this sensor
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				if (index == 0) {
					JOptionPane.showMessageDialog(null, "Please select an sensor", "Information",
							JOptionPane.INFORMATION_MESSAGE);
				} else {
					tab = new JTabbedPane();
					tab = Frame.getTab();

					tab.remove(2);
					tabSensor = new TabSensor(Color.GRAY, idemployee, "Tab Sensors", index);
					tab.add(tabSensor, 2);
					tab.setTitleAt(2, "Tab Sensors");
					Frame.goToTab(2);
				}
			}
		});

		/**
		 * Definition of Button newCommonArea
		 */
		newCommonArea = new JButton("New common area");
		newCommonArea.setBounds(((int) getToolkit().getScreenSize().getWidth() * 3 / 10),
				(int) getToolkit().getScreenSize().getHeight() * 15 / 20, 200, 40);
		this.add(newCommonArea);
		newCommonArea.addActionListener(new ActionListener() {
			/**
			 * If we pressed the newCommonAreaButton we close this Tab, and we go to
			 * TabCommonArea to find a new CommonArea
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				tab = new JTabbedPane();
				tab = Frame.getTab();
				tab.remove(7);
				Frame.goToTab(1);
			}
		});

		///////////////////////// FRAME/////////////////////////////////////////////////
		/**
		 * Different parameters of the window
		 */
		this.setLayout(new BorderLayout());
		this.add(bar, BorderLayout.NORTH);
		this.setBackground(Color.GRAY);
	}

	/**
	 * Launch thread
	 */
	public void threadLauncher() {
		threadListSensor.start();
		logger.log(Level.DEBUG, "Thread ListSensor started");
	}

	/**
	 * They find all the Sensors present in the CommonArea with the id sent by the
	 * TabCommonArea And add all this sensor in a list do be displayed
	 */
	public void updateListSensor() {
		requestType = "FIND ALL";
		commonArea = new CommonArea();
		Sensor sensor = new Sensor();
		table = "Sensor";
		ObjectMapper readMapper = new ObjectMapper();
		sensor.setIdCommonArea(area.getIdCommonArea());
		try {
			jsonString = readMapper.writeValueAsString(sensor);
			new ClientSocket(requestType, jsonString, table);
			jsonString = ClientSocket.getJson();
			Sensor[] listSensors = readMapper.readValue(jsonString, Sensor[].class);
			listSensor = Arrays.asList(listSensors);
			Collections.sort(listSensor, new SortByIdSensor());
			commonArea.setListSensor(listSensor);
			logger.log(Level.DEBUG, "Find all Sensors datas succeeded");
		} catch (Exception e1) {
			logger.log(Level.WARN, "Impossible to parse in JSON Sensor datas" + e1.getClass().getCanonicalName());
		}

		tableModel.clear();
		tableModel = new DefaultListModel<>();
		tableModel.addElement("ID, Sensor type, State, Name common area");
		for (Sensor sensors : listSensor) {
			tableModel.addElement(Integer.toString(sensors.getIdSensor()) + " " + sensors.getTypeSensor() + " "
					+ sensors.getSensorState() + " " + area.getNameCommonArea());
		}
		tableau.setModel(tableModel);
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
	 * @return the threadListSensor
	 */
	public Thread getThreadListSensor() {
		return threadListSensor;
	}

	/**
	 * @param threadListSensor the threadListSensor to set
	 */
	public void setThreadListSensor(Thread threadListSensor) {
		this.threadListSensor = threadListSensor;
	}

	/**
	 * @return the area
	 */
	public CommonArea getArea() {
		return area;
	}

	/**
	 * @param area the area to set
	 */
	public void setArea(CommonArea area) {
		this.area = area;
	}
}