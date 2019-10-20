package com.ehpadtech.monitor.commons.entity;

import java.util.ArrayList;
import java.util.List;
import com.ehpadtech.monitor.commons.entity.Sensor;


public class CommonArea {

	/**
	 * Different parameters used
	 */
	private int idCommonArea = 0;
	private String nameCommonArea = "";
	private int floorCommonArea = 0;
	private int maxSensor = 0;
	private int area = 0;
	private List<Sensor> listSensor = new ArrayList<Sensor>();

	/**
	 * Constructor
	 * 
	 * @param idCommonArea
	 * @param nameCommonArea
	 * @param floorCommonArea
	 * @param area
	 * @param maxSensor
	 */
	public CommonArea(int idCommonArea, String nameCommonArea, int floorCommonArea, int area, int maxSensor) {
		super();
		this.idCommonArea = idCommonArea;
		this.nameCommonArea = nameCommonArea;
		this.floorCommonArea = floorCommonArea;
		this.area = area;
		this.maxSensor = maxSensor;
	}

	/**
	 * Constructor
	 */
	public CommonArea() {
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
	 * @return the nameCommonArea
	 */
	public String getNameCommonArea() {
		return nameCommonArea;
	}

	/**
	 * @param nameCommonArea the nameCommonArea to set
	 */
	public void setNameCommonArea(String nameCommonArea) {
		this.nameCommonArea = nameCommonArea;
	}

	/**
	 * @return the FloorCommonArea
	 */
	public int getFloorCommonArea() {
		return floorCommonArea;
	}

	/**
	 * @param FloorCommonArea the etageCommonArea to set
	 */
	public void setFloorCommonArea(int floorCommonArea) {
		this.floorCommonArea = floorCommonArea;
	}

	/**
	 * @return the listSensor
	 */
	public List<Sensor> getListSensor() {
		return listSensor;
	}

	/**
	 * @param listSensor the listSensor to set
	 */
	public void setListSensor(List<Sensor> listSensor) {
		this.listSensor = listSensor;
	}

	/**
	 * @param Sensor the listSensor to add
	 */
	public void addSensor(Sensor sensor) {
		this.listSensor.add(sensor);
	}

	/**
	 * @param Sensor the listSensor to add
	 */
	public void removeSensor(Sensor sensor) {
		this.listSensor.remove(sensor);
	}

	/**
	 * @return the maxSensor
	 */
	public int getMaxSensor() {
		return maxSensor;
	}

	/**
	 * @param maxSensor the maxSensor to set
	 */
	public void setMaxSensor(int maxSensor) {
		this.maxSensor = maxSensor;
	}

	/**
	 * @return the area
	 */
	public int getArea() {
		return area;
	}

	/**
	 * @param area the area to set
	 */
	public void setArea(int area) {
		this.area = area;
	}

	@Override
	public String toString() {
		return "CommonArea [idCommonArea=" + idCommonArea + ", nameCommonArea=" + nameCommonArea + ", floorCommonArea="
				+ floorCommonArea + ", maxSensor=" + maxSensor + ", area=" + area + "]";
	}
	
	
}