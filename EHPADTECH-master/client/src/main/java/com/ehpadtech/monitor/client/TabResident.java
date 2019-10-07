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
import java.text.Normalizer;
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
import javax.swing.JTextField;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.ehpadtech.monitor.commons.entity.Resident;
import com.ehpadtech.monitor.launcher.ClientSocket;
import com.ehpadtech.monitor.utils.SortByIdResident;
import com.fasterxml.jackson.databind.ObjectMapper;


public class TabResident extends JPanel {

	/**
	 * Different parameters used
	 */
	private static final long serialVersionUID = 1L;
	private int idemployee;
	private int index;
	private JPanel bar;
	private JPanel search;
	private JLabel labelIdEmployee;
	private JLabel labelLastnameResident;
	private JLabel labelNameResident;
	private JLabel labelIdResident;
	private JLabel labelSearch;
	private JLabel labelHeadList;
	private JTextField searchBar;
	private JTextField textInputLastnameResident;
	private JTextField textInputNameResident;
	private JTextField textInputIdResident;
	private Font policeBar;
	private Font policeLabel;
	private JButton disconnection;
	private JButton addResident;
	private JButton save;
	private JButton delete;
	private JButton restaure;
	private JButton validButton;
	private Resident resident;
	private Resident resident2;
	private String requestType;
	private String table;
	private String jsonString;
	private JScrollPane sc;
	private ObjectMapper objectMapper;
	private Thread threadResident;
	private JList<String> list;
	private DefaultListModel<String> listM;
	private List<Resident> listResident;
	private List<Resident> listSearchResident;
	private static final Logger logger = LogManager.getLogger(TabProfile.class);
	private ResourceBundle rs = ResourceBundle.getBundle("parameters");

	/**
	 * Constructor
	 */
	public TabResident() {
	}

	/**
	 * Constructor
	 * 
	 * @param color
	 * @param idemployee
	 * @param title
	 */
	public TabResident(Color color, int idemployee, String title) {
		this.idemployee = idemployee;
		this.listResident = new ArrayList<Resident>();
		this.listSearchResident = new ArrayList<Resident>();

		///////////////////////// Thread/////////////////////////////////////////////////
		setThreadResident(new Thread(new Runnable() {
			/**
			 * Loop and update every 30 seconds the list of resident
			 */
			@Override
			public void run() {
				while (true) {
					updateResidentSelected();
					updateListResident();
					try {
						Thread.sleep(Integer.parseInt(rs.getString("time_threadSleep")));
					} catch (InterruptedException e) {
						logger.log(Level.WARN,
								"Impossible to sleep the thread Resident " + e.getClass().getCanonicalName());
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

		/**
		 * Definition of the panel Search
		 */
		search = new JPanel();
		search.setBackground(Color.DARK_GRAY);
		search.setBorder(BorderFactory.createMatteBorder(0, 25, 0, 25, bar.getBackground()));
		bar.add(search);

		/**
		 * Definition of the label search and add on panel search
		 */
		labelSearch = new JLabel();
		labelSearch.setText("Research : ");
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
		validButton.setText("Search");
		search.add(validButton);
		validButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				index = -9999;
				resident = new Resident();
				String searchReceived = searchBar.getText().trim();
				if (!searchReceived.equals("")) {
					/**
					 * If the research is just numerics they find first the IdResident
					 */
					if (searchReceived.matches("[0-9]+[0-9]*")) {
						requestType = "READ";
						resident2 = new Resident();
						table = "Resident";
						resident2.setIdResident(Integer.parseInt(searchReceived));
						try {
							jsonString = objectMapper.writeValueAsString(resident2);
							new ClientSocket(requestType, jsonString, table);
							jsonString = ClientSocket.getJson();
							resident2 = objectMapper.readValue(jsonString, Resident.class);
							logger.log(Level.DEBUG, "Find Resident data succed");
						} catch (Exception e1) {
							logger.log(Level.WARN,
									"Impossible to parse in JSON Resident datas " + e1.getClass().getCanonicalName());
						}
						listM.removeAllElements();
						listM.addElement("Results for resident with id : " + searchReceived);
						if (!resident2.getLastnameResident().equals("")) {
							listM.addElement(resident2.getIdResident() + "# " + resident2.getLastnameResident() + " "
									+ resident2.getNameResident());
						}

					} else {
						/**
						 * If the research is letter and numerics
						 */
						searchReceived = Normalizer.normalize(searchReceived, Normalizer.Form.NFD)
								.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
						resident2 = new Resident();
						resident2.setLastnameResident(searchReceived);
						requestType = "FIND ALL";
						table = "Resident";
						objectMapper = new ObjectMapper();
						try {
							jsonString = objectMapper.writeValueAsString(resident2);
							new ClientSocket(requestType, jsonString, table);
							jsonString = ClientSocket.getJson();
							Resident[] residents = objectMapper.readValue(jsonString, Resident[].class);
							listSearchResident = Arrays.asList(residents);
							logger.log(Level.DEBUG, "Find Resident data succed");
						} catch (Exception e1) {
							logger.log(Level.WARN,
									"Impossible to parse in JSON Resident datas " + e1.getClass().getCanonicalName());
						}
						listM.removeAllElements();
						listM.addElement("Results for resident with : " + searchReceived);
						if (listSearchResident.size() > 0)
							for (Resident residents : listSearchResident) {
								listM.addElement(residents.getIdResident() + "# " + residents.getLastnameResident()
										+ " " + residents.getNameResident());
							}
					}

				} else {
					/**
					 * If the research is empty they display all the Resident
					 */
					requestType = "READ ALL";
					table = "Resident";
					objectMapper = new ObjectMapper();
					try {
						jsonString = "READ ALL";
						new ClientSocket(requestType, jsonString, table);
						jsonString = ClientSocket.getJson();
						Resident[] residents = objectMapper.readValue(jsonString, Resident[].class);
						listResident = Arrays.asList(residents);
						logger.log(Level.DEBUG, "Find Resident data succed");
					} catch (Exception e1) {
						logger.log(Level.WARN, "Impossible to parse in JSON " + e1.getClass().getCanonicalName());
					}
					listM.removeAllElements();
					listM.addElement("All residents");
					for (Resident residents : listResident) {
						listM.addElement(residents.getIdResident() + "# " + residents.getLastnameResident() + " "
								+ residents.getNameResident());
					}
				}
				searchBar.setText("");
			}
		});

		///////////////////////// LIST RESIDENT ////////////////////////////////////

		resident = new Resident();
		resident.setIdResident(0);
		resident.setLastnameResident("");
		resident.setNameResident("");

		/**
		 * Find all the Resident in the data base and add on list to be displayed
		 */
		listM = new DefaultListModel<String>();
		list = new JList<String>(listM);
		sc = new JScrollPane(list);
		sc.setBounds(30, 120, 300, ((int) getToolkit().getScreenSize().getHeight() - 300));
		this.add(sc);
		updateListResident();
		/**
		 * when we pressed a line in the list they will send a request to get all the
		 * information about the Resident to be displayed on the textField
		 */
		index = -9999;
		MouseListener mouseListener = new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				index = list.locationToIndex(e.getPoint());
				String substring = listM.getElementAt(index).toString();
				int position = substring.indexOf("#");
				if (position > -1) {
					String id = substring.substring(0, position);

					requestType = "READ";
					resident = new Resident();
					table = "Resident";
					resident.setIdResident(Integer.parseInt(id));
					resident = getResident(resident, requestType, table);
					textInputIdResident.setText(Integer.toString(resident.getIdResident()));
					textInputLastnameResident.setText(resident.getLastnameResident());
					textInputNameResident.setText(resident.getNameResident());
				}
			}
		};
		list.addMouseListener(mouseListener);

		///////////////////////// LABEL///////////////////////////////////////////////
		/**
		 * Definition of label LastnameResident
		 */
		policeLabel = new Font("Arial", Font.BOLD, (int) getToolkit().getScreenSize().getWidth() / 80);

		labelLastnameResident = new JLabel("Last Name : ");
		labelLastnameResident.setBounds((int) getToolkit().getScreenSize().getWidth() * 2 / 7,
				(int) getToolkit().getScreenSize().getHeight() * 2 / 10, 200, 30);
		labelLastnameResident.setFont(policeLabel);
		labelLastnameResident.setForeground(Color.WHITE);
		this.add(labelLastnameResident);

		/**
		 * Definition of label NameResident
		 */
		labelNameResident = new JLabel("Name : ");
		labelNameResident.setBounds((int) getToolkit().getScreenSize().getWidth() * 4 / 7,
				(int) getToolkit().getScreenSize().getHeight() * 2 / 10, 200, 30);
		labelNameResident.setFont(policeLabel);
		labelNameResident.setForeground(Color.WHITE);
		this.add(labelNameResident);

		/**
		 * Definition of label IdResident
		 */

		labelIdResident = new JLabel("ID : ");
		labelIdResident.setBounds((int) getToolkit().getScreenSize().getWidth() * 2 / 7,
				(int) getToolkit().getScreenSize().getHeight() * 4 / 10, 100, 30);
		labelIdResident.setFont(policeLabel);
		labelIdResident.setForeground(Color.WHITE);
		this.add(labelIdResident);

		/**
		 * Definition of label HeadList
		 */
		labelHeadList = new JLabel("ID /Last Name /Name");
		labelHeadList.setBounds(40, 90, 300, 30);
		labelHeadList.setForeground(Color.WHITE);
		labelHeadList.setFont(policeBar);
		this.add(labelHeadList);
		//////////////////// TEXT AREA////////////////////////////////////////////////

		/**
		 * Definition of textArea LastnameResident
		 */
		textInputLastnameResident = new JTextField();
		textInputLastnameResident.setBounds((int) getToolkit().getScreenSize().getWidth() * 2 / 7,
				(int) getToolkit().getScreenSize().getHeight() * 5 / 20, 300, 40);
		textInputLastnameResident.setFont(policeLabel);
		textInputLastnameResident.setText(resident.getLastnameResident());
		this.add(textInputLastnameResident);

		/**
		 * Definition of textArea NameResident
		 */
		textInputNameResident = new JTextField();
		textInputNameResident.setBounds((int) getToolkit().getScreenSize().getWidth() * 4 / 7,
				(int) getToolkit().getScreenSize().getHeight() * 5 / 20, 300, 40);
		textInputNameResident.setFont(policeLabel);
		textInputNameResident.setText(resident.getNameResident());
		this.add(textInputNameResident);

		/**
		 * Definition of textArea IdResident
		 */
		textInputIdResident = new JTextField();
		textInputIdResident.setBounds((int) getToolkit().getScreenSize().getWidth() * 2 / 7,
				(int) getToolkit().getScreenSize().getHeight() * 9 / 20, 300, 40);
		textInputIdResident.setFont(policeLabel);
		textInputIdResident.setEditable(false);
		if (resident.getIdResident() == 0)
			textInputIdResident.setText("");
		else
			textInputIdResident.setText(Integer.toString(resident.getIdResident()));
		this.add(textInputIdResident);

		//////////////////// BUTTON////////////////////////////////////////////////
		/**
		 * Definition of Button AddResident
		 */
		addResident = new JButton("Add");
		addResident.setBounds(30, (int) getToolkit().getScreenSize().getHeight() * 16 / 20, 300, 40);
		this.add(addResident);
		addResident.addActionListener(new ActionListener() {
			/**
			 * When we pressed the button addResident we suppress the space and we get out
			 * the special characters and verify if the textField are empty or not If one of
			 * them is empty they send a message to user else they send the request to
			 * server
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				String newLastnameResident = textInputLastnameResident.getText().trim();
				newLastnameResident = Normalizer.normalize(newLastnameResident, Normalizer.Form.NFD)
						.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
				String newNameResident = textInputNameResident.getText().trim();
				newNameResident = Normalizer.normalize(newNameResident, Normalizer.Form.NFD)
						.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");

				if (newLastnameResident.equals("") || newNameResident.equals("")) {
					JOptionPane.showMessageDialog(null, "Empty field", "Error", JOptionPane.INFORMATION_MESSAGE);
				} else {
					/**
					 * Get the information about the Resident and if the user want to insert a
					 * resident already inserted with the last id selected in the list, we display
					 * an pop-up
					 * 
					 */
					requestType = "READ";
					table = "Resident";
					resident = getResident(resident, requestType, table);
					if (newLastnameResident.equals(resident.getLastnameResident())
							&& newNameResident.equals(resident.getNameResident())) {
						JOptionPane.showMessageDialog(null, "This resident already exist", "Information",
								JOptionPane.WARNING_MESSAGE);
					} else {
						/**
						 * Send the information to server to be inserted in data base
						 */
						requestType = "CREATE";
						table = "Resident";

						resident.setLastnameResident(newLastnameResident.toUpperCase());
						resident.setNameResident(newNameResident);
						try {
							jsonString = objectMapper.writeValueAsString(resident);
							new ClientSocket(requestType, jsonString, table);
							jsonString = ClientSocket.getJson();
							if (!jsonString.equals("INSERTED")) {
								JOptionPane.showMessageDialog(null, "Insertion failed", "Error",
										JOptionPane.ERROR_MESSAGE);
								logger.log(Level.WARN, "Impossible to insert resident");
							} else {
								/**
								 * After insertion we get the informations about the last resident inserted to
								 * be added and displayed on list
								 */
								logger.log(Level.DEBUG, "Insertion Succeeded");
								requestType = "READ ALL";
								table = "Resident";
								listResident = getAllResident(null, requestType, table);
								Collections.sort(listResident, new SortByIdResident());

								int x = listResident.size() - 1;

								resident = listResident.get(x);
								listM.addElement(resident.getIdResident() + "# " + resident.getLastnameResident() + " "
										+ resident.getNameResident());
								JOptionPane.showMessageDialog(null, "Inssertion succeeded", "Infos",
										JOptionPane.INFORMATION_MESSAGE);

								textInputLastnameResident.setText("");
								textInputNameResident.setText("");
							}
						} catch (Exception e1) {
							logger.log(Level.WARN,
									"Impossible to parse in JSON Resident datas" + e1.getClass().getCanonicalName());
						}
					}
				}
			}
		});

		/**
		 * Definition of Button Save
		 */
		save = new JButton("Save");
		save.setBounds(((int) getToolkit().getScreenSize().getWidth() * 5 / 7),
				(int) getToolkit().getScreenSize().getHeight() * 15 / 20, 200, 40);
		this.add(save);
		save.addActionListener(new ActionListener() {
			/**
			 * When we pressed the button save we update the Resident data we check if the
			 * informations are correct
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				requestType = "UPDATE";
				table = "Resident";

				String newLastnameResident = textInputLastnameResident.getText().trim();
				newLastnameResident = Normalizer.normalize(newLastnameResident, Normalizer.Form.NFD)
						.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
				String newNameResident = textInputNameResident.getText().trim();
				newNameResident = Normalizer.normalize(newNameResident, Normalizer.Form.NFD)
						.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");

				/**
				 * if text area are empty they open an pop-up
				 */
				if (newLastnameResident.equals("") || newNameResident.equals("")) {
					JOptionPane.showMessageDialog(null, "Empty field", "Error", JOptionPane.ERROR_MESSAGE);
				} else {
					resident.setLastnameResident(newLastnameResident.toUpperCase());
					resident.setNameResident(newNameResident);
					try {
						jsonString = objectMapper.writeValueAsString(resident);
						new ClientSocket(requestType, jsonString, table);
						jsonString = ClientSocket.getJson();
						if (!jsonString.equals("UPDATED")) {
							JOptionPane.showMessageDialog(null, "Update failed", "Error", JOptionPane.ERROR_MESSAGE);
							logger.log(Level.WARN, "Impossible to update resident");
						} else {
							logger.log(Level.DEBUG, "Update Succeeded");
							listM.set(index, resident.getIdResident() + "# " + resident.getLastnameResident() + " "
									+ resident.getNameResident());
							JOptionPane.showMessageDialog(null, "Datas updated", "Information",
									JOptionPane.INFORMATION_MESSAGE);
						}
					} catch (Exception e1) {
						logger.log(Level.WARN,
								"Impossible to parse in JSON Resident datas" + e1.getClass().getCanonicalName());
					}
				}
			}
		});

		/**
		 * Definition of Button Delete
		 */
		delete = new JButton("Delete");
		delete.setBounds(((int) getToolkit().getScreenSize().getWidth() * 3 / 7),
				(int) getToolkit().getScreenSize().getHeight() * 15 / 20, 150, 40);
		this.add(delete);
		delete.addActionListener(new ActionListener() {
			/**
			 * When we pressed the button delete we get the id of the Resident and we send
			 * it to server, to be deleted by him
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				if (index != -9999) {
					String substring = listM.getElementAt(index).toString();
					int position = substring.indexOf("#");
					if (position > -1) {
						requestType = "DELETE";
						table = "Resident";
						try {
							jsonString = objectMapper.writeValueAsString(resident);
							new ClientSocket(requestType, jsonString, table);
							jsonString = ClientSocket.getJson();
							if (!jsonString.equals("DELETED")) {
								JOptionPane.showMessageDialog(null, "Deletion failed", "Error",
										JOptionPane.ERROR_MESSAGE);
								logger.log(Level.WARN, "Impossible to delete this resident");
							} else {
								JOptionPane.showMessageDialog(null, "Deletion succeeded", "Information",
										JOptionPane.INFORMATION_MESSAGE);
							}
						} catch (Exception e1) {
							logger.log(Level.WARN,
									"Impossible to parse in JSON Resident datas" + e1.getClass().getCanonicalName());
						}
						listM.removeElementAt(index);
						index = (-9999);
						resident.setIdResident(0);
						resident.setLastnameResident("");
						resident.setNameResident("");

						textInputLastnameResident.setText("");
						textInputNameResident.setText("");

					} else {
						JOptionPane.showMessageDialog(null, "Please select an resident to be delete", "Error",
								JOptionPane.INFORMATION_MESSAGE);
					}
				} else {
					JOptionPane.showMessageDialog(null, "Please select an resident to be delete", "Error",
							JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});

		/**
		 * Definition of Button Restore
		 */
		restaure = new JButton("Restore");
		restaure.setBounds(((int)

		getToolkit().getScreenSize().getWidth() * 4 / 7), (int) getToolkit().getScreenSize().getHeight() * 15 / 20, 150,
				40);
		this.add(restaure);
		restaure.addActionListener(new ActionListener() {
			/**
			 * Set the textField at the last Resident selected by the user and if they will
			 * be deleted, they put nothing
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				textInputLastnameResident.setText(resident.getLastnameResident());
				textInputNameResident.setText(resident.getNameResident());
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
		threadResident.start();
		logger.log(Level.DEBUG, "Thread Resident started");
	}

	/**
	 * Send a request to find an resident with the ID
	 * 
	 * @param resident
	 * @param requestType
	 * @param table
	 * @return
	 */
	public Resident getResident(Resident resident, String requestType, String table) {
		objectMapper = new ObjectMapper();
		try {
			jsonString = objectMapper.writeValueAsString(resident);
			new ClientSocket(requestType, jsonString, table);
			jsonString = ClientSocket.getJson();
			resident = objectMapper.readValue(jsonString, Resident.class);
			logger.log(Level.DEBUG, "Find Resident datas succed");
			return resident;
		} catch (Exception e1) {
			logger.log(Level.WARN, "Impossible to parse in JSON Resident datas " + e1.getClass().getCanonicalName());
			return null;
		}
	}

	/**
	 * After have clicked on resident on list, the information will be updated to
	 * have the last informations
	 */
	public void updateResidentSelected() {
		if (index != -9999) {
			String substring = listM.getElementAt(index).toString();
			int position = substring.indexOf("#");
			if (position > -1) {
				String id = substring.substring(0, position);

				requestType = "READ";
				resident = new Resident();
				table = "Resident";
				resident.setIdResident(Integer.parseInt(id));
				resident = getResident(resident, requestType, table);
				textInputLastnameResident.setText(resident.getLastnameResident());
				textInputNameResident.setText(resident.getNameResident());
			}
		}
	}

	/**
	 * Method to update the set the listResident with the data received from Server
	 */
	public void updateListResident() {
		/**
		 * Declare the Object Resident
		 */
		resident = new Resident();
		resident.setLastnameResident("");
		resident.setNameResident("");

		/**
		 * Find all the Resident in the data base and add on list to be displayed
		 */
		requestType = "READ ALL";
		table = "Resident";
		listResident = getAllResident(null, requestType, table);
		Collections.sort(listResident, new SortByIdResident());
		listM.clear();
		listM = new DefaultListModel<>();
		listM.addElement("All residents ");
		for (Resident residents : listResident) {
			listM.addElement(residents.getIdResident() + "# " + residents.getLastnameResident() + " "
					+ residents.getNameResident());
		}
		list.setModel(listM);
	}

	/**
	 * Send a request to have all residents
	 * 
	 * @param resident
	 * @param requestType
	 * @param table
	 * @return
	 */
	public List<Resident> getAllResident(Resident resident, String requestType, String table) {
		objectMapper = new ObjectMapper();
		try {
			jsonString = objectMapper.writeValueAsString(resident);
			new ClientSocket(requestType, jsonString, table);
			jsonString = ClientSocket.getJson();
			Resident[] residents = objectMapper.readValue(jsonString, Resident[].class);
			logger.log(Level.DEBUG, "Find Residents datas succed");
			return Arrays.asList(residents);
		} catch (Exception e1) {
			logger.log(Level.WARN, "Impossible to parse in JSON Residents datas " + e1.getClass().getCanonicalName());
			return null;
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
	 * @return the threadResident
	 */
	public Thread getThreadResident() {
		return threadResident;
	}

	/**
	 * @param threadResident the threadResident to set
	 */
	public void setThreadResident(Thread threadResident) {
		this.threadResident = threadResident;
	}
}