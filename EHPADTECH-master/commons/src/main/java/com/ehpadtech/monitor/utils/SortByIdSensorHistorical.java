package com.ehpadtech.monitor.utils;

import java.util.Comparator;
import com.ehpadtech.monitor.commons.entity.SensorHistorical;


public class SortByIdSensorHistorical implements Comparator<SensorHistorical> {
	/**
	 * Compare the id SensorHistorical
	 */
	@Override
	public int compare(SensorHistorical sH1, SensorHistorical sH2) {
		return sH1.getIdHistorical() - sH2.getIdHistorical();
	}
}
