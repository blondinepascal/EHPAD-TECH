package com.ehpadtech.monitor.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.Normalizer;
import java.util.ResourceBundle;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.ehpadtech.monitor.commons.entity.Employee;
import com.ehpadtech.monitor.launcher.ClientSocket;
import com.fasterxml.jackson.databind.ObjectMapper;


public class TabProfile extends JPanel {

	/**
	 * Different parameters used
	 */
	private static final long serialVersionUID = 1L;
	private String requestType;
	private String table;
	private String jsonString;
	private int idemployee;
	private JPanel bar;
	private JLabel labelIdEmployee;
	private JLabel labelLastnameEmployee;
	private JLabel labelNameEmployee;
	private JLabel labelFunction;
	private JLabel labelPassword;
	private JTextField textInputLastnameEmployee;
	private JTextField textInputNameEmployee;
	private JTextField textInputFunctionEmployee;
	private JPasswordField textInputPasswordEmployee;
	private Font policeBar;
	private Font policeLabel;
	private Employee employee;
	private Employee employee2;
	private JButton disconnection;
	private JButton save;
	private JButton restaure;
	private JCheckBox showButton;
	private ObjectMapper readMapper;
	private Thread threadProfile;
	private static final Logger logger = LogManager.getLogger(TabProfile.class);
	private ResourceBundle rs = ResourceBundle.getBundle("parameters");

	/*
	 * Constructor
	 */
	public TabProfile() {
	}

	/**
	 * Constructor
	 * 
	 * @param color
	 * @param idemployee
	 * @param title
	 */
	public TabProfile(Color color, int idemployee, String title) {
		this.idemployee = idemployee;

		///////////////////////// Thread/////////////////////////////////////////////////
		setThreadProfile(new Thread(new Runnable() {
			/**
			 * Loop and update every 30 seconds the list of employees
			 */
			@Override
			public void run() {
				while (true) {
					updateEmployee();
					try {
						Thread.sleep(Integer.parseInt(rs.getString("time_threadSleep")));
					} catch (InterruptedException e) {
						logger.log(Level.WARN,
								"Impossible to sleep the thread Profile " + e.getClass().getCanonicalName());
					}
				}
			}
		}));

		/**
		 * Definition of the structures of this tab
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
		policeBar = new Font("Arial", Font.BOLD, 16);
		labelIdEmployee.setForeground(Color.WHITE);
		labelIdEmployee.setFont(policeBar);
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

		///////////////////////// LABEL///////////////////////////////////////////////
		/**
		 * Definition of label LastnameEmployee
		 */
		policeLabel = new Font("Arial", Font.BOLD, (int) getToolkit().getScreenSize().getWidth() / 80);
		labelLastnameEmployee = new JLabel("Last Name : ");
		labelLastnameEmployee.setBounds((int) getToolkit().getScreenSize().getWidth() * 1 / 4,
				(int) getToolkit().getScreenSize().getHeight() * 2 / 10, 200, 30);
		labelLastnameEmployee.setFont(policeLabel);
		labelLastnameEmployee.setForeground(Color.WHITE);
		this.add(labelLastnameEmployee);

		/**
		 * Definition of label NameEmployee
		 */
		labelNameEmployee = new JLabel("Name : ");
		labelNameEmployee.setBounds((int) getToolkit().getScreenSize().getWidth() * 2 / 4,
				(int) getToolkit().getScreenSize().getHeight() * 2 / 10, 200, 30);
		labelNameEmployee.setFont(policeLabel);
		labelNameEmployee.setForeground(Color.WHITE);
		this.add(labelNameEmployee);

		/**
		 * Definition of label Password
		 */
		labelPassword = new JLabel("Password : ");
		labelPassword.setBounds((int) getToolkit().getScreenSize().getWidth() * 1 / 4,
				(int) getToolkit().getScreenSize().getHeight() * 4 / 10, 200, 30);
		labelPassword.setFont(policeLabel);
		labelPassword.setForeground(Color.WHITE);
		this.add(labelPassword);

		/**
		 * Definition of label Function
		 */
		labelFunction = new JLabel("Function : ");
		labelFunction.setBounds((int) getToolkit().getScreenSize().getWidth() * 2 / 4,
				(int) getToolkit().getScreenSize().getHeight() * 4 / 10, 150, 30);
		labelFunction.setFont(policeLabel);
		labelFunction.setForeground(Color.WHITE);
		this.add(labelFunction);

		//////////////////// TEXT AREA////////////////////////////////////////////////
		/**
		 * Definition of textArea LastnameEmployee
		 */
		textInputLastnameEmployee = new JTextField();
		textInputLastnameEmployee.setBounds((int) getToolkit().getScreenSize().getWidth() * 1 / 4,
				(int) getToolkit().getScreenSize().getHeight() * 5 / 20, 300, 40);
		textInputLastnameEmployee.setFont(policeLabel);
		this.add(textInputLastnameEmployee);

		/**
		 * Definition of textArea NameEmployee
		 */
		textInputNameEmployee = new JTextField();
		textInputNameEmployee.setBounds((int) getToolkit().getScreenSize().getWidth() * 2 / 4,
				(int) getToolkit().getScreenSize().getHeight() * 5 / 20, 300, 40);
		textInputNameEmployee.setFont(policeLabel);
		this.add(textInputNameEmployee);

		/**
		 * Definition of textArea Password
		 */
		textInputPasswordEmployee = new JPasswordField();
		textInputPasswordEmployee.setBounds((int) getToolkit().getScreenSize().getWidth() * 1 / 4,
				(int) getToolkit().getScreenSize().getHeight() * 9 / 20, 300, 40);
		textInputPasswordEmployee.setFont(policeLabel);
		this.add(textInputPasswordEmployee);

		/**
		 * Definition of textArea Function
		 */
		textInputFunctionEmployee = new JTextField();
		textInputFunctionEmployee.setBounds((int) getToolkit().getScreenSize().getWidth() * 2 / 4,
				(int) getToolkit().getScreenSize().getHeight() * 9 / 20, 300, 40);
		textInputFunctionEmployee.setFont(policeLabel);
		this.add(textInputFunctionEmployee);

		/**
		 * Button to display the password
		 */
		showButton = new JCheckBox("Show password");
		showButton.setBounds((int) getToolkit().getScreenSize().getWidth() * 1 / 4,
				((int) getToolkit().getScreenSize().getHeight() * 9 / 20) + 50, 300, 40);
		showButton.setOpaque(false);
		showButton.setForeground(Color.WHITE);
		showButton.setFont(policeLabel);
		this.add(showButton);
		showButton.addActionListener(new ActionListener() {
			/**
			 * Display the content of TextField password employee When we check the CheckBox
			 * "Show password"
			 */
			public void actionPerformed(ActionEvent e) {
				if (showButton.isSelected()) {
					logger.log(Level.DEBUG, "Password show");
					textInputPasswordEmployee.setEchoChar((char) 0);
				} else {
					logger.log(Level.DEBUG, "Password hidden");
					textInputPasswordEmployee.setEchoChar('*');
				}
			}
		});

		///////////////////////// GET DATAS//////////////////////////////////////////
		updateEmployee();

		//////////////////// BUTTON////////////////////////////////////////////////
		/**
		 * Definition of Button Save
		 */
		save = new JButton("Save");
		save.setBounds(((int) getToolkit().getScreenSize().getWidth() * 2 / 4) + 250,
				(int) getToolkit().getScreenSize().getHeight() * 15 / 20, 200, 40);
		this.add(save);
		save.addActionListener(new ActionListener() {
			/**
			 * When we pressed the button save we update the Employee data we check if the
			 * informations are correct, if the textField are not empty and we suppress the
			 * special characters
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				requestType = "UPDATE";
				table = "Employee";

				String newLastnameEmployee = textInputLastnameEmployee.getText().trim();
				newLastnameEmployee = Normalizer.normalize(newLastnameEmployee, Normalizer.Form.NFD)
						.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
				String newNameEmployee = textInputNameEmployee.getText().trim();
				newNameEmployee = Normalizer.normalize(newNameEmployee, Normalizer.Form.NFD)
						.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
				String newFunctionEmployee = textInputFunctionEmployee.getText().trim();
				newFunctionEmployee = Normalizer.normalize(newFunctionEmployee, Normalizer.Form.NFD)
						.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
				char[] password = textInputPasswordEmployee.getPassword();
				String newPasswordEmployee = new String(password);

				/**
				 * if text area are empty they open an pop-up
				 */
				if (newLastnameEmployee.equals("") || newNameEmployee.equals("") || newFunctionEmployee.equals("")) {
					JOptionPane.showMessageDialog(null, "Empty fields", "Error", JOptionPane.INFORMATION_MESSAGE);
				} else {
					/**
					 * if they want to update the password they will input the current password to
					 * be verify by system
					 */
					if (!(newPasswordEmployee.equals(""))) {
						String verificationPassword = JOptionPane.showInputDialog(null, "Insert your old password", "");
						String request = "CONNECTION";
						employee2 = new Employee();

						employee2.setIdEmployee(idemployee);
						employee2.setPassword(verificationPassword);
						try {
							jsonString = readMapper.writeValueAsString(employee2);
							new ClientSocket(request, jsonString, table);
							jsonString = ClientSocket.getJson();
							employee2 = readMapper.readValue(jsonString, Employee.class);
						} catch (IOException e1) {
							logger.log(Level.WARN,
									"Impossible to parse in JSON Employee datas" + e1.getClass().getCanonicalName());
						}
						/**
						 * If the password is correct they send the datas to be updated
						 */
						if (!(employee2.getLastnameEmployee().equals(""))) {
							employee.setIdEmployee(idemployee);
							employee.setLastnameEmployee(newLastnameEmployee.toUpperCase());
							employee.setNameEmployee(newNameEmployee);
							employee.setFunction(newFunctionEmployee.toUpperCase());
							employee.setPassword(newPasswordEmployee);
							textInputPasswordEmployee.setText("");
							try {
								jsonString = readMapper.writeValueAsString(employee);
								new ClientSocket(requestType, jsonString, table);
								jsonString = ClientSocket.getJson();
								if (!jsonString.equals("UPDATED")) {
									JOptionPane.showMessageDialog(null, "Update failed", "Error",
											JOptionPane.ERROR_MESSAGE);
									logger.log(Level.WARN, "Impossible to update employee");
								} else {
									logger.log(Level.DEBUG, "Update Succeeded");
									JOptionPane.showMessageDialog(null, "Datas updated", "Information",
											JOptionPane.INFORMATION_MESSAGE);
								}
							} catch (Exception e1) {
								logger.log(Level.WARN,
										"Impossible to parse in JSON " + e1.getClass().getCanonicalName());
							}
						} else {
							JOptionPane.showMessageDialog(null, "One more time and you will be kicked",
									"Error, Wrong password", JOptionPane.INFORMATION_MESSAGE);
						}
					} else {
						employee.setIdEmployee(idemployee);
						employee.setLastnameEmployee(newLastnameEmployee.toUpperCase());
						employee.setNameEmployee(newNameEmployee);
						employee.setFunction(newFunctionEmployee.toUpperCase());

						try {
							jsonString = readMapper.writeValueAsString(employee);
							new ClientSocket(requestType, jsonString, table);
							jsonString = ClientSocket.getJson();
							if (!jsonString.equals("UPDATED")) {
								JOptionPane.showMessageDialog(null, "Update failed", "Error",
										JOptionPane.ERROR_MESSAGE);
								logger.log(Level.WARN, "Impossible to update employee");
							} else {
								logger.log(Level.DEBUG, "Update Succeeded");
								JOptionPane.showMessageDialog(null, "Datas updated", "Information",
										JOptionPane.INFORMATION_MESSAGE);
							}
						} catch (Exception e1) {
							logger.log(Level.WARN, "Impossible to parse in JSON " + e1.getClass().getCanonicalName());
						}
					}
				}
			}
		});

		/**
		 * Definition of Button Restore
		 */
		restaure = new JButton("Restore");
		restaure.setBounds(((int) getToolkit().getScreenSize().getWidth() * 2 / 4),
				(int) getToolkit().getScreenSize().getHeight() * 15 / 20, 200, 40);
		this.add(restaure);
		restaure.addActionListener(new ActionListener() {
			/**
			 * when we pressed the button restore we initialize the textArea with the last
			 * informations of the employee
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				textInputLastnameEmployee.setText(employee.getLastnameEmployee());
				textInputNameEmployee.setText(employee.getNameEmployee());
				textInputFunctionEmployee.setText(employee.getFunction());
				textInputPasswordEmployee.setText("");
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
	 * Launch thread
	 */
	public void threadLauncher() {
		threadProfile.start();
		logger.log(Level.DEBUG, "Thread Profile started");
	}

	///////////////////////// GET DATAS//////////////////////////////////////////
	/**
	 * Get information about employee who sign in the application
	 */
	public void updateEmployee() {
		requestType = "READ";
		employee = new Employee();
		table = "Employee";
		readMapper = new ObjectMapper();
		employee.setIdEmployee(idemployee);
		try {
			jsonString = readMapper.writeValueAsString(employee);
			new ClientSocket(requestType, jsonString, table);
			jsonString = ClientSocket.getJson();
			employee = readMapper.readValue(jsonString, Employee.class);
			logger.log(Level.DEBUG, "Find Employee data succed");
		} catch (Exception e1) {
			logger.log(Level.WARN, "Impossible to parse in JSON Employee datas " + e1.getClass().getCanonicalName());
		}
		textInputLastnameEmployee.setText(employee.getLastnameEmployee());
		textInputNameEmployee.setText(employee.getNameEmployee());
		textInputFunctionEmployee.setText(employee.getFunction());
		textInputPasswordEmployee.setText("");
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
	 * @return the threadProfile
	 */
	public Thread getThreadProfile() {
		return threadProfile;
	}

	/**
	 * @param threadProfile the threadProfile to set
	 */
	public void setThreadProfile(Thread threadProfile) {
		this.threadProfile = threadProfile;
	}
}