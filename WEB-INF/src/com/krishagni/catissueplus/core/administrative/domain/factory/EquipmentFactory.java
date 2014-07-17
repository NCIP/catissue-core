
package com.krishagni.catissueplus.core.administrative.domain.factory;

import com.krishagni.catissueplus.core.administrative.domain.Equipment;
import com.krishagni.catissueplus.core.administrative.events.EquipmentDetails;
import com.krishagni.catissueplus.core.administrative.events.EquipmentPatchDetails;

public interface EquipmentFactory {

	public Equipment create(EquipmentDetails details);

	public Equipment patch(Equipment oldEquipment, EquipmentPatchDetails details);

}
