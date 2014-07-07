
package com.krishagni.catissueplus.core.biospecimen.repository.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenInfo;
import com.krishagni.catissueplus.core.biospecimen.repository.SpecimenDao;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;

public class SpecimenDaoImpl extends AbstractDao<Specimen> implements SpecimenDao {

	@Override
	@SuppressWarnings("unchecked")
	public List<SpecimenInfo> getSpecimensList(Long parentSpecimenId) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_SPECIMEN_DETAILS_DY_SCG_ID);
		query.setLong("parentSpecimenId", parentSpecimenId);
		List<Object[]> result = query.list();
		List<SpecimenInfo> specimensInfo = new ArrayList<SpecimenInfo>();
		for (Object[] object : result) {
			SpecimenInfo info = new SpecimenInfo();
			info.setId(Long.valueOf(object[0].toString()));
			info.setLabel(object[1].toString());
			info.setSpecimenType(object[3].toString());
			info.setSpecimenClass(object[4].toString());
			info.setCollectionStatus(object[5].toString());
			if (object[6] != null) {
				info.setRequirementLabel(object[6].toString());
			}
			specimensInfo.add(info);
		}
		return specimensInfo;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Long getScgId(Long specimenId) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_SCG_ID_BY_SPECIMEN_ID);
		query.setLong("specimenId", specimenId);

		List<Long> rows = query.list();
		if (rows != null && !rows.isEmpty()) {
			return rows.iterator().next();
		}
		else {
			return null;
		}
	}

	@Override
	public Specimen getSpecimen(Long id) {
		return (Specimen)sessionFactory.getCurrentSession().get(Specimen.class, id);
	}
	
	@Override
	public Specimen getSpecimenByLabel(String label) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_SPECIMEN_BY_LABEL);
		query.setString("label", label);
		List<Specimen> results = query.list();
		return results.isEmpty()? null: results.get(0);
	}
	
	@Override
	public boolean isLabelUnique(String label) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_SPECIMEN_BY_LABEL);
		query.setString("label", label);
		return query.list().isEmpty()?true:false;
	}

	@Override
	public boolean isBarcodeUnique(String barcode) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_SPECIMEN_ID_BY_BARCODE);
		query.setString("barcode", barcode);
		return query.list().isEmpty()?true:false;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Specimen> getSpecimensByLabel(List<String> labels) {
		return sessionFactory.getCurrentSession()
				.getNamedQuery(GET_SPECIMENS_BY_LABEL)
				.setParameterList("labels", labels)
				.list();
	}
	

	private static final String FQN = Specimen.class.getName();

	private static final String GET_SCG_ID_BY_SPECIMEN_ID = FQN + ".getScgIdBySpecimenId";

	private static final String GET_SPECIMEN_DETAILS_DY_SCG_ID = FQN + ".getSpecimenDetailsByScgId";
	
	private static final String GET_SPECIMEN_ID_BY_BARCODE = FQN +".getSpecimenIdByBarcode";
	
	private static final String GET_SPECIMEN_BY_LABEL = FQN +".getSpecimenByLabel";
	
	private static final String GET_SPECIMENS_BY_LABEL = FQN + ".getSpecimensByLabel";


}
