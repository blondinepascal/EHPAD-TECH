package com.ehpadtech.monitor.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ehpadtech.monitor.commons.entity.Alert;
import com.ehpadtech.monitor.commons.entity.CommonArea;
import com.ehpadtech.monitor.commons.entity.Floor;
import com.ehpadtech.monitor.commons.entity.Sensor;
import com.ehpadtech.monitor.launcher.ClientSocket;
import com.ehpadtech.monitor.utils.SortByIdSensor;
import com.fasterxml.jackson.databind.ObjectMapper;



public class TabMapSensor extends JPanel implements MouseListener {

	private int index;
	private int idEmployee;
	private Sensor sensor;
	private Font police;
	private JButton disconnection;
	private JButton configureSensor;
	private JButton switchButton;
	private JPanel bar;
	private JLabel labelIdEmployee;
	private JLabel labelHeadList;
	private JTabbedPane tab;
	private JTextField textInputIdSensor;
	private JTextField textInputAlertState;
	private JTextField textInputThresholdMin;
	private JTextField textInputThresholdMax;

	private String requestType;
	private String table;
	private String jsonString;
	private CommonArea commonArea;
	private ObjectMapper objectMapper;
	private Thread threadMapSensor;
	private JScrollPane sc;

	private BufferedImage img = null;
	private BufferedImage img1 = null;
	private BufferedImage buffer = null;
	private BufferedImage buffer1 = null;
	private Point p = null;

	private JList<String> list;
	private JComboBox<String> textFloor;
	private JComboBox<String> textInputNameCommonArea;
	private JComboBox<String> textInputTypeSensor;
	private JComboBox<String> textInputSensitivity;
	private JComboBox<Integer> textInputHourStartActivity;
	private JComboBox<Integer> textInputMinuteStartActivity;
	private JComboBox<Integer> textInputHourEndActivity;
	private JComboBox<Integer> textInputMinuteEndActivity;

	private DefaultListModel<String> listM;
	private List<Sensor> listSensor = new ArrayList<Sensor>();
	private List<Alert> listAlert = new ArrayList<Alert>();
	private List<String> listNameRectangle = new ArrayList<String>();
	private Map<Integer, String> listRectangleCommonArea = new HashMap<Integer, String>();

	private ResourceBundle rsParameters = ResourceBundle.getBundle("parameters");
	private List<CommonArea> listCommonArea = new ArrayList<CommonArea>();
	private static final long serialVersionUID = 1L;

	private static final Logger logger = LogManager.getLogger(TabMapSensor.class);

	private static int x0;
	private static int y0;
	private static int x1;
	private static int y1;

	private static Rectangle kitchenE0;
	private static Rectangle dinningRoomE0;
	private static Rectangle staffRoomE0;
	private static Rectangle relaxationRoomE0;
	private static Rectangle corridorDE0;
	private static Rectangle corridorCE0;
	private static Rectangle corridorEE0;
	private static Rectangle infirmaryE0;
	private static Rectangle corridorAE0;
	private static Rectangle corridorBE0;
	private static Rectangle sittingRoomE0;
	private static Rectangle livingRoomE0;
	private static Rectangle entranceHallE0;
	private static Rectangle elevatorAE0;
	private static Rectangle elevatorBE0;

	private static Rectangle sittingRoomE1;
	private static Rectangle dinningRoomE1;
	private static Rectangle kitchenE1;
	private static Rectangle corridorAE1;
	private static Rectangle corridorBE1;
	private static Rectangle corridorCE1;
	private static Rectangle corridorDE1;
	private static Rectangle corridorEE1;
	private static Rectangle corridorFE1;
	private static Rectangle relaxationRoomE1;
	private static Rectangle livingRoomAE1;
	private static Rectangle staffRoomE1;
	private static Rectangle livingRoomBE1;
	private static Rectangle infirmaryE1;
	private static Rectangle elevatorAE1;
	private static Rectangle elevatorBE1;

	/**
	 * Constructor
	 */
	public TabMapSensor() {
	}

	/**
	 * Constructor
	 * 
	 * @param color
	 * @param idEmployee
	 * @param title
	 * @param idSensor
	 */
	public TabMapSensor(Color color, int idEmployee, String title, int idSensor) {
		this.idEmployee = idEmployee;
		this.addMouseListener(this);

		setThreadMapSensor(new Thread(new Runnable() {
			/**
			 * Loop and update every 30 seconds the list of Sensor with their alert state
			 */
			@Override
			public void run() {
				while (true) {
					updateListSensor();
					try {
						Thread.sleep(Integer.parseInt(rsParameters.getString("time_threadSleep")));
					} catch (InterruptedException e) {
						logger.log(Level.INFO, "Impossible to sleep the thread " + e.getClass().getCanonicalName());
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
		 * Definition of label Login on header bar
		 */
		labelIdEmployee = new JLabel("Login :   " + this.idEmployee + "    ");
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

		///////////////////////// FROM LIST SENSOR//////////////////////////////////////
		/**
		 * if the is before on TabListSensor they send a idSensor and if they are
		 * different to 0 they send a request to get informations about this Sensor
		 */
		if (idSensor != 0) {
			requestType = "READ";
			sensor = new Sensor();
			table = "Sensor";
			ObjectMapper readMapper = new ObjectMapper();
			sensor.setIdSensor(idSensor);
			try {
				jsonString = readMapper.writeValueAsString(sensor);
				new ClientSocket(requestType, jsonString, table);
				jsonString = ClientSocket.getJson();
				sensor = readMapper.readValue(jsonString, Sensor.class);
				logger.log(Level.DEBUG, "Find Sensor datas succed");
			} catch (Exception e1) {
				logger.log(Level.WARN, "Impossible to parse in JSON Sensor datas " + e1.getClass().getCanonicalName());
			}
		}

		///////////////////////// LIST SENSOR///////////////////////////////////////////
		listM = new DefaultListModel<String>();
		list = new JList<String>(listM);
		updateListSensor();

		/**
		 * Add a scrollBar on list
		 */
		sc = new JScrollPane(list);
		sc.setBounds(30, 170, 300, ((int) getToolkit().getScreenSize().getHeight() - 350));
		this.add(sc);

		/**
		 * when we pressed a line in the list they will send a request to get all the
		 * information about the Sensor selected to be displayed on the textField
		 */
		index = -9999;
		MouseListener mouseListener = new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				index = list.locationToIndex(e.getPoint());
				String substring = listM.getElementAt(index).toString();
				int position = substring.indexOf("#");
				if (position > -1) {
					String id = substring.substring(0, position);
					/**
					 * Find the Sensor by the id get on list
					 */
					requestType = "READ";
					sensor = new Sensor();
					table = "Sensor";
					sensor.setIdSensor(Integer.parseInt(id));
					try {
						jsonString = objectMapper.writeValueAsString(sensor);
						new ClientSocket(requestType, jsonString, table);
						jsonString = ClientSocket.getJson();
						sensor = objectMapper.readValue(jsonString, Sensor.class);
					} catch (IOException e1) {
						logger.log(Level.WARN, "Impossible to parse in JSON " + e1.getClass().getCanonicalName());
					}
					textInputIdSensor.setText(Integer.toString(sensor.getIdSensor()));
					textInputAlertState.setText(sensor.getAlertState().name());
					textInputSensitivity.setSelectedItem(sensor.getSensitivity().name());
					textInputTypeSensor.setSelectedItem(sensor.getTypeSensor().name());
					textInputThresholdMin.setText(Integer.toString(sensor.getThresholdMin()));
					textInputThresholdMax.setText(Integer.toString(sensor.getThresholdMax()));
					convertActivityTime(sensor.getStartActivity(), sensor.getEndActivity());
					if (sensor.getSensorState()) {
						switchButton.setText("ON");
						switchButton.setBackground(Color.GREEN);
					} else {
						switchButton.setText("OFF");
						switchButton.setBackground(Color.RED);
					}

					/**
					 * Find the CommonAreaName by the idCommonArea get on list
					 */
					requestType = "READ";
					commonArea = new CommonArea();
					table = "CommonArea";
					ObjectMapper readMapper = new ObjectMapper();
					commonArea.setIdCommonArea(sensor.getIdCommonArea());
					try {
						jsonString = readMapper.writeValueAsString(commonArea);
						new ClientSocket(requestType, jsonString, table);
						jsonString = ClientSocket.getJson();
						commonArea = readMapper.readValue(jsonString, CommonArea.class);
					} catch (IOException e1) {
						logger.log(Level.WARN, "Impossible to parse in JSON " + e1.getClass().getCanonicalName());
					}
					String str = commonArea.getNameCommonArea() + " #" + sensor.getIdCommonArea();
					for (int i = 0; i < textInputNameCommonArea.getItemCount(); i++) {
						if (textInputNameCommonArea.getItemAt(i).toString().contains(str)) {
							textInputNameCommonArea.setSelectedIndex(i);
						}
					}
				}
			}
		};
		list.addMouseListener(mouseListener);

		/**
		 * Definition of label HeadList
		 */
		labelHeadList = new JLabel("ID /Type /State /ID common area /  :");
		labelHeadList.setBounds(40, 140, 300, 30);
		labelHeadList.setFont(police);
		labelHeadList.setForeground(Color.WHITE);
		this.add(labelHeadList);

		///////////////////////// JComboBox/////////////////////////////////////////////
		/**
		 * List of floor
		 */
		textFloor = new JComboBox<String>();
		textFloor.setBounds(40, 90, 140, 30);
		add(textFloor);
		textFloor.addItem("-");
		for (Floor listFloor : Floor.values()) {
			textFloor.addItem(listFloor.name());
		}
		textFloor.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					textFloor.getSelectedItem();

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

		///////////////////////// BUTTON////////////////////////////////////////////////
		/**
		 * Definition of Button CheckSensor
		 */
//		configureSensor = new JButton("Configure Sensor");
//		configureSensor.setBounds(30, (int) getToolkit().getScreenSize().getHeight() * 16 / 20, 150, 40);
//		add(configureSensor);
//		configureSensor.addActionListener(new ActionListener() {
//
//			private TabSensor tabSensor;
//
//			/**
//			 * If we pressed the Configure Sensor Button we go to the TabSensor to watch the
//			 * informations about this sensor
//			 */
//			@Override
//			public void actionPerformed(ActionEvent e) {
//
//				if (index == 0 && index == -9999) {
//					JOptionPane.showMessageDialog(null, "Please select an sensor", "Information",
//							JOptionPane.INFORMATION_MESSAGE);
//				} else {
//					tab = new JTabbedPane();
//					tab = Frame.getTab();
//
//					tab.remove(2);
//					tabSensor = new TabSensor(Color.GRAY, idEmployee, "Tab Sensors", index);
//					tab.add(tabSensor, 2);
//					tab.setTitleAt(2, "Tab Sensors");
//					Frame.goToTab(2);
//				}
//			}
//		});

		///////////////////////// JScrollPane///////////////////////////////////////////

//		JScrollPane scroll = new JScrollPane(panel, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
//		scroll.setLocation(1000,1000);
//		this.add(scroll);

		///////////////////////// IMAGE/////////////////////////////////////////////////

		listRectangleCommonArea.put(1, "entrancehalle0");
		listRectangleCommonArea.put(2, "livingRoome0");
		listRectangleCommonArea.put(3, "sittingroome0");
		listRectangleCommonArea.put(4, "corridore0a");
		listRectangleCommonArea.put(5, "corridore0b");
		listRectangleCommonArea.put(6, "corridore0c");
		listRectangleCommonArea.put(7, "corridore0d");
		listRectangleCommonArea.put(8, "corridore0e");
		listRectangleCommonArea.put(9, "elevatore0a");
		listRectangleCommonArea.put(10, "elevatore0b");
		listRectangleCommonArea.put(11, "staffroome0");
		listRectangleCommonArea.put(12, "kitchene0");
		listRectangleCommonArea.put(13, "infirmarye0");
		listRectangleCommonArea.put(14, "relaxationroome0");
		listRectangleCommonArea.put(15, "dinningroome0");
		listRectangleCommonArea.put(16, "staffroome1");
		listRectangleCommonArea.put(17, "livingRoomea1");
		listRectangleCommonArea.put(18, "corridore1a");
		listRectangleCommonArea.put(19, "corridore1b");
		listRectangleCommonArea.put(20, "corridore1c");
		listRectangleCommonArea.put(21, "corridore1d");
		listRectangleCommonArea.put(22, "corridore1e");
		listRectangleCommonArea.put(23, "corridore1f");
		listRectangleCommonArea.put(24, "dinningroome1");
		listRectangleCommonArea.put(25, "infirmarye1");
		listRectangleCommonArea.put(26, "relaxationroome1");
		listRectangleCommonArea.put(27, "elevatorae1");
		listRectangleCommonArea.put(28, "elevatorbe1");
		listRectangleCommonArea.put(29, "kitchene1");
		listRectangleCommonArea.put(30, "sittingroome1");
		listRectangleCommonArea.put(31, "livingroomeb1");

	}

	@Override
	public void paintComponent(Graphics g) {

		// 50% transparent
		int alpha = 127;

		super.paintComponent(g);
		try {
			BufferedImage backGroundImage = ImageIO
					.read(getClass().getClassLoader().getResource("images/background.jpg"));
			g.drawImage(backGroundImage, 0, 0, this.getWidth(), this.getHeight(), this);
		} catch (IOException e) {
			logger.log(Level.WARN, "Impossible to load the background" + e.getClass().getCanonicalName());
		}

		if (textFloor.getSelectedItem().equals("GROUND_FLOOR")) {
			Graphics2D g2 = (Graphics2D) g;
			try {
				img = ImageIO.read(getClass().getClassLoader().getResource("images/Floor0.jpg"));
				this.buffer = img;

				x0 = (int) getToolkit().getScreenSize().getWidth() * 6 / 20;
				y0 = (int) getToolkit().getScreenSize().getHeight() * 3 / 20;

				g2.drawImage(buffer, x0, y0, 900, 580, this);

				entranceHallE0 = new Rectangle(x0 + 720, y0 + 85, 176, 407);
				kitchenE0 = new Rectangle(x0, y0 + 119, 147, 66);
				dinningRoomE0 = new Rectangle(x0, y0 + 187, 147, 202);
				staffRoomE0 = new Rectangle(x0, y0 + 391, 147, 64);
				relaxationRoomE0 = new Rectangle(x0 + 143, y0, 430, 83);
				infirmaryE0 = new Rectangle(x0 + 147, y0 + 492, 428, 84);
				corridorAE0 = new Rectangle(x0 + 216, y0 + 203, 502, 43);
				corridorBE0 = new Rectangle(x0 + 216, y0 + 324, 502, 46);
				corridorCE0 = new Rectangle(x0 + 149, y0 + 120, 64, 334);
				corridorDE0 = new Rectangle(x0, y0 + 85, 718, 33);
				corridorEE0 = new Rectangle(x0, y0 + 456, 718, 35);
				sittingRoomE0 = new Rectangle(x0 + 505, y0 + 120, 213, 80);
				livingRoomE0 = new Rectangle(x0 + 505, y0 + 373, 213, 81);
				elevatorAE0 = new Rectangle(x0 + 216, y0 + 246, 48, 29);
				elevatorBE0 = new Rectangle(x0 + 216, y0 + 295, 48, 27);

				Color myColour = new Color(255, 255, 255, alpha);
				g.setColor(myColour);

				// Draw rectangle
				g2.drawRect(kitchenE0.x, kitchenE0.y, kitchenE0.width, kitchenE0.height);
				g2.drawRect(dinningRoomE0.x, dinningRoomE0.y, dinningRoomE0.width, dinningRoomE0.height);
				g2.drawRect(staffRoomE0.x, staffRoomE0.y, staffRoomE0.width, staffRoomE0.height);
				g2.drawRect(relaxationRoomE0.x, relaxationRoomE0.y, relaxationRoomE0.width, relaxationRoomE0.height);
				g2.drawRect(infirmaryE0.x, infirmaryE0.y, infirmaryE0.width, infirmaryE0.height);
				g2.drawRect(corridorAE0.x, corridorAE0.y, corridorAE0.width, corridorAE0.height);
				g2.drawRect(corridorBE0.x, corridorBE0.y, corridorBE0.width, corridorBE0.height);
				g2.drawRect(corridorCE0.x, corridorCE0.y, corridorCE0.width, corridorCE0.height);
				g2.drawRect(corridorDE0.x, corridorDE0.y, corridorDE0.width, corridorDE0.height);
				g2.drawRect(corridorEE0.x, corridorEE0.y, corridorEE0.width, corridorEE0.height);
				g2.drawRect(sittingRoomE0.x, sittingRoomE0.y, sittingRoomE0.width, sittingRoomE0.height);
				g2.drawRect(livingRoomE0.x, livingRoomE0.y, livingRoomE0.width, livingRoomE0.height);
				g2.drawRect(entranceHallE0.x, entranceHallE0.y, entranceHallE0.width, entranceHallE0.height);
				g2.drawRect(elevatorAE0.x, elevatorAE0.y, elevatorAE0.width, elevatorAE0.height);
				g2.drawRect(elevatorBE0.x, elevatorBE0.y, elevatorBE0.width, elevatorBE0.height);

			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		if (textFloor.getSelectedItem().equals("FIRST_FLOOR")) {
			Graphics2D g2 = (Graphics2D) g;
			try {
				img1 = ImageIO.read(getClass().getClassLoader().getResource("images/Floor1.jpg"));
				this.buffer1 = img1;
				g2.drawImage(buffer1, x0, y0, 900, 580, this);

				x1 = (int) getToolkit().getScreenSize().getWidth() * 6 / 20 + 5;
				y1 = (int) getToolkit().getScreenSize().getHeight() * 3 / 20 + 3;

				sittingRoomE1 = new Rectangle(x1, y1, 168, 116);
				dinningRoomE1 = new Rectangle(x1, y1 + 202, 168, 164);
				kitchenE1 = new Rectangle(x1, y1 + 451, 168, 125);
				corridorAE1 = new Rectangle(x1 + 170, y1 + 82, 721, 34);
				corridorBE1 = new Rectangle(x1 + 248, y1 + 202, 578, 39);
				corridorCE1 = new Rectangle(x1 + 248, y1 + 321, 578, 45);
				corridorDE1 = new Rectangle(x1 + 170, y1 + 451, 721, 39);
				corridorEE1 = new Rectangle(x1 + 172, y1 + 118, 74, 332);
				corridorFE1 = new Rectangle(x1 + 829, y1 + 118, 62, 330);
				relaxationRoomE1 = new Rectangle(x1 + 337, y1, 389, 80);
				livingRoomAE1 = new Rectangle(x1 + 414, y1 + 118, 247, 82);
				staffRoomE1 = new Rectangle(x1 + 639, y1 + 243, 188, 76);
				livingRoomBE1 = new Rectangle(x1 + 413, y1 + 368, 248, 81);
				infirmaryE1 = new Rectangle(x1 + 337, y1 + 492, 389, 84);
				elevatorAE1 = new Rectangle(x1 + 248, y1 + 244, 52, 28);
				elevatorBE1 = new Rectangle(x1 + 248, y1 + 290, 52, 29);

				Color myColour = new Color(255, 255, 255, alpha);
				g.setColor(myColour);

//				g2.drawRect(sittingRoomE1.x, sittingRoomE1.y, sittingRoomE1.width, sittingRoomE1.height);
//				g2.drawRect(dinningRoomE1.x, dinningRoomE1.y, dinningRoomE1.width, dinningRoomE1.height);
//				g2.drawRect(kitchenE1.x, kitchenE1.y, kitchenE1.width, kitchenE1.height);
//				g2.drawRect(corridorAE1.x, corridorAE1.y, corridorAE1.width, corridorAE1.height);
//				g2.drawRect(corridorBE1.x, corridorBE1.y, corridorBE1.width, corridorBE1.height);
//				g2.drawRect(corridorCE1.x, corridorCE1.y, corridorCE1.width, corridorCE1.height);
//				g2.drawRect(corridorDE1.x, corridorDE1.y, corridorDE1.width, corridorDE1.height);
//				g2.drawRect(corridorEE1.x, corridorEE1.y, corridorEE1.width, corridorEE1.height);
//				g2.drawRect(corridorFE1.x, corridorFE1.y, corridorFE1.width, corridorFE1.height);
//				g2.drawRect(relaxationRoomE1.x, relaxationRoomE1.y, relaxationRoomE1.width, relaxationRoomE1.height);
//				g2.drawRect(livingRoomAE1.x, livingRoomAE1.y, livingRoomAE1.width, livingRoomAE1.height);
//				g2.drawRect(staffRoomE1.x, staffRoomE1.y, staffRoomE1.width, staffRoomE1.height);
//				g2.drawRect(livingRoomBE1.x, livingRoomBE1.y, livingRoomBE1.width, livingRoomBE1.height);
//				g2.drawRect(infirmaryE1.x, infirmaryE1.y, infirmaryE1.width, infirmaryE1.height);
//				g2.drawRect(elevatorAE1.x, elevatorAE1.y, elevatorAE1.width, elevatorAE1.height);
//				g2.drawRect(elevatorBE1.x, elevatorBE1.y, elevatorBE1.width, elevatorBE1.height);

			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}

		repaint();
	}

	private void testLocation(Point mouse, int set, Rectangle rectangle) {
		// if the mouse if here
		if (rectangle.contains(mouse)) {
			sensorByCommonArea(set);
		} else {
			logger.log(Level.DEBUG, "It's not a Common Area");
		}

	}

	/**
	 * Mouse Clicked
	 */
	public void mouseClicked(MouseEvent e) {
		// recovering the position of the mouse
		p = e.getPoint();

		if (textFloor.getSelectedItem().equals("GROUND_FLOOR")) {
			testLocation(p, 1, entranceHallE0);
			testLocation(p, 2, kitchenE0);
			testLocation(p, 3, dinningRoomE0);
			testLocation(p, 4, staffRoomE0);
			testLocation(p, 5, relaxationRoomE0);
			testLocation(p, 6, infirmaryE0);
			testLocation(p, 7, corridorAE0);
			testLocation(p, 8, corridorBE0);
			testLocation(p, 9, corridorCE0);
			testLocation(p, 10, corridorDE0);
			testLocation(p, 11, corridorEE0);
			testLocation(p, 12, sittingRoomE0);
			testLocation(p, 13, livingRoomE0);
			testLocation(p, 14, elevatorAE0);
			testLocation(p, 15, elevatorBE0);
		} else if (textFloor.getSelectedItem().equals("FIRST_FLOOR")) {
//			testLocation(p, 16, sittingRoomE1);
//			testLocation(p, 17, dinningRoomE1);
//			testLocation(p, 18, kitchenE1);
//			testLocation(p, 19, corridorAE1);
//			testLocation(p, 20, corridorBE1);
//			testLocation(p, 21, corridorCE1);
//			testLocation(p, 22, corridorDE1);
//			testLocation(p, 23, corridorEE1);
//			testLocation(p, 24, corridorFE1);
//			testLocation(p, 25, relaxationRoomE1);
//			testLocation(p, 26, livingRoomAE1);
//			testLocation(p, 27, staffRoomE1);
//			testLocation(p, 28, livingRoomBE1);
//			testLocation(p, 29, infirmaryE1);
//			testLocation(p, 30, elevatorAE1);
//			testLocation(p, 31, elevatorBE1);
		} else {
			logger.log(Level.DEBUG, "It's not a Common Area");
		}
	}

	/**
	 * Mouse Entered
	 */
	@Override
	public void mouseEntered(MouseEvent e) {
		p = e.getPoint();
	}

	/**
	 * Sensors are displayed by Common Area
	 * 
	 * @param set
	 */
	public void sensorByCommonArea(int set) {
		sensor = new Sensor();
		requestType = "READ ALL";
		table = "Sensor";

		try {
			jsonString = "READ ALL";
			new ClientSocket(requestType, jsonString, table);
			jsonString = ClientSocket.getJson();
			Sensor[] sensors = objectMapper.readValue(jsonString, Sensor[].class);
			listSensor = Arrays.asList(sensors);
			Collections.sort(listSensor, new SortByIdSensor());
			logger.log(Level.DEBUG, "Find Sensor data succed");
		} catch (Exception e1) {
			logger.log(Level.WARN, "Impossible to parse in JSON Sensor data " + e1.getClass().getCanonicalName());
		}

		listM.clear();
		listM = new DefaultListModel<>();
		listM.addElement("All sensors by Common Area : " + set);
		for (Sensor sensor : listSensor) {
			if (set == (sensor.getIdCommonArea())) {
				listM.addElement(sensor.getIdSensor() + "# " + sensor.getTypeSensor() + " ," + sensor.getSensorState()
						+ " ," + sensor.getIdCommonArea() + " , " + sensor.getAlertState().name() + " , "
						+ sensor.getSensitivity().name());
			}
		}
		list.setModel(listM);
	}

//	public void displayRectangle() {
//
//		commonArea = new CommonArea();
//
//		requestType = "READ ALL";
//		table = "CommonArea";
//
//		listCommonArea = getAllCommonArea(commonArea, requestType, table);
//		for (CommonArea c : listCommonArea) {
//			System.out.println("=====> " + c);
//		}
//		System.out.println("111111");
//		for (int i = 0; i < listNameRectangle.size(); i++) {
//			for (CommonArea areas : listCommonArea) {
//				if (listNameRectangle.contains(areas.getNameCommonArea().toLowerCase().trim())) {
//					System.out.println("22222222");
//					listRectangleCommonArea.put(areas.getIdCommonArea(), listNameRectangle.get(i));
//				}
//			}
//		}
//	}

	/**
	 * 
	 * @param commonArea
	 * @param requestType
	 * @param table
	 * @return
	 */
	public List<CommonArea> getAllCommonArea(CommonArea commonArea, String requestType, String table) {
		objectMapper = new ObjectMapper();
		try {
			jsonString = objectMapper.writeValueAsString(commonArea);
			new ClientSocket(requestType, jsonString, table);
			jsonString = ClientSocket.getJson();
			CommonArea[] commonAreas = objectMapper.readValue(jsonString, CommonArea[].class);
			logger.log(Level.DEBUG, "Find CommonArea data succed");
			return Arrays.asList(commonAreas);
		} catch (Exception e1) {
			logger.log(Level.WARN, "Impossible to parse in JSON CommonArea datas " + e1.getClass().getCanonicalName());
			return null;
		}
	}

	public void updateListSensor() {
		requestType = "READ ALL";
		table = "Sensor";
		objectMapper = new ObjectMapper();
		try {
			jsonString = "READ ALL";
			new ClientSocket(requestType, jsonString, table);
			jsonString = ClientSocket.getJson();
			Sensor[] sensors = objectMapper.readValue(jsonString, Sensor[].class);
			listSensor = Arrays.asList(sensors);
			logger.log(Level.DEBUG, "Find Sensor data succed");
		} catch (Exception e1) {
			logger.log(Level.WARN, "Impossible to parse in JSON Sensor data " + e1.getClass().getCanonicalName());
		}
		listM.clear();
		listM = new DefaultListModel<>();
		listM.addElement("All sensors");
		for (Sensor sens : listSensor) {
			listM.addElement(sens.getIdSensor() + "# " + sens.getTypeSensor() + " ," + sens.getSensorState() + " ,"
					+ sens.getIdCommonArea() + " , " + sens.getAlertState().name() + " , "
					+ sens.getSensitivity().name());
		}
		list.setModel(listM);
	}

	public void updateListCommonArea() {
		commonArea = new CommonArea();

		requestType = "READ ALL";
		table = "CommonArea";
		listCommonArea = getAllCommonArea(null, requestType, table);
		listM.clear();
		listM = new DefaultListModel<>();
		listM.addElement("All commons areas");
		for (CommonArea commonAreas : listCommonArea) {
			listM.addElement(commonAreas.getIdCommonArea() + "# " + commonAreas.getNameCommonArea() + " ,"
					+ commonAreas.getFloorCommonArea() + ",");
		}
		list.setModel(listM);
		logger.log(Level.DEBUG, "List CommonArea updated wih success");
	}

	/**
	 * 
	 * @param startActivity
	 * @param endActivity
	 */
	public void convertActivityTime(Time startActivity, Time endActivity) {
		String sA = startActivity.toString();
		String eA = endActivity.toString();

		int position = sA.indexOf(":");
		textInputHourStartActivity.setSelectedIndex(Integer.parseInt(sA.substring(0, position).trim()));
		textInputMinuteStartActivity
				.setSelectedIndex(Integer.parseInt(sA.substring(position + 1, position + 3).trim()));
		position = eA.indexOf(":");
		textInputHourEndActivity.setSelectedIndex(Integer.parseInt(eA.substring(0, position).trim()));
		textInputMinuteEndActivity.setSelectedIndex(Integer.parseInt(eA.substring(position + 1, position + 3).trim()));
	}

	/**
	 * @return the threadMapSensor
	 */
	public Thread getThreadMapSensor() {
		return threadMapSensor;
	}

	/**
	 * @param threadMapSensor the threadMapSensor to set
	 */
	public void setThreadMapSensor(Thread threadMapSensor) {
		this.threadMapSensor = threadMapSensor;
	}

	/**
	 * @return the listSensor
	 */
	public List<Sensor> getListSensor() {
		return listSensor;
	}

	/**
	 * @param listSensor the listSensor to set
	 */
	public void setListSensor(List<Sensor> listSensor) {
		this.listSensor = listSensor;
	}

	/**
	 * @return the listAlert
	 */
	public List<Alert> getListAlert() {
		return listAlert;
	}

	/**
	 * @param listAlert the listAlert to set
	 */
	public void setListAlert(List<Alert> listAlert) {
		this.listAlert = listAlert;
	}

	/**
	 * 
	 * @return
	 */
	public List<String> getListNameRectangle() {
		return listNameRectangle;
	}

	/**
	 * 
	 * @param listNameRectangle
	 */
	public void setListNameRectangle(List<String> listNameRectangle) {
		this.listNameRectangle = listNameRectangle;
	}

	/**
	 * 
	 * @return
	 */
	public Map<Integer, String> getListRectangleCommonArea() {
		return listRectangleCommonArea;
	}

	/**
	 * 
	 * @param listRectangleCommonArea
	 */
	public void setListRectangleCommonArea(Map<Integer, String> listRectangleCommonArea) {
		this.listRectangleCommonArea = listRectangleCommonArea;
	}

	/**
	 * 
	 * @param sensor
	 */
	public void addSensor(Sensor sensor) {
		this.listSensor.add(sensor);
	}

	/**
	 * 
	 * @param sensor
	 */
	public void removeSensor(Sensor sensor) {
		this.listSensor.remove(sensor);
	}

	/**
	 * Mouse Pressed
	 */
	@Override
	public void mousePressed(MouseEvent e) {
	}

	/**
	 * Mouse Released
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
	}

	/**
	 * Mouse Exited
	 */
	@Override
	public void mouseExited(MouseEvent e) {
	}

}
