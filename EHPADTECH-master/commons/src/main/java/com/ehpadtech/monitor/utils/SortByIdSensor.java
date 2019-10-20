package com.ehpadtech.monitor.utils;

import java.util.Comparator;
import com.ehpadtech.monitor.commons.entity.Sensor;


public class SortByIdSensor implements Comparator<Sensor> {
	/**
	 * Compare the id Sensor
	 */
	@Override
	public int compare(Sensor s1, Sensor s2) {
		return s1.getIdSensor() - s2.getIdSensor();
	}
}
