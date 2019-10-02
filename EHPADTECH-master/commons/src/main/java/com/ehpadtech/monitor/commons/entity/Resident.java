package com.ehpadtech.monitor.commons.entity;


public class Resident {

	/**
	 * Different parameters used
	 */
	private int idResident = 0;
	private String lastnameResident = "";
	private String nameResident = "";

	/**
	 * Constructor
	 * 
	 * @param idResident
	 * @param lastnameResident
	 * @param nameResident
	 */
	public Resident(int idResident, String lastnameResident, String nameResident) {
		super();
		this.idResident = idResident;
		this.lastnameResident = lastnameResident;
		this.nameResident = nameResident;
	}

	/**
	 * Constructor
	 */
	public Resident() {
	}

	/**
	 * @return the idResident
	 */
	public int getIdResident() {
		return idResident;
	}

	/**
	 * @param idResident the idResident to set
	 */
	public void setIdResident(int idResident) {
		this.idResident = idResident;
	}

	/**
	 * @return the lastnameResident
	 */
	public String getLastnameResident() {
		return lastnameResident;
	}

	/**
	 * @param lastnameResident the lastnameResident to set
	 */
	public void setLastnameResident(String lastnameResident) {
		this.lastnameResident = lastnameResident;
	}

	/**
	 * @return the nameResident
	 */
	public String getNameResident() {
		return nameResident;
	}

	/**
	 * @param nameResident the nameResident to set
	 */
	public void setNameResident(String nameResident) {
		this.nameResident = nameResident;
	}
}