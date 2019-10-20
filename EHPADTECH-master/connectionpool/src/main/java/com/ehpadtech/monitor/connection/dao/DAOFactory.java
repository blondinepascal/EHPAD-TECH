package com.ehpadtech.monitor.connection.dao;

import java.sql.Connection;
import com.ehpadtech.monitor.commons.entity.CommonArea;
import com.ehpadtech.monitor.commons.entity.Employee;
import com.ehpadtech.monitor.commons.entity.Resident;
import com.ehpadtech.monitor.commons.entity.Sensor;
import com.ehpadtech.monitor.commons.entity.SensorHistorical;


public class DAOFactory extends AbstractDAOFactory {

	/**
	 * Initialization of parameters
	 */
	protected final Connection con = null;

	/**
	 * Return a CommonArea Object
	 */
	@Override
	public DAO<CommonArea> getCommonAreaDAO() {
		return new CommonAreaDAO(con);
	}

	/**
	 * Return a Employee Object
	 */
	@Override
	public DAO<Employee> getEmployeeDAO() {
		return new EmployeeDAO(con);
	}

	/**
	 * Return a Resident Object
	 */
	@Override
	public DAO<Resident> getResidentDAO() {
		return new ResidentDAO(con);
	}

	/**
	 * Return a Sensor Object
	 */
	@Override
	public DAO<Sensor> getSensorDAO() {
		return new SensorDAO(con);
	}

	/**
	 * Return a SensorHistorical Object
	 */
	@Override
	public DAO<SensorHistorical> getSensorHistoricalDAO() {
		return new SensorHistoricalDAO(con);
	}
}