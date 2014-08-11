
package com.krishagni.catissueplus.core.biospecimen.repository.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenCollectionGroup;
import com.krishagni.catissueplus.core.biospecimen.repository.SpecimenCollectionGroupDao;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;

@Repository("specimenCollectionGroupDao")
public class SpecimenCollectionGroupDaoImpl extends AbstractDao<SpecimenCollectionGroup>
		implements
			SpecimenCollectionGroupDao {

	@Override
	public List<Specimen> getSpecimensList(Long scgId) {
		Object object = sessionFactory.getCurrentSession().get(SpecimenCollectionGroup.class.getName(), scgId);
		if (object == null) {
			return Collections.emptyList();
		}

		SpecimenCollectionGroup scg = (SpecimenCollectionGroup) object;
		return new ArrayList<Specimen>(scg.getSpecimenCollection());
	}

	@Override
	public boolean isNameUnique(String name) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_SCG_ID_BY_NAME);
		query.setString("name", name);
		return query.list().isEmpty() ? true : false;
	}

	@Override
	public boolean isBarcodeUnique(String barcode) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_SCG_ID_BY_BARCODE);
		query.setString("barcode", barcode);
		return query.list().isEmpty() ? true : false;
	}

	@Override
	public SpecimenCollectionGroup getscg(Long id) {
		return (SpecimenCollectionGroup)sessionFactory.getCurrentSession().get(SpecimenCollectionGroup.class, id);
	}

	private static final String FQN = SpecimenCollectionGroup.class.getName();

	private static final String GET_SCG_ID_BY_BARCODE = FQN + ".getScgIdByBarcode";

	private static final String GET_SCG_ID_BY_NAME = FQN + ".getScgIdByName";

}
