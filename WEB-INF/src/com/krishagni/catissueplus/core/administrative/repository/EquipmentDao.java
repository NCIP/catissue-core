
package com.krishagni.catissueplus.core.administrative.repository;

import java.util.List;

import com.krishagni.catissueplus.core.administrative.domain.Equipment;
import com.krishagni.catissueplus.core.common.repository.Dao;

public interface EquipmentDao extends Dao<Equipment> {

	public Equipment getEquipment(long anyLong);

	public Equipment getEquipment(String displayName);

	public boolean isUniqueDisplayName(String displayName);

	public boolean isUniqueDeviceName(String deviceName);

	public boolean isUniqueEquipmentId(String equipmentId);

	public List<Equipment> getAllEquipments(int maxResults);

}
