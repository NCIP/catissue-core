
package com.krishagni.catissueplus.core.administrative.repository;

import com.krishagni.catissueplus.core.administrative.domain.Equipment;
import com.krishagni.catissueplus.core.common.repository.Dao;

public interface EquipmentDao extends Dao<Equipment> {

	Equipment getEquipment(long anyLong);

	Equipment getEquipment(String anyString);

	boolean isUniqueDisplayName(String displayName);

	boolean isUniqueDeviceName(String deviceName);

	boolean isUniqueEquipmentId(String equipmentId);

}
