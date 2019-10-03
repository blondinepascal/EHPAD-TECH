package com.ehpadtech.monitor.commons.entity;

import java.util.Date;
import com.ehpadtech.monitor.commons.entity.AlertState;

public class SensorHistorical {

	/**
	 * Different parameters used
	 */
	private int idHistorical = 0;
	private Date date = null;
	private int idSensor = 0;
	private boolean sensorState = false;
	private AlertState alertState = null;

	/**
	 * Constructor
	 * 
	 * @param idHistorical
	 * @param date
	 * @param idSensor
	 * @param sensorState
	 * @param alertState
	 */
	public SensorHistorical(int idHistorical, Date date, int idSensor, boolean sensorState, AlertState alertState) {
		super();
		this.idHistorical = idHistorical;
		this.date = date;
		this.idSensor = idSensor;
		this.sensorState = sensorState;
		this.alertState = alertState;
	}

	/**
	 * Constructor
	 */
	public SensorHistorical() {
	}

	/**
	 * @return the idHistorical
	 */
	public int getIdHistorical() {
		return idHistorical;
	}

	/**
	 * @param idHistorical the idHistorical to set
	 */
	public void setIdHistorical(int idHistorical) {
		this.idHistorical = idHistorical;
	}

	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
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
}