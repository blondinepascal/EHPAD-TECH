package com.ehpadtech.monitor.utils;

import java.util.Comparator;
import com.ehpadtech.monitor.commons.entity.CommonArea;


public class SortByIdCommonArea implements Comparator<CommonArea> {
	/**
	 * Compare the id CommonArea
	 */
	@Override
	public int compare(CommonArea cA1, CommonArea cA2) {
		return cA1.getIdCommonArea() - cA2.getIdCommonArea();
	}
}
