
package com.krishagni.catissueplus.core.administrative.repository;

import com.krishagni.catissueplus.core.administrative.domain.Image;
import com.krishagni.catissueplus.core.common.repository.Dao;

public interface ImageDao extends Dao<Image> {

	public Image getImage(Long id);

	public Image getImage(String eqpImageId);

	public boolean isUniqueEquipmentImageId(String eqpImageId);

}
