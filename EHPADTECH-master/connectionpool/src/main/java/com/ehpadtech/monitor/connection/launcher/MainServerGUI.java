package com.ehpadtech.monitor.connection.launcher;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.ehpadtech.monitor.connection.socket.Server;


public class MainServerGUI extends JFrame {

	/**
	 * Initialization of different parameters
	 */
	private static final long serialVersionUID = 1L;
	private String host;
	private int port;
	private static int nbServer = 0;
	private int hour = 0;
	private int minute = 0;
	private int seconde = 0;
	private ActionListener tache_timer;
	private Server ts;
	private JLabel time;
	private JPanel container;
	private JButton launch;
	private JButton launchFake;
	private JButton stop;
	private Timer timer1;
	private static final Logger logger = LogManager.getLogger(MainServerGUI.class);

	/**
	 * Main to open the Server
	 * 
	 */
	public MainServerGUI() {
		ResourceBundle rs = ResourceBundle.getBundle("config");
		this.host = rs.getString("server.host");
		this.port = Integer.parseInt(rs.getString("server.port"));

		time = new JLabel();
		container = new JPanel();
		container.setLayout(new GridLayout(2, 2));
		/**
		 * Create a button to launch the server
		 */
		ts = new Server(host, port);

		

		/**
		 * Button who invoke the normal method who give one connection to one socket
		 */
		launch = new JButton("Launch server");
		launch.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ts.load();
				if (nbServer == 0) {

					ts.open();
					nbServer++;
					logger.log(Level.INFO, "Server Initialized");

					/**
					 * Creation of Timer to know how many time the server was launch
					 */
					int delais = 1000;
					tache_timer = new ActionListener() {
						public void actionPerformed(ActionEvent e1) {
							seconde++;
							if (seconde == 60) {
								seconde = 0;
								minute++;
							}
							if (minute == 60) {
								minute = 0;
								hour++;
							}
							time.setText(hour + ":" + minute + ":" + seconde);
						}
					};

					timer1 = new Timer(delais, tache_timer);
					timer1.start();
					ts.treatment();
				} else {
					logger.log(Level.INFO, "Server already launch");
				}
			}
		});

		/**
		 * Button to invoke the method who create a socket accept who block all the
		 * connection but accept the socket
		 */
		launchFake = new JButton("Launch cheated server");
		launchFake.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ts.load();
				if (nbServer == 0) {

					ts.openFake();
					nbServer++;
					logger.log(Level.INFO, "Server Initialized");

					/**
					 * Creation of Timer to know how many time the server was launch
					 */
					int delais = 1000;
					tache_timer = new ActionListener() {
						public void actionPerformed(ActionEvent e1) {
							seconde++;
							if (seconde == 60) {
								seconde = 0;
								minute++;
							}
							if (minute == 60) {
								minute = 0;
								hour++;
							}
							time.setText(hour + ":" + minute + ":" + seconde);
						}
					};
					timer1 = new Timer(delais, tache_timer);
					timer1.start();
					ts.treatment();
				} else {
					logger.log(Level.INFO, "Server already launch");
				}
			}
		});

		/**
		 * Create a button to stop the server
		 */
		stop = new JButton("Shutdown server");
		stop.addActionListener(new ActionListener() {
			/**
			 * Stop the server, stop the timer and set the number to server launch to 0
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				if (nbServer > 0) {
					ts.close();
					timer1.stop();
					nbServer = 0;
					logger.log(Level.INFO, "Server Closed");
					logger.log(Level.INFO, "Application closed");
				}
			}
		});

		/**
		 * Add the buttons to container
		 */
		container.add(launch);
		container.add(launchFake);
		container.add(stop);
		container.add(time);

		/**
		 * Define the frame
		 */
		this.setContentPane(container);
		this.setTitle("Server");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setSize(300, 150);
	}

	/**
	 * Main to open the Server
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		MainServerGUI frame = new MainServerGUI();
		frame.setVisible(true);
	}
}