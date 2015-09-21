package com.krishagni.catissueplus.core.biospecimen.repository.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;

import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenUnit;
import com.krishagni.catissueplus.core.biospecimen.repository.SpecimenUnitDao;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;

public class SpecimenUnitDaoImpl extends AbstractDao<SpecimenUnit> implements SpecimenUnitDao {
	
	@Override
	public Class<SpecimenUnit> getType() {
		return SpecimenUnit.class;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SpecimenUnit> listAll() {
		return getSessionFactory().getCurrentSession().getNamedQuery(GET_ALL).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public SpecimenUnit getByClassAndType(String specimenClass, String type) {
		Query query = null; 
		if (StringUtils.isBlank(type)) {
			query = getSessionFactory().getCurrentSession().getNamedQuery(GET_BY_CLASS)
					.setString("specimenClass", specimenClass);
		} else {
			query = getSessionFactory().getCurrentSession().getNamedQuery(GET_BY_TYPE)
					.setString("specimenClass", specimenClass)
					.setString("type", type);
		}
		
		List<SpecimenUnit> result = query.list();
		return CollectionUtils.isEmpty(result) ? null : result.iterator().next();
	} 	
	
	private static final String FQN = SpecimenUnit.class.getName();
	
	private static final String GET_ALL = FQN + ".getAll";
	
	private static final String GET_BY_CLASS = FQN + ".getByClass";
	
	private static final String GET_BY_TYPE = FQN + ".getByType";
}
