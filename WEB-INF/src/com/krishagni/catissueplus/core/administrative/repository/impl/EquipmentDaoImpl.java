
package com.krishagni.catissueplus.core.administrative.repository.impl;

import java.util.List;

import org.hibernate.Query;

import com.krishagni.catissueplus.core.administrative.domain.Equipment;
import com.krishagni.catissueplus.core.administrative.repository.EquipmentDao;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;

public class EquipmentDaoImpl extends AbstractDao<Equipment> implements EquipmentDao {

	private static final String FQN = Equipment.class.getName();

	private static final String GET_EQUIPMENT_BY_DISPLAY_NAME = FQN + ".getEquipmentByDisplayName";

	private static final String GET_EQUIPMENT_BY_DEVICE_NAME = FQN + ".getEquipmentByDeviceName";

	private static final String GET_EQUIPMENT_BY_EQUIPMENT_ID = FQN + ".getEquipmentByEquipmentId";

	@Override
	public Equipment getEquipment(long id) {
		return (Equipment) sessionFactory.getCurrentSession().get(Equipment.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Equipment getEquipment(String displayName) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_EQUIPMENT_BY_DISPLAY_NAME);
		query.setString("displayName", displayName);
		List<Equipment> eqList = query.list();
		return eqList.isEmpty() ? null : eqList.get(0);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean isUniqueDisplayName(String displayName) {
		Query query = getSessionFactory().getCurrentSession().getNamedQuery(GET_EQUIPMENT_BY_DISPLAY_NAME);
		query.setString("displayName", displayName);
		List<Equipment> eqList = query.list();
		return eqList.isEmpty() ? true : false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean isUniqueDeviceName(String deviceName) {
		Query query = getSessionFactory().getCurrentSession().getNamedQuery(GET_EQUIPMENT_BY_DEVICE_NAME);
		query.setString("deviceName", deviceName);
		List<Equipment> eqList = query.list();
		return eqList.isEmpty() ? true : false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean isUniqueEquipmentId(String equipmentId) {
		Query query = getSessionFactory().getCurrentSession().getNamedQuery(GET_EQUIPMENT_BY_EQUIPMENT_ID);
		query.setString("equipmentId", equipmentId);
		List<Equipment> eqList = query.list();
		return eqList.isEmpty() ? true : false;
	}

}
