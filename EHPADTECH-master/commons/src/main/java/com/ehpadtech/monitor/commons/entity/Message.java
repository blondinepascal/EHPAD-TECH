package com.ehpadtech.monitor.commons.entity;

import java.util.Date;


public class Message {

	/**
	 * Different parameters used
	 */
	private int idMessage = 0;
	private int threshold = 0;
	private int idSensor = 0;
	private String UUIDMessage = "";
	private Date alertDate = null;

	/**
	 * Constructor
	 * 
	 * @param idMessage
	 * @param threshold
	 * @param idSensor
	 * @param alertDate
	 */
	public Message(int idMessage, int threshold, int idSensor, Date alertDate) {
		this.idMessage = idMessage;
		this.threshold = threshold;
		this.idSensor = idSensor;
		this.alertDate = alertDate;

	}

	/**
	 * Constructor
	 */
	public Message() {
	}

	/**
	 * @return the idMessage
	 */
	public int getIdMessage() {
		return idMessage;
	}

	/**
	 * @param idMessage the idMessage to set
	 */
	public void setIdMessage(int idMessage) {
		this.idMessage = idMessage;
	}

	/**
	 * @return the threshold
	 */
	public int getThreshold() {
		return threshold;
	}

	/**
	 * @param threshold the threshold to set
	 */
	public void setThreshold(int threshold) {
		this.threshold = threshold;
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

	/**
	 * @return the uUIDMessage
	 */
	public String getUUIDMessage() {
		return UUIDMessage;
	}

	/**
	 * @param uUIDMessage the uUIDMessage to set
	 */
	public void setUUIDMessage(String uUIDMessage) {
		UUIDMessage = uUIDMessage;
	}
}