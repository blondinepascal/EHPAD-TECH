package com.ehpadtech.monitor.mocker.generator;

import java.util.Random;
import java.util.ResourceBundle;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.ehpadtech.monitor.commons.entity.Resident;
import com.ehpadtech.monitor.mocker.launcher.MockSocket;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GenerateResident {

	/**
	 * Different parameters used
	 */
	private int nbGeneration;
	private Random random;
	private Resident resident;
	private String table;
	private String requestType;
	private String jsonString;
	private ObjectMapper objectMapper;
	private ResourceBundle rsLastname = ResourceBundle.getBundle("lastname_resident_list");
	private ResourceBundle rsFirstname = ResourceBundle.getBundle("firstname_resident_list");
	private static final Logger logger = LogManager.getLogger(GenerateResident.class);

	/**
	 * Constructor
	 */
	public GenerateResident() {
	}

	/**
	 * Constructor
	 * 
	 * @param nbGeneration
	 */
	public GenerateResident(int nbGeneration) {
		this.nbGeneration = nbGeneration;
	}

	/**
	 * Generate an random resident and add it into data base
	 */
	public void generate() {
		for (int i = 0; i < nbGeneration; i++) {
			resident = new Resident();
			random = new Random();
			resident.setLastnameResident(rsLastname.getString(Integer.toString(random.nextInt(1000))));
			resident.setNameResident(rsFirstname.getString(Integer.toString(random.nextInt(1000))));

			requestType = "CREATE";
			table = "Resident";
			objectMapper = new ObjectMapper();
			try {
				jsonString = objectMapper.writeValueAsString(resident);
				new MockSocket(requestType, jsonString, table);
			} catch (Exception e1) {
				logger.log(Level.WARN, "Impossible to parse in JSON Resident data " + e1.getClass().getCanonicalName());
			}
		}
	}
}
