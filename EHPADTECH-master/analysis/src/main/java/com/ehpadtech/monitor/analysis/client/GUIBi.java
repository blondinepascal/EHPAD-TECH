package com.ehpadtech.monitor.analysis.client;

import static java.awt.BorderLayout.CENTER;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import com.ehpadtech.monitor.commons.entity.CommonArea;
import com.ehpadtech.monitor.commons.entity.Message;
import com.ehpadtech.monitor.commons.entity.Sensor;
import com.ehpadtech.monitor.commons.entity.SensorHistorical;
import com.ehpadtech.monitor.commons.entity.SensorType;
import com.ehpadtech.monitor.utils.SortByIdSensorHistorical;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.toedter.calendar.JDateChooser;



public class GUIBi extends JFrame {

	private JList<String> list;
	private JList<String> list1;
	private static List<Sensor> listSensor = new ArrayList<Sensor>();
	private static List<SensorHistorical> listSensorHistorical = new ArrayList<SensorHistorical>();
	private static List<CommonArea> listCommonAreas = new ArrayList<CommonArea>();
	private static List<CommonArea> listNumberCommonAreas = new ArrayList<CommonArea>();
	private static List<Message> listAllMessage = new ArrayList<Message>();
	DefaultPieDataset pieDataset = new DefaultPieDataset();
	DefaultPieDataset HistoricalAlert = new DefaultPieDataset();

	private ObjectMapper objectMapper;
	private String requestType;
	private String table;
	private String jsonString;
	private String request;
	private Integer s1;
	private Integer s2;
	private String etage0;
	private String etage1;
	private Integer AlertReceived;
	private Integer totalAlertReceived;
	private Integer totalNumberPendingAlert;
	private Integer down;
	private Integer over;
	private Integer MeanRC;

	private Integer used;
	private Integer unused;
	private Integer DateUpdate;
	private JPanel contentPane;

	private DefaultListModel<String> listM;
	private DefaultListModel<String> ListModel;
	private DefaultListModel<String> listCount;
	private JScrollPane sc;
	private JScrollPane sc1;

	private JComboBox cbArea;
	private JComboBox cbCapteur;
	private JComboBox cbTotalTypeSensorStock;
	private JComboBox cbAlertReceivedFloor;

	private JButton btnDeconnexion;
	private JButton btnTemperature;
	private JButton btnDate;
	private JButton btnGraphique;
	private JButton btnUpdateDate;
	private static final Logger logger = LogManager.getLogger(TabSensor.class);
	private JLabel lbl;
	private JDateChooser dateChooser;
	private JDateChooser dateChooser_1;
	private JDateChooser dateChooser_2;
	private JDateChooser dateChooser_3;

	private String DateFrom;
	private String DateTo;

	private JTextField tfDate;
	private JTextField tfDate1;
	private JDialog ratioSensors;
	private ChartPanel cPanel;
	private JLabel lblNbOver;
	private JLabel lblTotalAlertAttente;
	private JLabel lblPendingAlert;
	private JLabel lblNbConfigured;
	private int nbTotalAlert = 0;
	private int indice = 0;
	private int nbAlertSensor = 0;
	private JTabbedPane tabbedPane;
	private JPanel panel;
	private JPanel panel_1;
	private JPanel panel_2;
	private JLabel lblNumberOfCommon;
	private JLabel lblNbCommonArea;
	private JTextField tfSensor;
	private JTextField tfAlert;
	private JLabel lblNbSensorMaj_1;
	
	private JLabel lblMeanTemperatureStageRC;
	private JLabel tfMeanTemperatureStage1;
	private JLabel lblTotalStockRc;
	private JLabel lblTotalStockEtage1;
	private JLabel lbTotalStockRc;
	private JLabel lbTotalStockStage1;
	private JLabel lblDown;
	private JTextField tfAlertesReceived;
	private JLabel lblStockSensor;
	private JLabel lblPourcentageActive;
	private JLabel lblActiveSensor;
	private JLabel lblPassiveSensor;

	String[] sensorType = { "ALL", "SMOKE", "MOVE", "TEMPERATURE", "WINDOW", "DOOR", "ELEVATOR", "LIGHT", "FIRE",
			"BADGE", "ROUTER" };
	private JLabel lblTotalConfiguredSensors;
	//////////////////////////////////////////////////////////////////////
	/**
	 * Create the frame.
	 */
	public GUIBi() {

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 998, 636);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

/////////////////////////////////////////////
		//// JCombobox

		String[] periode = { "Année", "Mois", "Jour" };
		String[] area = { "ALL", "0", "1" };

		btnDeconnexion = new JButton("Disconnect");
		btnDeconnexion.setBounds(860, 0, 124, 28);
		contentPane.add(btnDeconnexion);
		btnDeconnexion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				logger.log(Level.INFO, "Application closed, after disconnection");
				System.exit(ABORT);
			}
		});

		/////////////////////////////////////////////


//		Object[] NumberUsedSensor = returns();

///////////////////////////////////////////////////
		// List
		
		listM = new DefaultListModel<String>();

		listM.addElement("Tout les capteurs");
		for (Sensor sensors : listSensor) {
			listM.addElement(sensors.getIdSensor() + "# " + sensors.getTypeSensor() + " ," + sensors.getSensorState()
					+ " ," + sensors.getIdCommonArea());
		}

		ListModel = new DefaultListModel<String>();

		ListModel.addElement("Toute les alertes");
		for (SensorHistorical sensorHistorical : listSensorHistorical) {
			ListModel.addElement(sensorHistorical.getAlertState() + "# " + sensorHistorical.getIdHistorical() + " ,"
					+ sensorHistorical.getIdSensor() + " ," + sensorHistorical.getDate());
		}

/////////////////////////////////////
		// Dialog
		ratioSensors = new JDialog();

////////////////////////////////////

		//////////////////////////////////////////////
		// TabbedPane - DASH BOARD
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(26, 11, 824, 577);
		contentPane.add(tabbedPane);

		// Graphic All sensor

		Integer totalNumberPendingAlert = getNumberPendingAlert();

		String NbCommonArea = getNumberCommonArea();

		Object NbPassiveActiveSensor[] = getNumberPassiveActiveSensor();

		panel = new JPanel();
		panel.setBackground(Color.GRAY);
		tabbedPane.addTab("DASHBOARD", null, panel, null);
//		panel.setBackground( Color.GRAY );
		panel.setLayout(null);

		///////////////////////////////////////////////////////
		// DATE

		dateChooser_2 = new JDateChooser();
		dateChooser_2.setBounds(265, 37, 80, 20);
		panel.add(dateChooser_2);

		dateChooser_3 = new JDateChooser();
		dateChooser_3.setBounds(265, 76, 80, 20);
		panel.add(dateChooser_3);

		JLabel lblNombreDalertes = new JLabel("Total Alerts received");
		lblNombreDalertes.setBounds(76, 516, 144, 22);
		panel.add(lblNombreDalertes);

		JLabel lblTotalOver = new JLabel("Total OVER");
		lblTotalOver.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblTotalOver.setBounds(129, 396, 80, 22);
		panel.add(lblTotalOver);

		lblTotalAlertAttente = new JLabel("Total Pending Alert");
		lblTotalAlertAttente.setFont(new Font("Tahoma", Font.PLAIN, 9));
		lblTotalAlertAttente.setBounds(211, 396, 152, 22);
		panel.add(lblTotalAlertAttente);

		JLabel lblNombreDePannes = new JLabel("Total DOWN");
		lblNombreDePannes.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblNombreDePannes.setBounds(43, 396, 89, 16);
		panel.add(lblNombreDePannes);

		lblNumberOfCommon = new JLabel("number of common area");
		lblNumberOfCommon.setFont(new Font("Tahoma", Font.BOLD, 8));
		lblNumberOfCommon.setBounds(10, 11, 123, 28);
		panel.add(lblNumberOfCommon);

		JLabel lblNbSensorMaj = new JLabel("Total UPDATE SENSORS");
		lblNbSensorMaj.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNbSensorMaj.setBounds(143, 11, 123, 28);
		panel.add(lblNbSensorMaj);

		JLabel lblTotalActive = new JLabel("Total Active");
		lblTotalActive.setBounds(444, 228, 89, 14);
		panel.add(lblTotalActive);

		JLabel lblTotalPassive = new JLabel("Total Passive");
		lblTotalPassive.setBounds(444, 259, 89, 14);
		panel.add(lblTotalPassive);

		JLabel lbltfMeanTemperatureStageRC = new JLabel("Mean temperature Stage RC");
		lbltfMeanTemperatureStageRC.setFont(new Font("Tahoma", Font.BOLD, 11));
		lbltfMeanTemperatureStageRC.setBounds(581, 92, 167, 22);
		panel.add(lbltfMeanTemperatureStageRC);

		JLabel lblMeanTemperatureStage = new JLabel("Mean temperature Stage 1");
		lblMeanTemperatureStage.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblMeanTemperatureStage.setBounds(585, 14, 163, 22);
		panel.add(lblMeanTemperatureStage);

		lbTotalStockRc = new JLabel("Total Sensor RC");
		lbTotalStockRc.setFont(new Font("Tahoma", Font.BOLD, 10));
		lbTotalStockRc.setBounds(545, 516, 101, 22);
		panel.add(lbTotalStockRc);

		lbTotalStockStage1 = new JLabel("Total Sensor Floor 1");
		lbTotalStockStage1.setFont(new Font("Tahoma", Font.BOLD, 10));
		lbTotalStockStage1.setBounds(684, 516, 112, 22);
		panel.add(lbTotalStockStage1);

		tfAlertesReceived = new JTextField();
		tfAlertesReceived.setBounds(116, 452, 69, 55);
		tfAlertesReceived.setColumns(10);
		panel.add(tfAlertesReceived);

		lblDown = new JLabel();
		lblDown.setForeground(Color.RED);
		lblDown.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblDown.setBackground(Color.RED);
		lblDown.setBounds(57, 365, 35, 20);
		panel.add(lblDown);

		lblNbOver = new JLabel();
		lblNbOver.setForeground(Color.ORANGE);
		lblNbOver.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblNbOver.setBackground(Color.RED);
		lblNbOver.setBounds(138, 357, 35, 36);
		panel.add(lblNbOver);

		lblPendingAlert = new JLabel();
		lblPendingAlert.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblPendingAlert.setBackground(Color.LIGHT_GRAY);
		lblPendingAlert.setBounds(231, 357, 114, 36);
		lblPendingAlert.setText(Integer.toString(totalNumberPendingAlert));
		panel.add(lblPendingAlert);

		lblNbCommonArea = new JLabel();
		lblNbCommonArea.setFont(new Font("Tahoma", Font.BOLD, 25));
		lblNbCommonArea.setBackground(Color.RED);
		lblNbCommonArea.setBounds(34, 34, 98, 55);
		lblNbCommonArea.setText(NbCommonArea.toString().replaceAll("\"", ""));
		panel.add(lblNbCommonArea);

		lblNbSensorMaj_1 = new JLabel("0");
		lblNbSensorMaj_1.setFont(new Font("Tahoma", Font.BOLD, 25));
		lblNbSensorMaj_1.setForeground(new Color(0, 255, 0));
		lblNbSensorMaj_1.setBackground(Color.WHITE);
		lblNbSensorMaj_1.setBounds(175, 41, 80, 55);
		panel.add(lblNbSensorMaj_1);
		lblPassiveSensor = new JLabel();
		lblPassiveSensor.setBounds(412, 256, 22, 20);
		lblPassiveSensor.setText(NbPassiveActiveSensor[1].toString().replaceAll("\"", ""));
		panel.add(lblPassiveSensor);

		lblActiveSensor = new JLabel();
		lblActiveSensor.setBounds(412, 228, 22, 20);
		lblActiveSensor.setText(NbPassiveActiveSensor[0].toString().replaceAll("\"", ""));
		panel.add(lblActiveSensor);

		lblPourcentageActive = new JLabel();
		lblPourcentageActive.setFont(new Font("Tahoma", Font.BOLD, 25));
		lblPourcentageActive.setBounds(318, 225, 81, 54);
		lblPourcentageActive.setText(NbPassiveActiveSensor[2].toString().replaceAll("\"", ""));
		panel.add(lblPourcentageActive);

		// String MeanRC = getAverageTemperature();
		lblMeanTemperatureStageRC = new JLabel();
		lblMeanTemperatureStageRC.setForeground(Color.GREEN);
		lblMeanTemperatureStageRC.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblMeanTemperatureStageRC.setHorizontalAlignment(SwingConstants.TRAILING);
		lblMeanTemperatureStageRC.setBounds(559, 37, 113, 44);

		panel.add(lblMeanTemperatureStageRC);

		tfMeanTemperatureStage1 = new JLabel();
		tfMeanTemperatureStage1.setHorizontalAlignment(SwingConstants.TRAILING);
		tfMeanTemperatureStage1.setBounds(601, 125, 57, 44);
		panel.add(tfMeanTemperatureStage1);

		lblTotalStockRc = new JLabel();
		lblTotalStockRc.setForeground(Color.RED);
		lblTotalStockRc.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblTotalStockRc.setHorizontalAlignment(SwingConstants.TRAILING);
		lblTotalStockRc.setBounds(547, 475, 57, 44);


		panel.add(lblTotalStockRc);

		lblTotalStockEtage1 = new JLabel();
		lblTotalStockEtage1.setForeground(Color.RED);
		lblTotalStockEtage1.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblTotalStockEtage1.setHorizontalAlignment(SwingConstants.TRAILING);
		lblTotalStockEtage1.setBounds(684, 475, 57, 44);


		panel.add(lblTotalStockEtage1);

		///////////////////////////////////////////////////////////
		// Button - DASHBOARD
		btnTemperature = new JButton("Calculer");
		btnTemperature.setBounds(684, 47, 124, 28);
		panel.add(btnTemperature);

		btnUpdateDate = new JButton("Get Update");
		btnUpdateDate.setBounds(153, 107, 89, 23);
		panel.add(btnUpdateDate);
		btnUpdateDate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");

				DateFrom = df.format(dateChooser_2.getDate());
				DateTo = df1.format(dateChooser_3.getDate());

				// Method of calculating of Number of UPDATE SENSOR 'parametre date ' AND
				requestType = "COUNT";
				table = "SensorHistorical";
				jsonString = "SELECT COUNT(*) FROM historique WHERE date_historique BETWEEN '" + DateFrom + "'AND '"
						+ DateTo + "'AND ( type_alerte = 'NORMAL')";
				new ClientSocket(requestType, jsonString, table);
				String jsonString_list = ClientSocket.getJson();
				logger.log(Level.DEBUG, "Number sensor updated per period finded");
				System.out.println("resultat date" + jsonString_list);
				DateUpdate = Integer.parseInt(jsonString_list.replaceAll("\"", ""));
				lblNbSensorMaj_1.setText(Integer.toString(DateUpdate));

			}
		});

		JLabel lblSensorPassive = new JLabel("% Sensor Passive");
		lblSensorPassive.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblSensorPassive.setBounds(318, 278, 105, 20);
		panel.add(lblSensorPassive);

		JComboBox cbAlertReceivedFloor_1 = new JComboBox(area);
		cbAlertReceivedFloor_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				JComboBox cbAlertReceivedFloor = (JComboBox) e.getSource();

				if (cbAlertReceivedFloor.getSelectedIndex() == 0) {
					requestType = "COUNT";
					table = "SensorHistorical";
					jsonString = "SELECT COUNT(*) FROM historique";
					new ClientSocket(requestType, jsonString, table);
					jsonString = ClientSocket.getJson();
					logger.log(Level.DEBUG, "Number alert received finded");
					String jsonString_list = jsonString.replaceAll("\"", "");
					AlertReceived = Integer.parseInt(jsonString_list);
					tfAlertesReceived.setText(Integer.toString(AlertReceived));

				} else {

					requestType = "COUNT";
					table = "Sensor";
					jsonString = "SELECT COUNT(*) FROM capteur INNER JOIN historique ON capteur.id_capteur = historique.id_capteur WHERE capteur.id_partie_commune = "
							+ cbAlertReceivedFloor.getSelectedItem().toString() + "";
					new ClientSocket(requestType, jsonString, table);
					jsonString = ClientSocket.getJson();
					logger.log(Level.DEBUG, "Number alert received finded");
					String jsonString_list = jsonString.replaceAll("\"", "");
					AlertReceived = Integer.parseInt(jsonString_list);
					tfAlertesReceived.setText(Integer.toString(AlertReceived));

				}

			}
		});
		cbAlertReceivedFloor_1.setBounds(195, 468, 73, 22);
		panel.add(cbAlertReceivedFloor_1);

		JLabel imagelabel = new JLabel();
		ImageIcon image = new ImageIcon(
				"C:\\\\Users\\\\Slayd\\\\OneDrive\\\\Bureau\\\\cours\\\\PDS\\\\BlackMamba\\\\BlackMamba\\\\Temperature.png");

////////////////////////////////////////////////////////////
		// Combobox in Tabbed Alert
		panel_1 = new JPanel();
		tabbedPane.addTab("Alert", null, panel_1, null);
		panel_1.setLayout(null);
		cbArea = new JComboBox(area);
		cbArea.setBounds(291, 39, 61, 35);
		panel_1.add(cbArea);
		
		/**
		 * Combobox Display Alert
		 */
		cbArea.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				JComboBox cbArea = (JComboBox) e.getSource();
				String selectedBook = (String) cbArea.getSelectedItem();

				if (selectedBook.equals("ALL")) {
					ListModel.clear();
					for (CommonArea area : listCommonAreas) {
						for (Sensor str : listSensor) {
							for (SensorHistorical hist : listSensorHistorical) {
								if (hist.getIdSensor() == str.getIdSensor()) {
									if (str.getIdCommonArea() == area.getIdCommonArea()) {// A MODIFIER PLUS TARD

										ListModel.addElement(str.getIdSensor() + "# " + str.getTypeSensor() + " ,"
												+ str.getSensorState() + " ," + area.getNameCommonArea()
												+ str.getAlertState() + "#" + area.getFloorCommonArea());

									}
								}
							}
						}
					}
					nbTotalAlert = ListModel.getSize();
					nbAlertSensor = ListModel.getSize();
//					tfAlertesReceived.setText(String.valueOf(nbTotalAlert));
					tfAlert.setText(String.valueOf(nbAlertSensor));

				} else if (selectedBook.equals("RC")) {
					ListModel.clear();
					for (CommonArea area : listCommonAreas) {
						if (area.getFloorCommonArea() == 0) {
							for (Sensor str : listSensor) {
								for (SensorHistorical hist : listSensorHistorical) {
									if (hist.getIdSensor() == str.getIdSensor()) {
										if (str.getIdCommonArea() == area.getIdCommonArea()) {// A MODIFIER PLUS TARD

											ListModel.addElement(str.getIdSensor() + "# " + str.getTypeSensor() + " ,"
													+ str.getSensorState() + " ," + area.getNameCommonArea()
													+ str.getAlertState() + "#" + area.getFloorCommonArea());

										}
									}
								}
							}
						}
					}

					nbAlertSensor = ListModel.getSize();
					tfAlert.setText(String.valueOf(nbAlertSensor));
				}

				else if (selectedBook.equals("Etage 1")) {
					ListModel.clear();
					for (CommonArea area : listCommonAreas) {
						if (area.getFloorCommonArea() == 1) {
							for (Sensor str : listSensor) {
								for (SensorHistorical hist : listSensorHistorical) {
									if (hist.getIdSensor() == str.getIdSensor()) {
										if (str.getIdCommonArea() == area.getIdCommonArea()) {// A MODIFIER PLUS TARD

											ListModel.addElement(str.getIdSensor() + "# " + str.getTypeSensor() + " ,"
													+ str.getSensorState() + " ," + area.getNameCommonArea()
													+ str.getAlertState() + "#" + area.getFloorCommonArea());
										}
									}
								}
							}
						}
					}
					nbAlertSensor = ListModel.getSize();
					tfAlert.setText(String.valueOf(nbAlertSensor));
				} else if (selectedBook.equals("Etage 2")) {
					ListModel.clear();
					System.out.println("Nous somme à l'étage 2");

					for (CommonArea area : listCommonAreas) {
						if (area.getFloorCommonArea() == 2) {
							for (Sensor str : listSensor) {
								for (SensorHistorical hist : listSensorHistorical) {
									if (hist.getIdSensor() == str.getIdSensor()) {
										if (str.getIdCommonArea() == area.getIdCommonArea()) {// A MODIFIER PLUS TARD

											ListModel.addElement(str.getIdSensor() + "# " + str.getTypeSensor() + " ,"
													+ str.getSensorState() + " ," + area.getNameCommonArea()
													+ str.getAlertState() + "#" + area.getFloorCommonArea());
										}
									}
								}
							}
						}
					}
				}
				nbAlertSensor = ListModel.getSize();
				tfAlert.setText(String.valueOf(nbAlertSensor));
			}
		});

		cbArea.setEditable(true);
		cbArea.addItem(area);

		/////////////////////////////////////////////////////////////
		// Date - Alert
		dateChooser_1 = new JDateChooser();
		dateChooser_1.setBounds(189, 55, 47, 28);
		panel_1.add(dateChooser_1);

		dateChooser = new JDateChooser();
		dateChooser.setBounds(189, 11, 47, 28);
		panel_1.add(dateChooser);

		// Textfield - Alert
		tfDate = new JTextField();
		tfDate.setBounds(90, 11, 47, 28);
		panel_1.add(tfDate);
		tfDate.setColumns(10);

		tfDate1 = new JTextField();
		tfDate1.setBounds(90, 55, 47, 28);
		panel_1.add(tfDate1);
		tfDate1.setColumns(10);
		// label - Alert
		JLabel lblTo = new JLabel("to");
		lblTo.setBounds(52, 66, 28, 16);
		panel_1.add(lblTo);

		JLabel lblNewLabel = new JLabel("Data Range from");
		lblNewLabel.setBounds(10, 23, 97, 16);
		panel_1.add(lblNewLabel);
		sc1 = new JScrollPane();
		sc1.setBounds(31, 145, 276, 354);
		panel_1.add(sc1);
		list1 = new JList<String>(ListModel);
		sc1.setViewportView(list1);

		// Button - Alert
		btnDate = new JButton("GetDate");
		btnDate.setBounds(386, 39, 97, 22);
		panel_1.add(btnDate);
		btnDate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
				DateFormat df1 = new SimpleDateFormat("dd-MM-yyyy");

				tfDate.setText(df.format(dateChooser.getDate()));
				tfDate1.setText(df1.format(dateChooser_1.getDate()));

///////////////////////////////////////////////////////				
				ListModel.removeAllElements();
				Collections.sort(listSensorHistorical,new SortByIdSensorHistorical());
				for (SensorHistorical sensorHistorical1 : listSensorHistorical) {

					if (sensorHistorical1.getDate().after(dateChooser.getDate())
							&& sensorHistorical1.getDate().before((dateChooser_1.getDate()))) {
						ListModel.addElement(Integer.toString(sensorHistorical1.getIdHistorical()) + " Date : "
								+ sensorHistorical1.getDate() + " ID Sensor : " + sensorHistorical1.getIdSensor()
								+ " State : " + sensorHistorical1.getSensorState() + " Alert State :"
								+ sensorHistorical1.getAlertState());
						nbAlertSensor = ListModel.getSize();
						System.out.println("okay " + nbAlertSensor);
					}
				}
			}
		});

		tfAlert = new JTextField();
		tfAlert.setBounds(330, 262, 119, 67);
		panel_1.add(tfAlert);
		tfAlert.setColumns(10);

		////////////////////////////////////////////////
		// tabbed Sensor
		panel_2 = new JPanel();
		tabbedPane.addTab("Sensor", null, panel_2, null);
		panel_2.setLayout(null);
		sc = new JScrollPane();
		sc.setBounds(38, 64, 313, 381);
		panel_2.add(sc);
		list = new JList<String>(listM);
		sc.setViewportView(list);

		// label Sensor
		JLabel lblSensors = new JLabel("Sensors");
		lblSensors.setBounds(57, 11, 55, 39);
		panel_2.add(lblSensors);

		cbCapteur = new JComboBox(sensorType);
		cbCapteur.setBounds(111, 13, 100, 35);
		panel_2.add(cbCapteur);
		cbCapteur.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				JComboBox cbCapteur = (JComboBox) e.getSource();
				String selectedBook = (String) cbCapteur.getSelectedItem();

				if (selectedBook.equals("ALL")) {
					listM.clear();
					for (Sensor str : listSensor) {

						listM.addElement(str.getIdSensor() + "# " + str.getTypeSensor() + " ," + str.getSensorState()
								+ " ," + str.getIdCommonArea());

						indice = listM.size();
						System.out.println("okay " + indice);

					}

				} else if (selectedBook.equals("SMOKE")) {
					listM.clear();
					for (Sensor str : listSensor) {
						if (str.getTypeSensor().equals(SensorType.SMOKE)) {
							listM.addElement(str.getIdSensor() + "# " + str.getTypeSensor() + " ,"
									+ str.getSensorState() + " ," + str.getIdCommonArea());

							System.out.println("okay " + indice);
						}
					}

				} else if (selectedBook.equals("ROUTER")) {
					listM.clear();
					for (Sensor str : listSensor) {
						if (str.getTypeSensor().equals(SensorType.ROUTER)) {
							listM.addElement(str.getIdSensor() + "# " + str.getTypeSensor() + " ,"
									+ str.getSensorState() + " ," + str.getIdCommonArea());
							indice = listM.size();
						}
					}

				} else if (selectedBook.equals("ELEVATOR")) {
					listM.clear();
					for (Sensor str : listSensor) {
						if (str.getTypeSensor().equals(SensorType.ELEVATOR))
							listM.addElement(str.getIdSensor() + "# " + str.getTypeSensor() + " ,"
									+ str.getSensorState() + " ," + str.getIdCommonArea());
						indice = listM.size();

					}

				} else if (selectedBook.equals("DOOR")) {
					listM.clear();
					for (Sensor str : listSensor) {
						if (str.getTypeSensor().equals(SensorType.DOOR)) {
							listM.addElement(str.getIdSensor() + "# " + str.getTypeSensor() + " ,"
									+ str.getSensorState() + " ," + str.getIdCommonArea());
							indice = listM.size();
						}
					}
				} else if (selectedBook.equals("LIGHT")) {
					listM.clear();

					for (Sensor str : listSensor) {
						if (str.getTypeSensor().equals(SensorType.LIGHT))
							listM.addElement(str.getIdSensor() + "# " + str.getTypeSensor() + " ,"
									+ str.getSensorState() + " ," + str.getIdCommonArea());
						indice = listM.size();

					}

				} else if (selectedBook.equals("MOVE")) {
					listM.clear();
					for (Sensor str : listSensor) {
						if (str.getTypeSensor().equals(SensorType.MOVE)) {
							listM.addElement(str.getIdSensor() + "# " + str.getTypeSensor() + " ,"
									+ str.getSensorState() + " ," + str.getIdCommonArea());
							indice = listM.size();
						}
					}
				} else if (selectedBook.equals("WINDOW")) {
					listM.clear();
					for (Sensor str : listSensor) {
						if (str.getTypeSensor().equals(SensorType.WINDOW))
							listM.addElement(str.getIdSensor() + "# " + str.getTypeSensor() + " ,"
									+ str.getSensorState() + " ," + str.getIdCommonArea());
						indice = listM.size();
					}

				}
			}
		});

		// Textfield in Sensor
		tfSensor = new JTextField();
		tfSensor.setText(String.valueOf(nbAlertSensor));
		tfSensor.setColumns(10);
		tfSensor.setBackground(Color.GREEN);
		tfSensor.setBounds(132, 456, 97, 65);
		panel_2.add(tfSensor);

////////////////////////////////////////////////////////////////////
		// Bouton Graphic
		JButton btnGraphic = new JButton("VisualisationGraphique");
		btnGraphic.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				ratioSensors.getContentPane().add(cPanel, CENTER);
				ratioSensors.pack();
				ratioSensors.setVisible(true);

			}
		});

		getAllSensor();
		getAllSensorHistorical();
		getNumberDownOverSensor();
		getNumberAlertReceived();
		getNumberStageSensor();
//		getAllCommonArea();
//		getNumberPassiveActiveSensor();
		getAllMessage();
		getNumberUsedSensor();

		getNumberCommonArea();
		getAverageTemperature();

	}

////////////////////////////////////
	// Method Get all Sensor contained in the Sensor database
	private void getAllSensor() {

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

	}

///////////////////////////////////////////
	// Method Get all Alert contained in the Historical database
	private void getAllSensorHistorical() {

		requestType = "READ ALL";
		table = "SensorHistorical";
		objectMapper = new ObjectMapper();
		try {
			jsonString = "READ ALL";
			new ClientSocket(requestType, jsonString, table);
			jsonString = ClientSocket.getJson();
			System.out.println(jsonString);
			SensorHistorical[] sensorHistorical = objectMapper.readValue(jsonString, SensorHistorical[].class);
			listSensorHistorical = Arrays.asList(sensorHistorical);
			logger.log(Level.INFO, "Find SensorHistorical data succed");
			nbTotalAlert = listSensorHistorical.size();
		} catch (Exception e1) {
			logger.log(Level.INFO, "Impossible to parse in JSON Sensor data " + e1.getClass().getCanonicalName());
		}

	}
	////////////////////////////////////

	public void getAllMessage() {
		requestType = "READ ALL";
		table = "Message";
		objectMapper = new ObjectMapper();
		try {
			jsonString = "READ ALL";
			new ClientSocket(requestType, jsonString, table);
			jsonString = ClientSocket.getJson();
			Message[] Message = objectMapper.readValue(jsonString, Message[].class);
			logger.log(Level.INFO, "Find CommonArea data succed");
			listAllMessage = Arrays.asList(Message);
		} catch (Exception e1) {
			logger.log(Level.INFO, "Impossible to parse in JSON CommonArea datas " + e1.getClass().getCanonicalName());

		}
	}

//////////////////////////////////////

///////////////////////////////////////////////
	/**
	 * 
	 * @return numberPending Alert
	 */
	public Integer getNumberPendingAlert() {
		requestType = "COUNT";
		table = "Message";
		jsonString = "SELECT COUNT(*) FROM message;";
		new ClientSocket(requestType, jsonString, table);
		jsonString = ClientSocket.getJson();
		logger.log(Level.DEBUG, "Number pending Alert finded");
		String jsonString_list = jsonString.replaceAll("\"", "");
		totalNumberPendingAlert = Integer.parseInt(jsonString_list);

		return totalNumberPendingAlert;
	}

////////////////////////////////////////////////
	/**
	 * 
	 * count the number of passive and active sensors
	 * 
	 */
	public Object[] getNumberPassiveActiveSensor() {
		String s1 = "";
		String s2 = "";
		int s3 = 0;
		int resultat = 0;
		requestType = "COUNT";
		table = "Sensor";
		jsonString = "SELECT sum(case when etat = 'ON' then 1 else 0 end ) As nbActiveSensor, sum(case when etat = 'OFF' then 1 else 0 end ) As nbPassiveSensor FROM capteur WHERE id_capteur !=0";
		new ClientSocket(requestType, jsonString, table);
		jsonString = ClientSocket.getJson();
		logger.log(Level.DEBUG, "Number of sensor Active/passive finded");
		String jsonString_list[] = jsonString.replaceAll("\"", "").split(",");
		s1 = jsonString_list[0];
		s2 = jsonString_list[1];

		BigDecimal bg1, bg2, bg3;

		int active = Integer.parseInt(s1);
		int passive = Integer.parseInt(s2);
		s3 = active + passive;
		bg1 = new BigDecimal(s2);
		bg2 = new BigDecimal(s3);

		bg3 = bg1.divide(bg2, 3, RoundingMode.CEILING).multiply(new BigDecimal(100));

		System.out.println("resultat pourcentage" + resultat + bg3);
		DecimalFormat df2 = new DecimalFormat("###.##");
		return new Object[] { s1, s2, df2.format(bg3) + "%" };
	}

///////////////////////////////////////////////////////////////////
	// number of CommonArea
	public String getNumberCommonArea() {
		requestType = "COUNT";
		table = "CommonArea";
		jsonString = "SELECT COUNT(*) FROM partie_commune";
		new ClientSocket(requestType, jsonString, table);
		jsonString = ClientSocket.getJson();
		logger.log(Level.DEBUG, "Number CommonArea finded");
		return jsonString;
	}

//////////////////////////////////////////////////////////////////
	// Method of calculating the number of DOWN Sensors and OVER Sensors for a
	// graphic visualization
	public void getNumberDownOverSensor() {

		requestType = "COUNT";
		table = "Message";
		jsonString = "SELECT sum(case when seuil = '0' AND (id_capteur !=0 AND id_capteur != 9999) then 1 else 0 end ) As nbSensorDown,sum(case when seuil = '0' AND (id_capteur !=0 AND id_capteur = 9999) then 1 else 0 end ) As nbSensorOver FROM message";
		new ClientSocket(requestType, jsonString, table);
		jsonString = ClientSocket.getJson();
		logger.log(Level.DEBUG, "Number of sensor down/over finded");
		String jsonString_list[] = jsonString.replaceAll("\"", "").split(",");
		down = Integer.parseInt(jsonString_list[0]);
		over = Integer.parseInt(jsonString_list[1]);
		lblDown.setText(down.toString().replaceAll("/", ""));
		lblNbOver.setText(over.toString().replaceAll("/", ""));

		// Graphic Historical alert

		HistoricalAlert.setValue("Number of DOWN Sensors", (Number) down);
		HistoricalAlert.setValue("Number of OVER Sensors", (Number) over);

		JFreeChart pieChart1 = ChartFactory.createPieChart(" Ratio of number of alert", HistoricalAlert, true, true,
				true);
		pieChart1.setBackgroundPaint(Color.GRAY);
		ChartPanel cpAlerts = new ChartPanel(pieChart1);

		cpAlerts.setBounds(33, 181, 275, 165);
		cpAlerts.setOpaque(false);
		panel.add(cpAlerts);

	}

//////////////////////////////////////////////////////////////////////
	// Method of calculating of number Alert Received
	public Integer getNumberAlertReceived() {

		requestType = "COUNT";
		table = "SensorHistorical";
		jsonString = "SELECT COUNT(*) FROM historique";
		new ClientSocket(requestType, jsonString, table);
		jsonString = ClientSocket.getJson();
		logger.log(Level.DEBUG, "Number alert received finded");
		String jsonString_list = jsonString.replaceAll("\"", "");
		totalAlertReceived = Integer.parseInt(jsonString_list);

		return totalAlertReceived;
	}

///////////////////////////////////////////////////////////////////////
	// Method of calculating of number Unused Sensor, number Used Sensor for a
	// graphic visualization
	public void getNumberUsedSensor() {

		requestType = "COUNT";
		table = "Sensor";

		jsonString = "SELECT sum(case when id_capteur = 0 then 1 else 0 end ) As nbUnusedSensor, sum(case when id_capteur != 0 then 1 else 0 end ) As nbUsedSensor FROM capteur";
		new ClientSocket(requestType, jsonString, table);
		jsonString = ClientSocket.getJson();
		logger.log(Level.DEBUG, "Used/UnUsed Sensor finded");
		String jsonString_list[] = jsonString.replaceAll("\"", "").split(",");

		unused = Integer.parseInt(jsonString_list[0]);
		used = Integer.parseInt(jsonString_list[1]);

		pieDataset.setValue("Unused", unused);
		pieDataset.setValue("Used", used);

		JFreeChart pieChart = ChartFactory.createPieChart("Ratio of unused sensors and used", pieDataset, true, true,
				true);
		pieChart.setBackgroundPaint(Color.GRAY);

		ChartPanel cpSensors = new ChartPanel(pieChart);
		cpSensors.setBounds(523, 172, 286, 165);
		panel.add(cpSensors);
		cpSensors.setMouseWheelEnabled(true);

		//////////////////////////////////////////////////////////////////
		// Textfield - DASHBOARD

		lblStockSensor = new JLabel("0");
		lblStockSensor.setBackground(Color.LIGHT_GRAY);
		lblStockSensor.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblStockSensor.setForeground(Color.RED);
		lblStockSensor.setBounds(670, 393, 41, 28);
		panel.add(lblStockSensor);

		// label - DASHBOARD
		JLabel lblStock = new JLabel("Number of sensors in stock");
		lblStock.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblStock.setBounds(589, 432, 184, 16);
		panel.add(lblStock);

		cbTotalTypeSensorStock = new JComboBox(sensorType);
		cbTotalTypeSensorStock.setBounds(635, 357, 87, 22);
		panel.add(cbTotalTypeSensorStock);
		/**
		 * 
		 * 
			count the number of sensors updated
		 * 
		 */
		JButton btnGetConfigured = new JButton("Get Configured");
		btnGetConfigured.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");

				DateFrom = df.format(dateChooser_2.getDate());
				DateTo = df1.format(dateChooser_3.getDate());

				requestType = "COUNT";
				table = "SensorHistorical";
				jsonString = "SELECT COUNT(*) FROM historique WHERE date_historique BETWEEN '" + DateFrom + "'AND '"
						+ DateTo + "'AND ( type_alerte = 'CONFIGURED')";
				new ClientSocket(requestType, jsonString, table);
				String jsonString_list = ClientSocket.getJson();
				logger.log(Level.DEBUG, "Number sensor updated per period finded");
				System.out.println("resultat date" + jsonString_list);
				DateUpdate = Integer.parseInt(jsonString_list.replaceAll("\"", ""));
				lblNbConfigured .setText(Integer.toString(DateUpdate));

				
			}
		});
		btnGetConfigured.setBounds(399, 107, 116, 23);
		panel.add(btnGetConfigured);
		
		lblNbConfigured = new JLabel("0");
		lblNbConfigured.setForeground(Color.GREEN);
		lblNbConfigured.setFont(new Font("Tahoma", Font.BOLD, 25));
		lblNbConfigured.setBounds(438, 52, 69, 44);
		panel.add(lblNbConfigured);
		
		lblTotalConfiguredSensors = new JLabel("Total CONFIGURED SENSORS");
		lblTotalConfiguredSensors.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblTotalConfiguredSensors.setBounds(390, 11, 123, 28);
		panel.add(lblTotalConfiguredSensors);
		
		/**
		 * Combobox for  calculate how many sensors are used and not used
		 */
		cbTotalTypeSensorStock.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				JComboBox cbTotalTypeSensorStock = (JComboBox) e.getSource();

				if (cbTotalTypeSensorStock.getSelectedIndex() == 0) {
					requestType = "COUNT";
					table = "Sensor";
					jsonString = "SELECT sum(case when id_partie_commune = 0 then 1 else 0 end ) As nbUnusedSensor, sum(case when id_partie_commune != 0 then 1 else 0 end ) As nbUsedSensor FROM capteur";
					new ClientSocket(requestType, jsonString, table);
					jsonString = ClientSocket.getJson();
					logger.log(Level.DEBUG, "Used/UnUsed Sensor finded");
					String jsonString_list[] = jsonString.replaceAll("\"", "").split(",");
					unused = Integer.parseInt(jsonString_list[0]);
					used = Integer.parseInt(jsonString_list[1]);

					lblStockSensor.setText(Integer.toString(unused));

					pieDataset.setValue("Unused", unused);
					pieDataset.setValue("Used", used);

					JFreeChart pieChart = ChartFactory.createPieChart("Ratio of unused sensors and used", pieDataset,
							true, true, true);

					pieChart.setBackgroundPaint(Color.GRAY);
					ChartPanel cpSensors = new ChartPanel(pieChart);
					cpSensors.setBounds(575, 21, 228, 140);
					panel.add(cpSensors);
					cpSensors.setMouseWheelEnabled(true);

				} else {

					requestType = "COUNT";
					table = "Sensor";
					jsonString = "SELECT sum(case when id_capteur = 0 then 1 else 0 end ) As nbUnusedSensor, sum(case when id_capteur != 0 then 1 else 0 end ) As nbUsedSensor FROM capteur WHERE type_capteur = '"
							+ cbTotalTypeSensorStock.getSelectedItem().toString() + "'";
					new ClientSocket(requestType, jsonString, table);
					jsonString = ClientSocket.getJson();
					logger.log(Level.DEBUG, "Used/UnUsed Sensor finded");
					String jsonString_list[] = jsonString.replaceAll("\"", "").split(",");
					unused = Integer.parseInt(jsonString_list[0]);
					used = Integer.parseInt(jsonString_list[1]);
					lblStockSensor.setText(Integer.toString(unused));

					pieDataset.setValue("Unused", unused);
					pieDataset.setValue("Used", used);

					JFreeChart pieChart = ChartFactory.createPieChart("Ratio of unused sensors and used", pieDataset,
							true, true, true);
					pieChart.setBackgroundPaint(Color.GRAY);
					ChartPanel cpSensors = new ChartPanel(pieChart);
					cpSensors.setBounds(575, 21, 228, 140);
					panel.add(cpSensors);
					cpSensors.setMouseWheelEnabled(true);

				}
			}
		});

	}

////////////////////////////////////////////////////
	// Method of calculating of number of sensor for each floor
	public void getNumberStageSensor() {
		requestType = "COUNT";
		table = "Sensor";

		jsonString = "SELECT sum(case when ( id_partie_commune = 1 AND id_capteur != 0) then 1 else 0 end ) As nbSensorEtage1, sum(case when ( id_partie_commune = 0 AND id_capteur != 0) then 1 else 0 end ) As nbSensorRC FROM capteur";
		new ClientSocket(requestType, jsonString, table);
		jsonString = ClientSocket.getJson();
		logger.log(Level.DEBUG, "Number sensor for each floor finded");

		String jsonString_list[] = jsonString.replaceAll("\"", "").split(",");
		etage0 = jsonString_list[1];
		etage1 = jsonString_list[0];

		lblTotalStockRc.setText(etage0);
		lblTotalStockEtage1.setText((etage1));
	}

/////////////////////////////////////////////////////////////////

//////////////////////////////////////////////////////////
	// Method of calculating of average temperature of the sensors
	public void getAverageTemperature() {
		String s1 = "";
		String s2 = "";
		String s3 = "";
		String s4 = "";
//		String s5 = "";
		requestType = "COUNT";
		table = "Sensor";

		jsonString = "SELECT capteur.id_capteur, capteur.type_capteur, id_partie_commune, ROUND(AVG(message.seuil),2) FROM capteur INNER JOIN message ON capteur.id_capteur = message.id_capteur WHERE capteur.type_capteur = 'TEMPERATURE' GROUP BY capteur.id_capteur, capteur.id_partie_commune";
		new ClientSocket(requestType, jsonString, table);
		jsonString = ClientSocket.getJson();
		logger.log(Level.INFO, "Find AVERAGE TEMPERATURE FOR EACH STAGE");
//		System.out.println(jsonString);
		String jsonString_list[] = jsonString.replaceAll("\"", "").split(",");
		s1 = jsonString_list[0];
		s2 = jsonString_list[1];
		s3 = jsonString_list[2];
		s4 = jsonString_list[3];
//		s5 = jsonString_list[6];
//			System.out.println("capteur " + s1);
//			System.out.println("TYPE " + s2);
//			System.out.println("Etage "+ s3);
		System.out.println("Moyenne " + s4);
		lblMeanTemperatureStageRC.setText(s4 + "°");
//		tfMeanTemperatureStage1.setText(s5+ "°");

	}

	public void paintComponent(Graphics g) {
		try {
			BufferedImage backGroundImage = ImageIO.read(getClass().getClassLoader().getResource("background.jpg"));
			g.drawImage(backGroundImage, 0, 0, this.getWidth(), this.getHeight(), this);
		} catch (IOException e) {
			logger.log(Level.WARN, "Impossible to load the background" + e.getClass().getCanonicalName());
		}
	}
}

///////////////
