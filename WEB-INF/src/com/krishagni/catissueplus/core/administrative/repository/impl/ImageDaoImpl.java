
package com.krishagni.catissueplus.core.administrative.repository.impl;

import java.util.List;

import org.hibernate.Query;

import com.krishagni.catissueplus.core.administrative.domain.Equipment;
import com.krishagni.catissueplus.core.administrative.domain.Image;
import com.krishagni.catissueplus.core.administrative.repository.ImageDao;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;

public class ImageDaoImpl extends AbstractDao<Image> implements ImageDao {

	private static final String GET_EQUIPMENT_IMAGE_ID = Image.class.getName() + ".getEquipmentImageId";

	@Override
	public Image getImage(Long id) {
		return (Image) sessionFactory.getCurrentSession().get(Image.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean isUniqueEquipmentImageId(String eqpImageId) {
		Query query = getSessionFactory().getCurrentSession().getNamedQuery(GET_EQUIPMENT_IMAGE_ID);
		query.setString("eqpImageId", eqpImageId);
		List<Equipment> imgList = query.list();
		return imgList.isEmpty() ? true : false;
	}

}
