package com.ehpadtech.monitor.commons.entity;

import java.util.Date;
import com.ehpadtech.monitor.commons.entity.AlertState;

public class Alert {

	/**
	 * Different parameters used
	 */
	private int idAlert = 0;
	private AlertState alertState = null;
	private int idSensor = 0;
	private Date alertDate = null;

	/**
	 * Constructor
	 * 
	 * @param idAlert
	 * @param alertState
	 * @param idSensor
	 * @param alertDate
	 */
	public Alert(int idAlert, AlertState alertState, int idSensor, Date alertDate) {
		this.idAlert = idAlert;
		this.alertState = alertState;
		this.idSensor = idSensor;
		this.alertDate = alertDate;
	}

	/**
	 * Constructor
	 */
	public Alert() {
	}

	/**
	 * @return the idAlert
	 */
	public int getIdAlert() {
		return idAlert;
	}

	/**
	 * @param idAlert the idAlert to set
	 */
	public void setIdAlert(int idAlert) {
		this.idAlert = idAlert;
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
	 * @return the alertDate
	 */
	public Date getAlertDate() {
		return alertDate;
	}

	/**
	 * @param alertDate the alertDate to set
	 */
	public void setAlertDate(Date alertDate) {
		this.alertDate = alertDate;
	}
}
