package com.ehpadtech.monitor.mocker.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import  com.ehpadtech.monitor.mocker.generator.GenerateEmployee;
import  com.ehpadtech.monitor.mocker.generator.GenerateResident;

public class TabMockOther extends JPanel {

	/**
	 * Different parameters used
	 */
	private static final long serialVersionUID = 1L;
	private Font policeLabel;
	private JLabel labelNbGenerateEmployee;
	private JLabel labelNbGenerateResident;
	private JTextField textInputNbGenerateEmployee;
	private JTextField textInputNbGenerateResident;
	private JButton generateButtonEmployee;
	private JButton generateButtonResident;
	private JButton restoreButton;
	private GenerateEmployee generateEmployee;
	private GenerateResident generateResident;

	/**
	 * Constructor
	 */
	public TabMockOther() {
	}

	/**
	 * Constructor
	 * 
	 * @param color
	 * @param title
	 */
	public TabMockOther(Color color, String title) {
		///////////////////////// LABEL///////////////////////////////////////////////
		/**
		 * Definition of labelNbGenerate Employee
		 */
		policeLabel = new Font("Arial", Font.BOLD, 20);
		labelNbGenerateEmployee = new JLabel("Number employee to generate : ");
		labelNbGenerateEmployee.setBounds(100, 100, 350, 30);
		labelNbGenerateEmployee.setFont(policeLabel);
		this.add(labelNbGenerateEmployee);

		/**
		 * Definition of labelNbGenerate Resident
		 */
		policeLabel = new Font("Arial", Font.BOLD, 20);
		labelNbGenerateResident = new JLabel("Number resident to generate : ");
		labelNbGenerateResident.setBounds(450, 100, 350, 30);
		labelNbGenerateResident.setFont(policeLabel);
		this.add(labelNbGenerateResident);

		//////////////////// TEXT AREA////////////////////////////////////////////////
		/**
		 * Definition of textArea NbGenerate Employee
		 */
		textInputNbGenerateEmployee = new JTextField();
		textInputNbGenerateEmployee.setBounds(100, 150, 250, 30);
		textInputNbGenerateEmployee.setFont(policeLabel);
		textInputNbGenerateEmployee.setText("");
		this.add(textInputNbGenerateEmployee);

		/**
		 * Definition of textArea NbGenerate Resident
		 */
		textInputNbGenerateResident = new JTextField();
		textInputNbGenerateResident.setBounds(450, 150, 250, 30);
		textInputNbGenerateResident.setFont(policeLabel);
		textInputNbGenerateResident.setText("");
		this.add(textInputNbGenerateResident);

		///////////////////////// BUTTON/////////////////////////////////////////////////
		/**
		 * Definition of Button AddEmployee
		 */
		generateButtonEmployee = new JButton("Generate Employee");
		generateButtonEmployee.setBounds(100, 250, 200, 40);
		this.add(generateButtonEmployee);
		generateButtonEmployee.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (textInputNbGenerateEmployee.getText().trim().equals("")) {
					JOptionPane.showMessageDialog(null, "Number of Employee to generate is empty", "Information", JOptionPane.INFORMATION_MESSAGE);
				} else {
					if (textInputNbGenerateEmployee.getText().trim().matches("[0-9]+[0-9]*")) {
						generateEmployee = new GenerateEmployee(Integer.parseInt(textInputNbGenerateEmployee.getText().trim()));
						generateEmployee.generate();
					} else {
						JOptionPane.showMessageDialog(null, "The number of generation is an integer", "Information", JOptionPane.INFORMATION_MESSAGE);
					}
				}
			}
		});

		/**
		 * Definition of Button AddResident
		 */
		generateButtonResident = new JButton("Generate Resident");
		generateButtonResident.setBounds(500, 250, 200, 40);
		this.add(generateButtonResident);
		generateButtonResident.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (textInputNbGenerateResident.getText().trim().equals("")) {
					JOptionPane.showMessageDialog(null, "Number of Resident to generate is empty", "Information", JOptionPane.INFORMATION_MESSAGE);
				} else {
					if (textInputNbGenerateResident.getText().trim().matches("[0-9]+[0-9]*")) {
						generateResident = new GenerateResident(Integer.parseInt(textInputNbGenerateResident.getText().trim()));
						generateResident.generate();
					} else {
						JOptionPane.showMessageDialog(null, "The number of generation is an integer", "Information", JOptionPane.INFORMATION_MESSAGE);
					}
				}
			}
		});

		/**
		 * Set to default values the different fields
		 */
		restoreButton = new JButton("Restore");
		restoreButton.setBounds(350, 250, 100, 40);
		this.add(restoreButton);
		restoreButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				textInputNbGenerateEmployee.setText("");
				textInputNbGenerateResident.setText("");
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
