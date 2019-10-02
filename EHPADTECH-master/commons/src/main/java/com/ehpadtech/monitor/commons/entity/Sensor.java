package com.ehpadtech.monitor.commons.entity;

import java.sql.Time;
import java.util.Date;


public class Sensor {

	/**
	 * Different parameters used
	 */
	private int idSensor = 0;
	private SensorType typeSensor = null;
	private boolean sensorState = false;
	private int idCommonArea = 0;
	private AlertState alertState = null;
	private Sensitivity maxSensitivity = null;
	private Time startActivity = null;
	private Time endActivity = null;
	private int thresholdMin = 0;
	private int thresholdMax = 0;
	private Date lastUpdate;
	private boolean configured = false;

	/**
	 * Constructor
	 * 
	 * @param idSensor
	 * @param typeSensor
	 * @param sensorState
	 * @param idCommonArea
	 * @param alertState
	 * @param maxSensitivity
	 * @param startActivity
	 * @param endActivity
	 * @param thresholdMin
	 * @param thresholdMax
	 * @param lastUpdate
	 * @param configured
	 */
	public Sensor(int idSensor, SensorType typeSensor, boolean sensorState, int idCommonArea, AlertState alertState, Sensitivity maxSensitivity, Time startActivity, Time endActivity, int thresholdMin, int thresholdMax, Date lastUpdate, boolean configured) {
		super();
		this.idSensor = idSensor;
		this.typeSensor = typeSensor;
		this.sensorState = sensorState;
		this.idCommonArea = idCommonArea;
		this.alertState = alertState;
		this.maxSensitivity = maxSensitivity;
		this.startActivity = startActivity;
		this.endActivity = endActivity;
		this.thresholdMin = thresholdMin;
		this.thresholdMax = thresholdMax;
		this.lastUpdate = lastUpdate;
		this.configured = configured;
	}

	/**
	 * Constructor
	 */
	public Sensor() {
	}

	/**
	 * @return the thresholdMin
	 */
	public int getThresholdMin() {
		return thresholdMin;
	}

	/**
	 * @param thresholdMin the thresholdMin to set
	 */
	public void setThresholdMin(int thresholdMin) {
		this.thresholdMin = thresholdMin;
	}

	/**
	 * @return the thresholdMax
	 */
	public int getThresholdMax() {
		return thresholdMax;
	}

	/**
	 * @param thresholdMax the thresholdMax to set
	 */
	public void setThresholdMax(int thresholdMax) {
		this.thresholdMax = thresholdMax;
	}

	/**
	 * @return the idSensor
	 */
	public int getIdSensor() {
		return idSensor;
	}

	/**
	 * @param idSensor the idSensor to set
	 */
	public void setIdSensor(int idSensor) {
		this.idSensor = idSensor;
	}

	/**
	 * @return the typeSensor
	 */
	public SensorType getTypeSensor() {
		return typeSensor;
	}

	/**
	 * @param typeSensor the typeSensor to set
	 */
	public void setTypeSensor(SensorType typeSensor) {
		this.typeSensor = typeSensor;
	}

	/**
	 * @return the sensorState
	 */
	public boolean getSensorState() {
		return sensorState;
	}

	/**
	 * @param sensorState the sensorState to set
	 */
	public void setSensorState(boolean sensorState) {
		this.sensorState = sensorState;
	}

	/**
	 * @return the idCommonArea
	 */
	public int getIdCommonArea() {
		return idCommonArea;
	}

	/**
	 * @param idCommonArea the idCommonArea to set
	 */
	public void setIdCommonArea(int idCommonArea) {
		this.idCommonArea = idCommonArea;
	}

	/**
	 * @return the alertState
	 */
	public AlertState getAlertState() {
		return alertState;
	}

	/**
	 * @param alertState the alertState to set
	 */
	public void setAlertState(AlertState alertState) {
		this.alertState = alertState;
	}

	/**
	 * @return the maxSensitivity
	 */
	public Sensitivity getSensitivity() {
		return maxSensitivity;
	}

	/**
	 * @param maxSensitivity the maxSensitivity to set
	 */
	public void setSensitivity(Sensitivity maxSensitivity) {
		this.maxSensitivity = maxSensitivity;
	}

	/**
	 * @return the startActivity
	 */
	public Time getStartActivity() {
		return startActivity;
	}

	/**
	 * @param startActivity the startActivity to set
	 */
	public void setStartActivity(Time startActivity) {
		this.startActivity = startActivity;
	}

	/**
	 * @return the endActivity
	 */
	public Time getEndActivity() {
		return endActivity;
	}

	/**
	 * @param endActivity the endActivity to set
	 */
	public void setEndActivity(Time endActivity) {
		this.endActivity = endActivity;
	}

	/**
	 * @return the lastUpdate
	 */
	public Date getLastUpdate() {
		return lastUpdate;
	}

	/**
	 * @param lastUpdate the lastUpdate to set
	 */
	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	/**
	 * @return the configured
	 */
	public boolean getConfigured() {
		return configured;
	}

	/**
	 * @param configured the configured to set
	 */
	public void setConfigured(boolean configured) {
		this.configured = configured;
	}
}