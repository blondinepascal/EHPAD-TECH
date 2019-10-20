package com.ehpadtech.monitor.mocker.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import com.ehpadtech.monitor.commons.entity.SensorType;

public class TabMockSensor extends JPanel {

	/**
	 * Different parameters used
	 */
	private static final long serialVersionUID = 1L;
	private Font policeLabel;
	private JLabel labelNbGenerateSensor;
	private JLabel labelTypeSensor;
	private JTextField textInputNbGenerateSensor;
	private JComboBox textInputTypeSensor;
	private JButton generateButtonSensor;
	private JButton restoreButton;
	private JRadioButton configuredRadio;
	private JRadioButton notConfiguredRadio;
	private ButtonGroup buttonGroup;
	//private GenerateSensor generateSensor;

	/**
	 * Constructor
	 */
	public TabMockSensor() {
	}

	public TabMockSensor(Color color, String title) {
		///////////////////////// LABEL///////////////////////////////////////////////
		/**
		 * Definition of labelNbGenerate Sensor
		 */
		policeLabel = new Font("Arial", Font.BOLD, 20); 
		labelNbGenerateSensor = new JLabel("Number: ");
		labelNbGenerateSensor.setBounds(100, 100, 350, 30);
		labelNbGenerateSensor.setFont(policeLabel);
		this.add(labelNbGenerateSensor);

		/**
		 * Definition of labelNbGenerate Sensor
		 */
		labelTypeSensor = new JLabel("Type: ");
		labelTypeSensor.setBounds(100, 200, 350, 30);
		labelTypeSensor.setFont(policeLabel);
		this.add(labelTypeSensor);
		//////////////////// TEXT AREA////////////////////////////////////////////////
		/**
		 * Definition of textArea NbGenerate Sensor
		 */
		textInputNbGenerateSensor = new JTextField();
		textInputNbGenerateSensor.setBounds(100, 150, 250, 30);
		textInputNbGenerateSensor.setFont(policeLabel);
		textInputNbGenerateSensor.setText("");
		this.add(textInputNbGenerateSensor);

		/**
		 * Definition of ButtonGroup RandomSensor
		 */
		configuredRadio = new JRadioButton("Configured");
		configuredRadio.setBounds(400, 150, 150, 30);
		configuredRadio.setFont(policeLabel);
		configuredRadio.setBackground(color);

		notConfiguredRadio = new JRadioButton("Not configured");
		notConfiguredRadio.setBounds(600, 150, 200, 30);
		notConfiguredRadio.setFont(policeLabel);
		notConfiguredRadio.setBackground(color);

		buttonGroup = new ButtonGroup();
		buttonGroup.add(configuredRadio);
		buttonGroup.add(notConfiguredRadio);

		this.add(configuredRadio);
		this.add(notConfiguredRadio);

		/**
		 * Definition of ComboBox TypeSensor
		 */
		String[] types = { "ALL", SensorType.SMOKE.name(), SensorType.MOVE.name(), SensorType.TEMPERATURE.name(),
				SensorType.WINDOW.name(), SensorType.DOOR.name() };
		textInputTypeSensor = new JComboBox<String>(types);
		textInputTypeSensor.setBounds(100, 250, 250, 40);
		textInputTypeSensor.setFont(policeLabel);
		textInputTypeSensor.setSelectedIndex(0);
		this.add(textInputTypeSensor);

		///////////////////////// BUTTON/////////////////////////////////////////////////
		/**
		 * Definition of Button AddSensor
		 */
		generateButtonSensor = new JButton("Generate Sensor");
		generateButtonSensor.setBounds(100, 350, 200, 40);
		this.add(generateButtonSensor);
		generateButtonSensor.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (textInputNbGenerateSensor.getText().trim().equals("")) {
					JOptionPane.showMessageDialog(null, "Number of Sensor to generate is empty", "Information",
							JOptionPane.INFORMATION_MESSAGE);
				} else {
					if (textInputNbGenerateSensor.getText().trim().matches("[0-9]+[0-9]*")) {
						// TODO Create class generateSensor
						// generateEmployee = new GenerateSensor(
						// Integer.parseInt(textInputNbGenerateSensor.getText().trim()));
						// generateEmployee.generate();
					} else {
						JOptionPane.showMessageDialog(null, "The number of generation is an integer", "Information",
								JOptionPane.INFORMATION_MESSAGE);
					}
				}
			}
		});

		/**
		 * Set to default values the different fields
		 */
		restoreButton = new JButton("Restore");
		restoreButton.setBounds(350, 350, 100, 40);
		//this.add(restoreButton);
		restoreButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				textInputNbGenerateSensor.setText("");
			}
		});

		///////////////////////// FRAME/////////////////////////////////////////////////
		/**
		 * Different parameters of the window
		 */
		this.setLayout(new BorderLayout());
		this.setBackground(color);
	}
}
