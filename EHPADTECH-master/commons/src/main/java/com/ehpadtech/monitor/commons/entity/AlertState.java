package com.ehpadtech.monitor.commons.entity;


public enum AlertState {
	/*
	 * Different states of alert who can be used to define the state of sensors and
	 * commonArea if the electricity is down Normal => does'nt have any problem with
	 * the sensor Alert => the sensor is considerate in alert after treatment of
	 * messages Down => the sensor is breakdown they did'nt work correctly Over =>
	 * all sensors are breakdown or they have a great problem in building
	 */
	NORMAL, ALERT, DOWN, OVER, DELETED, CONFIGURED
}