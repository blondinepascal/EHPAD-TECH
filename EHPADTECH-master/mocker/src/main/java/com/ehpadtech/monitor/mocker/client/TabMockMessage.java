package com.ehpadtech.monitor.mocker.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ehpadtech.monitor.commons.entity.Message;
import com.ehpadtech.monitor.commons.entity.Sensor;
import com.ehpadtech.monitor.commons.entity.SensorType;
import com.ehpadtech.monitor.mocker.generator.GenerateMessage;
import com.ehpadtech.monitor.mocker.launcher.MockSocket;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TabMockMessage extends JPanel {

	/**
	 * Different parameters used
	 */
	private static final long serialVersionUID = 1L;
	private int nbThread;
	private int threshold;
	private boolean bool;
	private String requestType;
	private String table;
	private String jsonString;
	private String request;
	private Font policeLabel;
	private JLabel labelIdSensor;
	private JLabel labelThreshold;
	private JLabel labelSensorType;
	private JLabel labelRandomSensor;
	private JTextField textInputIdSensor;
	private JSlider sliderThreshold;
	private ButtonGroup buttonGroup;
	private JRadioButton allCaseRadio;
	private JRadioButton oneCaseRadio;
	private JRadioButton typeSensorCaseRadio;
	private JButton generateButton;
	private JButton restoreButton;
	private Thread threadGenerateMessage;
	private Message message;
	private ObjectMapper objectMapper;
	private JComboBox<String> textInputTypeSensor;
	private List<Sensor> listSensor = new ArrayList<Sensor>();
	private List<Sensor> listRestrictedSensor = new ArrayList<Sensor>();
	private static final Logger logger = LogManager.getLogger(TabMockMessage.class);
	private GenerateMessage generateMessage;

	/**
	 * Constructor
	 */
	public TabMockMessage() {
	}

	/**
	 * Constructor
	 * 
	 * @param color
	 * @param title
	 */
	public TabMockMessage(Color color, String title) {

		///////////////////////// LABEL///////////////////////////////////////////////
		/**
		 * Definition of label IdSensor
		 */
		policeLabel = new Font("Arial", Font.BOLD, 20);
		labelIdSensor = new JLabel("ID sensor : ");
		labelIdSensor.setBounds(100, 100, 200, 30);
		labelIdSensor.setFont(policeLabel);
		this.add(labelIdSensor);

		/**
		 * Definition of label Threshold
		 */
		labelThreshold = new JLabel("Threshold : ");
		labelThreshold.setBounds(100, 200, 200, 30);
		labelThreshold.setFont(policeLabel);
		this.add(labelThreshold);

		/**
		 * Definition of label SensorType
		 */
		labelSensorType = new JLabel("Sensor type : ");
		labelSensorType.setBounds(400, 200, 200, 30);
		labelSensorType.setFont(policeLabel);
		this.add(labelSensorType);

		/**
		 * Definition of label RandomSensor
		 */
		labelRandomSensor = new JLabel("Wich sensor generate : ");
		labelRandomSensor.setBounds(400, 100, 300, 30);
		labelRandomSensor.setFont(policeLabel);
		this.add(labelRandomSensor);

		//////////////////// TEXT AREA////////////////////////////////////////////////
		/**
		 * Definition of textArea IdSensor
		 */
		textInputIdSensor = new JTextField();
		textInputIdSensor.setBounds(100, 150, 250, 30);
		textInputIdSensor.setFont(policeLabel);
		textInputIdSensor.setText("");
		this.add(textInputIdSensor);

		/**
		 * Definition of Slider Threshold
		 */
		sliderThreshold = new JSlider();
		sliderThreshold.setPaintTicks(true);
		sliderThreshold.setPaintLabels(true);
		sliderThreshold.setMaximum(100);
		sliderThreshold.setMinimum(0);
		sliderThreshold.setValue(30);
		sliderThreshold.setMinorTickSpacing(5);
		sliderThreshold.setMajorTickSpacing(10);

		sliderThreshold.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent event) {
				if (textInputTypeSensor.getSelectedIndex() != -1) {
					SensorType element = SensorType.valueOf(textInputTypeSensor.getSelectedItem().toString());
					switch (element) {
					case ELEVATOR:
						labelThreshold.setText("Threshold : " + ((JSlider) event.getSource()).getValue() + "00kg");
						threshold = ((JSlider) event.getSource()).getValue() * 100;
						if (generateMessage != null) {
							generateMessage.setThreshold(((JSlider) event.getSource()).getValue() * 100);
						}
						break;
					case SMOKE:
						labelThreshold.setText("Threshold : " + ((JSlider) event.getSource()).getValue() + "0ppm");
						threshold = ((JSlider) event.getSource()).getValue() * 10;
						if (generateMessage != null) {
							generateMessage.setThreshold(((JSlider) event.getSource()).getValue() * 10);
						}
						break;
					default:
						labelThreshold.setText("Threshold : " + ((JSlider) event.getSource()).getValue());
						threshold = ((JSlider) event.getSource()).getValue();
						if (generateMessage != null) {
							generateMessage.setThreshold(((JSlider) event.getSource()).getValue());
						}
					}
				}
			}
		});

		sliderThreshold.setBackground(color);
		sliderThreshold.setBounds(100, 250, 250, 50);
		this.add(sliderThreshold);

		/**
		 * Definition of ButtonGroup RandomSensor
		 */
		allCaseRadio = new JRadioButton("All");
		allCaseRadio.setBounds(400, 150, 100, 30);
		allCaseRadio.setFont(policeLabel);
		allCaseRadio.setBackground(color);

		oneCaseRadio = new JRadioButton("One");
		oneCaseRadio.setBounds(500, 150, 100, 30);
		oneCaseRadio.setFont(policeLabel);
		oneCaseRadio.setBackground(color);

		typeSensorCaseRadio = new JRadioButton("Sensor type");
		typeSensorCaseRadio.setBounds(600, 150, 250, 30);
		typeSensorCaseRadio.setFont(policeLabel);
		typeSensorCaseRadio.setBackground(color);

		buttonGroup = new ButtonGroup();
		buttonGroup.add(allCaseRadio);
		buttonGroup.add(oneCaseRadio);
		buttonGroup.add(typeSensorCaseRadio);

		this.add(allCaseRadio);
		this.add(oneCaseRadio);
		this.add(typeSensorCaseRadio);

		/**
		 * Definition of ComboBox TypeSensor
		 */
		String[] types = { SensorType.SMOKE.name(), SensorType.MOVE.name(), SensorType.TEMPERATURE.name(),
				SensorType.WINDOW.name(), SensorType.DOOR.name(), SensorType.ELEVATOR.name(), SensorType.LIGHT.name(),
				SensorType.FIRE.name(), SensorType.BADGE.name(), SensorType.ROUTER.name() };
		textInputTypeSensor = new JComboBox<String>(types);
		textInputTypeSensor.setBounds(400, 250, 250, 40);
		textInputTypeSensor.setFont(policeLabel);
		textInputTypeSensor.setSelectedItem(null);
		this.add(textInputTypeSensor);
		textInputTypeSensor.addItemListener(new ItemListener() {
			/**
			 * All type of Sensor
			 */
			@Override
			public void itemStateChanged(ItemEvent e) {
				SensorType element = SensorType.valueOf(e.getItem().toString());
				switch (element) {
				case SMOKE:
					sliderThreshold.setMaximum(50);
					sliderThreshold.setMinimum(0);
					sliderThreshold.setValue(10);
					sliderThreshold.setMinorTickSpacing(5);
					sliderThreshold.setMajorTickSpacing(10);
					break;
				case TEMPERATURE:
					sliderThreshold.setMaximum(50);
					sliderThreshold.setMinimum(-20);
					sliderThreshold.setValue(20);
					sliderThreshold.setMinorTickSpacing(5);
					sliderThreshold.setMajorTickSpacing(10);
					break;
				case ELEVATOR:
					sliderThreshold.setMaximum(8);
					sliderThreshold.setMinimum(0);
					sliderThreshold.setValue(4);
					sliderThreshold.setMinorTickSpacing(1);
					sliderThreshold.setMajorTickSpacing(2);
					break;
				default:
					sliderThreshold.setMaximum(1);
					sliderThreshold.setMinimum(0);
					sliderThreshold.setValue(0);
				}
			}
		});

		///////////////////////// BUTTON/////////////////////////////////////////////////
		/**
		 * Definition of Button AddSensor
		 */
		generateButton = new JButton("Generate");
		generateButton.setBounds(100, 350, 100, 40);
		this.add(generateButton);
		generateButton.addActionListener(new ActionListener() {
			@Override
			public synchronized void actionPerformed(ActionEvent e) {
				if (!allCaseRadio.isSelected() && !oneCaseRadio.isSelected() && !typeSensorCaseRadio.isSelected()) {
					JOptionPane.showMessageDialog(null, "Please select the form to generate", "Information",
							JOptionPane.INFORMATION_MESSAGE);
				} else {
					if (allCaseRadio.isSelected()) {
						getAllSensor();
						message = new Message();
						request = "ALL";
						launchGeneration();
					} else if (oneCaseRadio.isSelected()) {
						if (textInputIdSensor.getText().trim().equals("")) {
							JOptionPane.showMessageDialog(null, "ID sensor empty", "Information",
									JOptionPane.INFORMATION_MESSAGE);
						} else {
							if (textInputIdSensor.getText().trim().matches("[0-9]+[0-9]*")) {
								if (textInputTypeSensor.getSelectedItem() == null) {
									JOptionPane.showMessageDialog(null, "Please select the sensor type", "Information",
											JOptionPane.INFORMATION_MESSAGE);
								} else {
									getAllSensor();
									message = new Message();
									request = "ONE";
									for (Sensor sensors : listSensor) {
										if (sensors.getTypeSensor().name()
												.equals(textInputTypeSensor.getSelectedItem().toString())
												&& sensors.getIdSensor() == Integer
														.parseInt(textInputIdSensor.getText().trim())) {
											if (sensors.getSensorState()) {
												message.setIdSensor(sensors.getIdSensor());
											} else {
												JOptionPane.showMessageDialog(null, "This sensor is OFF", "Information",
														JOptionPane.INFORMATION_MESSAGE);
											}
										}
									}
									if (message.getIdSensor() == 0) {
										JOptionPane.showMessageDialog(null,
												"This sensor does'nt exist with this information", "Information",
												JOptionPane.INFORMATION_MESSAGE);
									}
									launchGeneration();
								}
							} else {
								JOptionPane.showMessageDialog(null, "Please insert an ID sensor", "Information",
										JOptionPane.INFORMATION_MESSAGE);
							}
						}
					} else if (typeSensorCaseRadio.isSelected()) {
						if (textInputTypeSensor.getSelectedItem() == null) {
							JOptionPane.showMessageDialog(null, "Please select the sensor type", "Information",
									JOptionPane.INFORMATION_MESSAGE);
						} else {
							getAllSensor();
							boolean boo = true;
							message = new Message();
							request = "TYPE";
							listRestrictedSensor.removeAll(listRestrictedSensor);
							for (Sensor sensors : listSensor) {
								if (sensors.getTypeSensor().name().equals(
										textInputTypeSensor.getSelectedItem().toString()) && sensors.getSensorState()) {
									listRestrictedSensor.add(sensors);
									if (boo) {
										message.setIdSensor(sensors.getIdSensor());
										boo = false;
									}
								}
							}
							for (Sensor sensors : listSensor) {
								if (!(sensors.getTypeSensor().name()
										.equals(textInputTypeSensor.getSelectedItem().toString())
										&& sensors.getSensorState())) {
									listRestrictedSensor.add(sensors);
								}
							}
							listSensor = listRestrictedSensor;
							launchGeneration();
						}
					}
				}
			}
		});

		/**
		 * Set to default values the different fields
		 */
		restoreButton = new JButton("Restore");
		restoreButton.setBounds(500, 350, 100, 40);
		this.add(restoreButton);
		restoreButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				textInputIdSensor.setText("");
				textInputTypeSensor.setSelectedItem(null);
				oneCaseRadio.setSelected(true);
				labelThreshold.setText("Threshold : ");
			}
		});

		/**
		 * Stop the generation of message with the boolean and restart the number of
		 * thread
		 */
		restoreButton = new JButton("Stop generation");
		restoreButton.setBounds(250, 350, 200, 40);
		this.add(restoreButton);
		restoreButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				generateMessage.setBool(false);
				bool = false;
				nbThread = 0;
				logger.log(Level.INFO, "Generation stopped");
			}
		});

		///////////////////////// FRAME/////////////////////////////////////////////////
		/**
		 * Different parameters of the window
		 */
		this.setLayout(new BorderLayout());
		this.setBackground(color);
	}

	/**
	 * Launch the generation of messages
	 */
	public void launchGeneration() {
		if (nbThread == 0) {
			bool = true;
			generateMessage = new GenerateMessage(message, listSensor, request);
			generateMessage.setThreshold(threshold);
			generateMessage.start();
			nbThread++;
		}
	}

	/**
	 * Send a request to get all the sensors
	 */
	public void getAllSensor() {
		requestType = "READ ALL";
		table = "Sensor";
		objectMapper = new ObjectMapper();
		try {
			jsonString = "READ ALL";
			new MockSocket(requestType, jsonString, table);
			jsonString = MockSocket.getJson();
			Sensor[] sensors = objectMapper.readValue(jsonString, Sensor[].class);
			listSensor = Arrays.asList(sensors);
			logger.log(Level.DEBUG, "Find Sensor data succed");
		} catch (Exception e1) {
			logger.log(Level.WARN, "Impossible to parse in JSON Sensor data " + e1.getClass().getCanonicalName());
		}
	}

	/**
	 * @return the threadGenerateMessage
	 */
	public Thread getThreadGenerateMessage() {
		return threadGenerateMessage;
	}

	/**
	 * @param threadGenerateMessage the threadGenerateMessage to set
	 */
	public void setThreadGenerateMessage(Thread threadGenerateMessage) {
		this.threadGenerateMessage = threadGenerateMessage;
	}

	/**
	 * @return the bool
	 */
	public boolean getBool() {
		return bool;
	}

	/**
	 * @param bool the bool to set
	 */
	public void setBool(boolean bool) {
		this.bool = bool;
	}
}