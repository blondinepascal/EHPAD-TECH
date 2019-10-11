package com.ehpadtech.monitor.analysis.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.ehpadtech.monitor.commons.entity.CommonArea;
import com.ehpadtech.monitor.commons.entity.Sensor;
import com.ehpadtech.monitor.commons.entity.SensorType;
import com.fasterxml.jackson.databind.ObjectMapper;


public class TabSensor extends JPanel {

	/**
	 * Different parameters used
	 */
	private static final long serialVersionUID = 1L;
	private String requestType;
	private String table;
	private String jsonString;
	private int idemployee;
	private int index;

	private JPanel bar;
	private JPanel search;
	private JLabel labelIdEmployee;
	private JLabel labelSearch;
	private JLabel labelIdSensor;
	private JLabel labelNameCommonArea;
	private JLabel labelTypeSensor;
	private JLabel labelStateSensor;
	private JLabel labelHeadList;
	private JLabel labelNumber;
	private JTextField textInputIdSensor;
	private JTextField searchBar;
	private Font policeBar;
	private Font policeLabel;
	private JButton disconnection;
	private JButton addSensor;
	private JButton save;
	private JButton restaure;
	private JButton delete;
	private JButton switchButton;
	private JButton validButton;
	private Sensor sensor;
	private Sensor sensor2;
	private JScrollPane sc;
	private CommonArea commonArea;
	private ObjectMapper objectMapper;
	private Thread threadSensor;
	private JComboBox<String> textInputNameCommonArea;
	private JComboBox<String> textInputTypeSensor;
	private JComboBox<String> nombreSensor;
	private DefaultListModel<String> listM;
	private JList<String> list;
	private List<Sensor> listSensor = new ArrayList<Sensor>();
	private List<Sensor> listSearchSensor = new ArrayList<Sensor>();
	private List<CommonArea> listCommonArea = new ArrayList<CommonArea>();
	private static final Logger logger = LogManager.getLogger(TabSensor.class);

	public TabSensor() {
	}

	public TabSensor(Color color, int idemployee, String title, int idSensor) {
		this.idemployee = idemployee;

		setThreadSensor(new Thread(new Runnable() {
			/**
			 * Loop and update every 30 sec the list of employees
			 */
			@Override
			public void run() {
				while (true) {
					updateListSensor();
					updateListAreas();
					updateSensorSelected();
					try {
						Thread.sleep(30000);
					} catch (InterruptedException e) {
						logger.log(Level.INFO, "Impossible to sleep the thread" + e.getClass().getCanonicalName());
					}
				}
			}
		}));

		/**
		 * Definition of the structure of this tab
		 */
		bar = new JPanel();
		bar.setBackground(Color.PINK);
		bar.setPreferredSize(new Dimension((int) getToolkit().getScreenSize().getWidth(), 80));
		bar.setLayout(new BorderLayout());
		bar.setBorder(BorderFactory.createMatteBorder(20, 100, 20, 100, bar.getBackground()));

		///////////////////////// BAR/////////////////////////////////////////////////
		/**
		 * Definition of label Identifiant on header bar
		 */
		labelIdEmployee = new JLabel("Identifiant :   " + this.idemployee + "    ");
		policeBar = new Font("Arial", Font.BOLD, 16);
		labelIdEmployee.setForeground(Color.WHITE);
		labelIdEmployee.setFont(policeBar);
		bar.add(labelIdEmployee, BorderLayout.WEST);

		/**
		 * Definition of the button and the different action after pressed the button
		 */
		disconnection = new JButton("Se Déconnecter");
		bar.add(disconnection, BorderLayout.EAST);
		disconnection.addActionListener(new ActionListener() {
			/**
			 * Close the application
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				logger.log(Level.INFO, "Application closed, after disconnection");
				System.exit(ABORT);

			}
		});

		/**
		 * Definition of the panel Search
		 */
		search = new JPanel();
		search.setBackground(Color.PINK);
		search.setBorder(BorderFactory.createMatteBorder(0, 25, 0, 25, bar.getBackground()));
		bar.add(search);

		/**
		 * Definition of the label search and add on panel search
		 */
		labelSearch = new JLabel();
		labelSearch.setText("Recherche : ");
		labelSearch.setFont(policeBar);
		labelSearch.setForeground(Color.WHITE);
		search.add(labelSearch);

		/**
		 * Definition of the textField seachBar and add panel search
		 */
		searchBar = new JTextField();
		searchBar.setPreferredSize(new Dimension(350, 30));
		search.add(searchBar);

		/**
		 * Definition of the ValidButton
		 */
		validButton = new JButton();
		validButton.setText("Valider");
		search.add(validButton);
		validButton.addActionListener(new ActionListener() {
			/**
			 * Verify the content of the search if they match with just numerics they will
			 * send a request to search with the id written in the research the IdSensor or
			 * the IdCommonArea of the Sensor. And add all the results on a list to display
			 * But if there is letter and numerics they will send a request to return all
			 * the Sensor when the type of Sensor contains the research
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				sensor2 = new Sensor();
				String searchReceived = searchBar.getText().trim();
				if (!searchReceived.equals("")) {
					/**
					 * If the research is just numerics they find first the IdSensor
					 */
					if (searchReceived.matches("[0-9]+[0-9]*")) {
						requestType = "READ";
						sensor2 = new Sensor();
						table = "Sensor";
						sensor2.setIdSensor(Integer.parseInt(searchReceived));
						try {
							jsonString = objectMapper.writeValueAsString(sensor2);
							new ClientSocket(requestType, jsonString, table);
							jsonString = ClientSocket.getJson();
							sensor2 = objectMapper.readValue(jsonString, Sensor.class);
							logger.log(Level.INFO, "Find Sensor data succed");
						} catch (Exception e1) {
							logger.log(Level.INFO,
									"Impossible to parse in JSON Sensor datas " + e1.getClass().getCanonicalName());
						}
						listM.removeAllElements();
						if (sensor2.getTypeSensor() != null) {
							listM.addElement("Résultat pour un capteur avec l'id : " + searchReceived);
							listM.addElement(sensor2.getIdSensor() + "# " + sensor2.getTypeSensor() + " ,"
									+ sensor2.getSensorState() + " ," + sensor2.getIdCommonArea());
						}
						/**
						 * Find Sensor with IdCommonArea
						 */
						searchReceived = Normalizer.normalize(searchReceived, Normalizer.Form.NFD)
								.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
						sensor2.setIdCommonArea(Integer.parseInt(searchReceived));
						requestType = "FIND ALL";
						table = "Sensor";
						objectMapper = new ObjectMapper();
						try {
							jsonString = objectMapper.writeValueAsString(sensor2);
							new ClientSocket(requestType, jsonString, table);
							jsonString = ClientSocket.getJson();
							Sensor[] sensors = objectMapper.readValue(jsonString, Sensor[].class);
							listSearchSensor = Arrays.asList(sensors);
							logger.log(Level.INFO, "Find Sensor data succed");
						} catch (Exception e1) {
							logger.log(Level.INFO,
									"Impossible to parse in JSON Sensor data" + e1.getClass().getCanonicalName());
						}
						if (listSearchSensor.size() > 0)
							listM.addElement("Résultat capteurs avec l'id partie commune  : " + searchReceived);
						for (Sensor sensors : listSearchSensor) {
							listM.addElement(sensors.getIdSensor() + "# " + sensors.getTypeSensor() + " ,"
									+ sensors.getSensorState() + " ," + sensors.getIdCommonArea());
						}
					} else {
						/**
						 * If the research contains letter and numerics we match with the types of
						 * Sensor
						 */
						sensor2 = new Sensor();
						if (searchReceived.toUpperCase().equals("S") || searchReceived.toUpperCase().equals("SM")
								|| searchReceived.toUpperCase().equals("SMO")
								|| searchReceived.toUpperCase().equals("SMOK")
								|| searchReceived.toUpperCase().equals("SMOKE")) {
							sensor2.setTypeSensor(SensorType.SMOKE);
						} else if (searchReceived.toUpperCase().equals("M") || searchReceived.toUpperCase().equals("MO")
								|| searchReceived.toUpperCase().equals("MOV")
								|| searchReceived.toUpperCase().equals("MOVE")) {
							sensor2.setTypeSensor(SensorType.MOVE);
						} else if (searchReceived.toUpperCase().equals("T") || searchReceived.toUpperCase().equals("TE")
								|| searchReceived.toUpperCase().equals("TEM")
								|| searchReceived.toUpperCase().equals("TEMP")
								|| searchReceived.toUpperCase().equals("TEMPE")
								|| searchReceived.toUpperCase().equals("TEMPER")
								|| searchReceived.toUpperCase().equals("TEMPERA")
								|| searchReceived.toUpperCase().equals("TEMPERAT")
								|| searchReceived.toUpperCase().equals("TEMPERATU")
								|| searchReceived.toUpperCase().equals("TEMPERATUR")
								|| searchReceived.toUpperCase().equals("TEMPERATURE")) {
							sensor2.setTypeSensor(SensorType.TEMPERATURE);
						} else if (searchReceived.toUpperCase().equals("W") || searchReceived.toUpperCase().equals("WI")
								|| searchReceived.toUpperCase().equals("WIN")
								|| searchReceived.toUpperCase().equals("WIND")
								|| searchReceived.toUpperCase().equals("WINDO")
								|| searchReceived.toUpperCase().equals("WINDOW")) {
							sensor2.setTypeSensor(SensorType.WINDOW);
						} else if (searchReceived.toUpperCase().equals("D") || searchReceived.toUpperCase().equals("DO")
								|| searchReceived.toUpperCase().equals("DOO")
										| searchReceived.toUpperCase().equals("DOOR")) {
							sensor2.setTypeSensor(SensorType.DOOR);
						} else if (searchReceived.toUpperCase().equals("E") || searchReceived.toUpperCase().equals("EL")
								|| searchReceived.toUpperCase().equals("ELE")
								|| searchReceived.toUpperCase().equals("ELEV")
								|| searchReceived.toUpperCase().equals("ELEVA")
								|| searchReceived.toUpperCase().equals("ELEVAT")
								|| searchReceived.toUpperCase().equals("ELEVATO")
								|| searchReceived.toUpperCase().equals("ELEVATOR")) {
							sensor2.setTypeSensor(SensorType.ELEVATOR);
						} else if (searchReceived.toUpperCase().equals("L") || searchReceived.toUpperCase().equals("LI")
								|| searchReceived.toUpperCase().equals("LIG")
								|| searchReceived.toUpperCase().equals("LIGH")
								|| searchReceived.toUpperCase().equals("LIGHT")) {
							sensor2.setTypeSensor(SensorType.LIGHT);
						} else if (searchReceived.toUpperCase().equals("F") || searchReceived.toUpperCase().equals("FI")
								|| searchReceived.toUpperCase().equals("FIR")
								|| searchReceived.toUpperCase().equals("FIRE")) {
							sensor2.setTypeSensor(SensorType.FIRE);
						} else if (searchReceived.toUpperCase().equals("B") || searchReceived.toUpperCase().equals("BA")
								|| searchReceived.toUpperCase().equals("BAD")
								|| searchReceived.toUpperCase().equals("BADG")
								|| searchReceived.toUpperCase().equals("BADGE")) {
							sensor2.setTypeSensor(SensorType.BADGE);
						} else if (searchReceived.toUpperCase().equals("R") || searchReceived.toUpperCase().equals("RO")
								|| searchReceived.toUpperCase().equals("ROU")
								|| searchReceived.toUpperCase().equals("ROUT")
								|| searchReceived.toUpperCase().equals("ROUTE")
								|| searchReceived.toUpperCase().equals("ROUTER")) {
							sensor2.setTypeSensor(SensorType.ROUTER);
						}
						requestType = "FIND ALL";
						table = "Sensor";
						objectMapper = new ObjectMapper();
						try {
							jsonString = objectMapper.writeValueAsString(sensor2);
							new ClientSocket(requestType, jsonString, table);
							jsonString = ClientSocket.getJson();
							Sensor[] sensors = objectMapper.readValue(jsonString, Sensor[].class);
							listSearchSensor = Arrays.asList(sensors);
							logger.log(Level.INFO, "Find Sensor data succed");
						} catch (Exception e1) {
							logger.log(Level.INFO,
									"Impossible to parse in JSON Sensor datas" + e1.getClass().getCanonicalName());
						}
						listM.removeAllElements();
						if (listSearchSensor.size() > 0)
							listM.addElement("Résultat pour le type de capteur : " + searchReceived);
						for (Sensor sensors : listSearchSensor) {
							listM.addElement(sensors.getIdSensor() + "# " + sensors.getTypeSensor() + " ,"
									+ sensors.getSensorState() + " ," + sensors.getIdCommonArea());
						}
					}
				} else {
					/**
					 * If the research is empty they display all the Sensors
					 */
					requestType = "READ ALL";
					table = "Sensor";
					objectMapper = new ObjectMapper();
					try {
						jsonString = "READ ALL";
						new ClientSocket(requestType, jsonString, table);
						jsonString = ClientSocket.getJson();
						Sensor[] sensors = objectMapper.readValue(jsonString, Sensor[].class);
						listSensor = Arrays.asList(sensors);
						logger.log(Level.INFO, "Find Sensor datas succed");
					} catch (Exception e1) {
						logger.log(Level.INFO,
								"Impossible to parse in JSON Sensor data " + e1.getClass().getCanonicalName());
					}
					listM.removeAllElements();
					listM.addElement("Tout les capteurs");
					for (Sensor sensors : listSensor) {
						listM.addElement(sensors.getIdSensor() + "# " + sensors.getTypeSensor() + " ,"
								+ sensors.getSensorState() + " ," + sensors.getIdCommonArea());
					}
				}
				searchBar.setText("");
			}
		});

		/**
		 * Definition of the List CommonArea
		 */
		textInputNameCommonArea = new JComboBox<String>();

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
				;
				new ClientSocket(requestType, jsonString, table);
				jsonString = ClientSocket.getJson();
				sensor = readMapper.readValue(jsonString, Sensor.class);
				logger.log(Level.INFO, "Find Sensor datas succed");
			} catch (Exception e1) {
				logger.log(Level.INFO, "Impossible to parse in JSON Sensor datas" + e1.getClass().getCanonicalName());
			}

		}

		///////////////////////// LIST SENSOR///////////////////////////////////////////
		listM = new DefaultListModel<String>();
		list = new JList<String>(listM);
		updateListSensor();

		sc = new JScrollPane(list);
		sc.setBounds(30, 120, 300, ((int) getToolkit().getScreenSize().getHeight() - 300));
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
						;
						new ClientSocket(requestType, jsonString, table);
						jsonString = ClientSocket.getJson();
						sensor = objectMapper.readValue(jsonString, Sensor.class);
					} catch (Exception e1) {
						logger.log(Level.INFO, "Impossible to parse in JSON " + e1.getClass().getCanonicalName());
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
						;
						new ClientSocket(requestType, jsonString, table);
						jsonString = ClientSocket.getJson();
						commonArea = readMapper.readValue(jsonString, CommonArea.class);
					} catch (Exception e1) {
						logger.log(Level.INFO, "Impossible to parse in JSON " + e1.getClass().getCanonicalName());
					}

					textInputIdSensor.setText(Integer.toString(sensor.getIdSensor()));
					String str = commonArea.getNameCommonArea() + " #" + sensor.getIdCommonArea();
					for (int i = 0; i < textInputNameCommonArea.getItemCount(); i++) {
						if (textInputNameCommonArea.getItemAt(i).toString().contains(str)) {
							textInputNameCommonArea.setSelectedIndex(i);
						}
					}
					textInputTypeSensor.setSelectedItem(sensor.getTypeSensor().toString());
					if (sensor.getSensorState() == true) {
						switchButton.setText("ON");
						switchButton.setBackground(Color.GREEN);
					} else {
						switchButton.setText("OFF");
						switchButton.setBackground(Color.RED);
					}
				}
			}
		};
		list.addMouseListener(mouseListener);

		/**
		 * Call the method define at end to update the list of CommonAreas availables
		 * they will be execute one time at creation of the window
		 */
		updateListAreas();

		///////////////////////// LABEL/////////////////////////////////////////////////
		/**
		 * Definition of label IdSensor
		 */
		policeLabel = new Font("Arial", Font.BOLD, (int) getToolkit().getScreenSize().getWidth() / 80);
		labelIdSensor = new JLabel("Id : ");
		labelIdSensor.setBounds((int) getToolkit().getScreenSize().getWidth() * 2 / 7,
				(int) getToolkit().getScreenSize().getHeight() * 2 / 10, 200, 30);
		labelIdSensor.setFont(policeLabel);
		this.add(labelIdSensor);

		/**
		 * Definition of label NameCommonArea
		 */
		labelNameCommonArea = new JLabel("Nom de la Partie Commune : ");
		labelNameCommonArea.setBounds((int) getToolkit().getScreenSize().getWidth() * 4 / 7,
				(int) getToolkit().getScreenSize().getHeight() * 2 / 10, 250, 30);
		labelNameCommonArea.setFont(policeLabel);
		this.add(labelNameCommonArea);

		/**
		 * Definition of label TypeSensor
		 */
		labelTypeSensor = new JLabel("Type Capteur : ");
		labelTypeSensor.setBounds((int) getToolkit().getScreenSize().getWidth() * 2 / 7,
				(int) getToolkit().getScreenSize().getHeight() * 4 / 10, 200, 30);
		labelTypeSensor.setFont(policeLabel);
		this.add(labelTypeSensor);

		/**
		 * Definition of label TypeSensor
		 */
		labelNumber = new JLabel("le resultat");
		labelNumber.setBounds((int) getToolkit().getScreenSize().getWidth() * 6 / 7,
				(int) getToolkit().getScreenSize().getHeight() * 4 / 10, 200, 30);
		labelNumber.setFont(policeLabel);
		labelNumber.setText(returnNumber().toString());
		this.add(labelNumber);

		/**
		 * Definition of label StateSensor
		 */
		labelStateSensor = new JLabel("Etat du Capteur : ");
		labelStateSensor.setBounds((int) getToolkit().getScreenSize().getWidth() * 4 / 7,
				(int) getToolkit().getScreenSize().getHeight() * 4 / 10, 200, 30);
		labelStateSensor.setFont(policeLabel);
		this.add(labelStateSensor);

		/**
		 * Definition of label HeadList
		 */
		labelHeadList = new JLabel("ID /Type /Etat /ID Partie Commune");
		labelHeadList.setBounds(40, 90, 200, 30);
		labelHeadList.setFont(policeBar);
		this.add(labelHeadList);
		//////////////////// TEXT AREA////////////////////////////////////////////////
		/**
		 * Definition of textArea IdSensor
		 */
		textInputIdSensor = new JTextField();
		textInputIdSensor.setBounds((int) getToolkit().getScreenSize().getWidth() * 2 / 7,
				(int) getToolkit().getScreenSize().getHeight() * 5 / 20, 300, 40);
		textInputIdSensor.setFont(policeLabel);
		if (idSensor == 0)
			textInputIdSensor.setText("");
		else
			textInputIdSensor.setText(Integer.toString(idSensor));
		textInputIdSensor.setEditable(false);
		this.add(textInputIdSensor);

		/**
		 * Definition of textArea NameCommonArea
		 */
		textInputNameCommonArea.setBounds((int) getToolkit().getScreenSize().getWidth() * 4 / 7,
				(int) getToolkit().getScreenSize().getHeight() * 5 / 20, 300, 40);
		textInputNameCommonArea.setFont(policeLabel);
		this.add(textInputNameCommonArea);

		/**
		 * Definition of textArea TypeSensor
		 */
		String[] types = { "SMOKE", "MOVE", "TEMPERATURE", "WINDOW", "DOOR", "ELEVATOR", "LIGHT", "FIRE", "BADGE",
				"ROUTER" };
		textInputTypeSensor = new JComboBox<String>(types);
		textInputTypeSensor.setBounds((int) getToolkit().getScreenSize().getWidth() * 2 / 7,
				(int) getToolkit().getScreenSize().getHeight() * 9 / 20, 300, 40);
		textInputTypeSensor.setFont(policeLabel);
		this.add(textInputTypeSensor);
		textInputTypeSensor.addItemListener(new ItemListener() {
			/**
			 * All type of Sensor
			 */
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getItem().toString().equals("SMOKE"))
					sensor.setTypeSensor(SensorType.SMOKE);
				else if (e.getItem().toString().equals("MOVE"))
					sensor.setTypeSensor(SensorType.MOVE);
				else if (e.getItem().toString().equals("TEMPERATURE"))
					sensor.setTypeSensor(SensorType.TEMPERATURE);
				else if (e.getItem().toString().equals("WINDOW"))
					sensor.setTypeSensor(SensorType.WINDOW);
				else if (e.getItem().toString().equals("DOOR"))
					sensor.setTypeSensor(SensorType.DOOR);
				else if (e.getItem().toString().equals("ELEVATOR"))
					sensor.setTypeSensor(SensorType.ELEVATOR);
				else if (e.getItem().toString().equals("LIGHT"))
					sensor.setTypeSensor(SensorType.LIGHT);
				else if (e.getItem().toString().equals("FIRE"))
					sensor.setTypeSensor(SensorType.FIRE);
				else if (e.getItem().toString().equals("BADGE"))
					sensor.setTypeSensor(SensorType.BADGE);
			}
		});

		/**
		 * Definition of textArea StateSensor
		 */
		switchButton = new JButton();
		switchButton.setBounds((int) getToolkit().getScreenSize().getWidth() * 4 / 7,
				(int) getToolkit().getScreenSize().getHeight() * 9 / 20, 100, 40);
		switchButton.setText("OFF");
		switchButton.setBackground(Color.RED);
		switchButton.setFont(policeLabel);
		this.add(switchButton);
		switchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				if (switchButton.getText().equals("ON")) {
					switchButton.setText("OFF");
					switchButton.setBackground(Color.RED);
				} else if (switchButton.getText().equals("OFF")) {
					switchButton.setText("ON");
					switchButton.setBackground(Color.GREEN);
				}
			}
		});

		if (idSensor != 0 && index == -9999) {
			/**
			 * Find the Sensor by the id get on list
			 */
			requestType = "READ";
			sensor = new Sensor();
			table = "Sensor";
			sensor.setIdSensor(idSensor);
			try {
				jsonString = objectMapper.writeValueAsString(sensor);
				;
				new ClientSocket(requestType, jsonString, table);
				jsonString = ClientSocket.getJson();
				sensor = objectMapper.readValue(jsonString, Sensor.class);
			} catch (Exception e1) {
				logger.log(Level.INFO, "Impossible to parse in JSON " + e1.getClass().getCanonicalName());
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
			} catch (Exception e1) {
				logger.log(Level.INFO, "Impossible to parse in JSON " + e1.getClass().getCanonicalName());
			}
			textInputTypeSensor.setSelectedItem(sensor.getTypeSensor().toString());
			if (sensor.getSensorState() == true) {
				switchButton.setText("ON");
				switchButton.setBackground(Color.GREEN);
			} else {
				switchButton.setText("OFF");
				switchButton.setBackground(Color.RED);
			}
		}
		///////////////////////// BUTTON/////////////////////////////////////////////////
		/**
		 * Definition of Button AddSensor
		 */
		addSensor = new JButton("Ajouter");
		addSensor.setBounds(30, (int) getToolkit().getScreenSize().getHeight() - 150, 300, 40);
		this.add(addSensor);
		addSensor.addActionListener(new ActionListener() {
			/**
			 * When we add an sensor, we get the commonArea, the state and the type of the
			 * sensor from the textFields and the item selected by the user
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				requestType = "CREATE";
				table = "Sensor";

				if (textInputIdSensor.getText().toString().equals("")) {
					sensor.setIdSensor(0);
				} else {
					sensor.setIdSensor(Integer.parseInt(textInputIdSensor.getText().toString()));
				}
				String newNameCommonArea = textInputNameCommonArea.getSelectedItem().toString();
				int position = newNameCommonArea.indexOf("#");
				String id = newNameCommonArea.substring(position + 1, newNameCommonArea.length());
				sensor.setIdCommonArea(Integer.parseInt(id));

				String newStateSensor = switchButton.getText();
				if (newStateSensor.equals("ON"))
					sensor.setSensorState(true);
				else
					sensor.setSensorState(false);

				/**
				 * Read the sensor type selected
				 */
				String newTypeSensor = textInputTypeSensor.getSelectedItem().toString();
				if (newTypeSensor.equals("SMOKE"))
					sensor.setTypeSensor(SensorType.SMOKE);
				else if (newTypeSensor.equals("MOVE"))
					sensor.setTypeSensor(SensorType.MOVE);
				else if (newTypeSensor.equals("TEMPERATURE"))
					sensor.setTypeSensor(SensorType.TEMPERATURE);
				else if (newTypeSensor.equals("WINDOW"))
					sensor.setTypeSensor(SensorType.WINDOW);
				else if (newTypeSensor.equals("DOOR"))
					sensor.setTypeSensor(SensorType.DOOR);
				else if (newTypeSensor.equals("ELEVATOR"))
					sensor.setTypeSensor(SensorType.ELEVATOR);
				else if (newTypeSensor.equals("LIGHT"))
					sensor.setTypeSensor(SensorType.LIGHT);
				else if (newTypeSensor.equals("FIRE"))
					sensor.setTypeSensor(SensorType.FIRE);
				else if (newTypeSensor.equals("BADGE"))
					sensor.setTypeSensor(SensorType.BADGE);

				ObjectMapper insertMapper = new ObjectMapper();
				try {
					jsonString = insertMapper.writeValueAsString(sensor);
					new ClientSocket(requestType, jsonString, table);
					jsonString = ClientSocket.getJson();
					if (!jsonString.equals("INSERTED")) {
						JOptionPane.showMessageDialog(null, "L'ajout a échoué", "Erreur", JOptionPane.ERROR_MESSAGE);
						logger.log(Level.INFO, "Impossible to insert sensor");
					} else {
						logger.log(Level.INFO, "Insertion Succeded");
						requestType = "READ ALL";
						table = "Sensor";
						jsonString = "READ ALL";
						new ClientSocket(requestType, jsonString, table);
						jsonString = ClientSocket.getJson();
						Sensor[] sensors = objectMapper.readValue(jsonString, Sensor[].class);
						listSensor = Arrays.asList(sensors);
						int x = listSensor.size() - 1;
						sensor = listSensor.get(x);
						listM.addElement(sensor.getIdSensor() + "# " + sensor.getTypeSensor() + " ,"
								+ sensor.getSensorState() + " ," + sensor.getIdCommonArea());
						logger.log(Level.INFO, "Find Sensor succeded");
						JOptionPane.showMessageDialog(null, "Linsertion à été effectuer", "Infos",
								JOptionPane.INFORMATION_MESSAGE);
					}
				} catch (Exception e1) {
					logger.log(Level.INFO,
							"Impossible to parse in JSON Sensor Datas" + e1.getClass().getCanonicalName());
				}
			}
		});

		/**
		 * Definition of Button Save
		 */
		save = new JButton("Sauvegarder");
		save.setBounds(((int) getToolkit().getScreenSize().getWidth() * 2 / 4) + 250,
				(int) getToolkit().getScreenSize().getHeight() * 15 / 20, 200, 40);
		this.add(save);
		save.addActionListener(new ActionListener() {
			/**
			 * Get the selected commonArea ,the state and the type of sensor and send to
			 * server the request
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				requestType = "UPDATE";
				table = "Sensor";

				if (textInputIdSensor.getText().toString().equals("")) {
					sensor.setIdSensor(0);
				} else {
					sensor.setIdSensor(Integer.parseInt(textInputIdSensor.getText().toString()));
				}
				String newNameCommonArea = textInputNameCommonArea.getSelectedItem().toString();
				int position = newNameCommonArea.indexOf("#");
				String id = newNameCommonArea.substring(position + 1, newNameCommonArea.length());
				sensor.setIdCommonArea(Integer.parseInt(id));

				/**
				 * Get the state of sensor
				 */
				String newStateSensor = switchButton.getText();
				if (newStateSensor.equals("ON"))
					sensor.setSensorState(true);
				else
					sensor.setSensorState(false);

				/**
				 * Select the type of sensor
				 */
				String newTypeSensor = textInputTypeSensor.getSelectedItem().toString();
				if (newTypeSensor.equals("SMOKE"))
					sensor.setTypeSensor(SensorType.SMOKE);
				else if (newTypeSensor.equals("MOVE"))
					sensor.setTypeSensor(SensorType.MOVE);
				else if (newTypeSensor.equals("TEMPERATURE"))
					sensor.setTypeSensor(SensorType.TEMPERATURE);
				else if (newTypeSensor.equals("WINDOW"))
					sensor.setTypeSensor(SensorType.WINDOW);
				else if (newTypeSensor.equals("DOOR"))
					sensor.setTypeSensor(SensorType.DOOR);
				else if (newTypeSensor.equals("ELEVATOR"))
					sensor.setTypeSensor(SensorType.ELEVATOR);
				else if (newTypeSensor.equals("LIGHT"))
					sensor.setTypeSensor(SensorType.LIGHT);
				else if (newTypeSensor.equals("FIRE"))
					sensor.setTypeSensor(SensorType.FIRE);
				else if (newTypeSensor.equals("BADGE"))
					sensor.setTypeSensor(SensorType.BADGE);

				if (sensor.getIdSensor() == 0) {
					JOptionPane.showMessageDialog(null, "Veuillez selectionner un capteur à mettre a jour", "Erreur",
							JOptionPane.ERROR_MESSAGE);
				} else {
					ObjectMapper insertMapper = new ObjectMapper();
					try {
						jsonString = insertMapper.writeValueAsString(sensor);
						new ClientSocket(requestType, jsonString, table);
						jsonString = ClientSocket.getJson();
						if (!jsonString.equals("UPDATED")) {
							JOptionPane.showMessageDialog(null, "La Mise a jour a échoué", "Erreur",
									JOptionPane.ERROR_MESSAGE);
							logger.log(Level.INFO, "Impossible to update sensor");
						} else {
							logger.log(Level.INFO, "Update Succeded");
							listM.set(index, sensor.getIdSensor() + "# " + sensor.getTypeSensor() + " ,"
									+ sensor.getSensorState() + " ," + sensor.getIdCommonArea());
							JOptionPane.showMessageDialog(null, "Données Mises à jours", "Infos",
									JOptionPane.INFORMATION_MESSAGE);
						}
					} catch (Exception e1) {
						logger.log(Level.INFO,
								"Impossible to parse in JSON sensor datas" + e1.getClass().getCanonicalName());
					}
				}
			}
		});

		/**
		 * Definition of Button Restaure
		 */
		restaure = new JButton("Annuler");
		restaure.setBounds(((int) getToolkit().getScreenSize().getWidth() * 2 / 4),
				(int) getToolkit().getScreenSize().getHeight() * 15 / 20, 200, 40);
		this.add(restaure);
		restaure.addActionListener(new ActionListener() {
			/**
			 * when the button Restaure is pressed, we define the textAreas as the last
			 * sensor datas selected in the list by the user
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				if (sensor.getIdSensor() == 0) {
					textInputIdSensor.setText("");
				} else {
					textInputIdSensor.setText(Integer.toString(sensor.getIdSensor()));
				}
				textInputNameCommonArea.setSelectedItem(commonArea.getNameCommonArea());
				textInputTypeSensor.setSelectedItem(sensor.getTypeSensor().toString());
				if (sensor.getSensorState() == true) {
					switchButton.setText("ON");
					switchButton.setBackground(Color.GREEN);
				} else {
					switchButton.setText("OFF");
					switchButton.setBackground(Color.RED);
				}
			}
		});

		/**
		 * Definition of Button Delete
		 */
		delete = new JButton("Supprimer");
		delete.setBounds(((int) getToolkit().getScreenSize().getWidth() * 2 / 4) - 250,
				(int) getToolkit().getScreenSize().getHeight() * 15 / 20, 200, 40);
		this.add(delete);
		delete.addActionListener(new ActionListener() {
			/**
			 * When we pressed the button delete we get the id of the sensor and we send it
			 * to server, to be deleted by him
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!textInputIdSensor.getText().toString().equals("")) {
					requestType = "DELETE";
					table = "Sensor";
					ObjectMapper connectionMapper = new ObjectMapper();
					try {
						jsonString = connectionMapper.writeValueAsString(sensor);
						new ClientSocket(requestType, jsonString, table);
						jsonString = ClientSocket.getJson();
						if (!jsonString.equals("DELETED")) {
							JOptionPane.showMessageDialog(null, "La suppression a échoué", "Erreur",
									JOptionPane.ERROR_MESSAGE);
							logger.log(Level.INFO, "Impossible to delete this sensor");
						} else {
							JOptionPane.showMessageDialog(null, "Suppression du capteur", "Infos",
									JOptionPane.INFORMATION_MESSAGE);
							logger.log(Level.INFO, "Deletion of sensor succed");
						}
					} catch (Exception e1) {
						logger.log(Level.INFO,
								"Impossible to parse in JSON Sensor datas" + e1.getClass().getCanonicalName());
					}
					sensor.setIdCommonArea(0);
					sensor.setIdSensor(0);

					listM.removeElementAt(index);
					index = -9999;
					textInputIdSensor.setText("");
					switchButton.setText("OFF");
					switchButton.setBackground(Color.RED);
				} else {
					JOptionPane.showMessageDialog(null, "Veuillez selectionner un capteur à supprimer", "Erreur",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		threadSensor.start();

		///////////////////////// FRAME/////////////////////////////////////////////////
		/**
		 * Diferent parameter of the window
		 */
		this.setLayout(new BorderLayout());
		this.add(bar, BorderLayout.NORTH);
		this.setBackground(color);
	}

	///////////////////////// LIST COMMON AREA//////////////////////////////////////
	/**
	 * updateListAreas get all the CommonAreas available in the data base and will
	 * transform that in a list
	 */
	public void updateListAreas() {
		requestType = "READ ALL";
		table = "CommonArea";
		objectMapper = new ObjectMapper();
		try {
			jsonString = "READ ALL";
			new ClientSocket(requestType, jsonString, table);
			jsonString = ClientSocket.getJson();
			CommonArea[] commonAreas = objectMapper.readValue(jsonString, CommonArea[].class);
			listCommonArea = Arrays.asList(commonAreas);
			logger.log(Level.INFO, "Recuperation of all Common Areas available succed");
		} catch (Exception e1) {
			logger.log(Level.INFO, "Impossible to parse in JSON Common Area datas " + e1.getClass().getCanonicalName());
		}
		String areasAdd = "";
		textInputNameCommonArea.removeAllItems();
		for (CommonArea commonAreas : listCommonArea) {
			if (!areasAdd.contains(commonAreas.getNameCommonArea() + " #" + commonAreas.getIdCommonArea()))
				textInputNameCommonArea.addItem(commonAreas.getNameCommonArea() + " #" + commonAreas.getIdCommonArea());
			areasAdd = areasAdd + commonAreas.getNameCommonArea() + " #" + commonAreas.getIdCommonArea() + ",";
		}
		logger.log(Level.INFO, "Convertion of all Common Areas available in a list succed");
	}

	public void updateListSensor() {
		sensor = new Sensor();
		sensor.setIdCommonArea(0);
		sensor.setSensorState(false);
		sensor.setTypeSensor(null);

		/**
		 * Find all the Sensor in the data base and add on list to be displayed
		 */
		requestType = "READ ALL";
		table = "Sensor";
		objectMapper = new ObjectMapper();
		try {
			jsonString = "READ ALL";
			new ClientSocket(requestType, jsonString, table);
			jsonString = ClientSocket.getJson();
			Sensor[] sensors = objectMapper.readValue(jsonString, Sensor[].class);
			listSensor = Arrays.asList(sensors);
			logger.log(Level.INFO, "Find Sensor data succed");

		} catch (Exception e1) {
			logger.log(Level.INFO, "Impossible to parse in JSON Sensor data " + e1.getClass().getCanonicalName());
		}
		listM.removeAllElements();
		for (Sensor sensors : listSensor) {
			listM.addElement(sensors.getIdSensor() + "# " + sensors.getTypeSensor() + " ," + sensors.getSensorState()
					+ " ," + sensors.getIdCommonArea());
		}
		if (listM.isEmpty() && (!listSensor.isEmpty())) {
			updateListSensor();
		}

	}

	public void updateSensorSelected() {
		if (index != -9999) {
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
				} catch (Exception e1) {
					logger.log(Level.INFO, "Impossible to parse in JSON " + e1.getClass().getCanonicalName());
				}

				textInputIdSensor.setText(Integer.toString(sensor.getIdSensor()));
				String str = commonArea.getNameCommonArea() + " #" + sensor.getIdCommonArea();
				for (int i = 0; i < textInputNameCommonArea.getItemCount(); i++) {
					if (textInputNameCommonArea.getItemAt(i).toString().contains(str)) {
						textInputNameCommonArea.setSelectedIndex(i);
					}
				}
				textInputTypeSensor.setSelectedItem(sensor.getTypeSensor().toString());
				if (sensor.getSensorState() == true) {
					switchButton.setText("ON");
					switchButton.setBackground(Color.GREEN);
				} else {
					switchButton.setText("OFF");
					switchButton.setBackground(Color.RED);
				}
			}
		}
	}

	public Thread getThreadSensor() {
		return threadSensor;
	}

	public void setThreadSensor(Thread threadSensor) {
		this.threadSensor = threadSensor;
	}

	// SLAYDE
	public String returnNumber() {
		int nb = 0;
		if (!listSensor.isEmpty()) {
			for (Sensor count : listSensor) {
				if (count.getTypeSensor() == SensorType.TEMPERATURE) {
					nb++;
				}
			}

		}
		return String.valueOf(nb);

	}
}
