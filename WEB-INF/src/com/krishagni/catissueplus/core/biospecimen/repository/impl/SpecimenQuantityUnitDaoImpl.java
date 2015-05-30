package com.krishagni.catissueplus.core.biospecimen.repository.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;

import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenQuantityUnit;
import com.krishagni.catissueplus.core.biospecimen.repository.SpecimenQuantityUnitDao;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;

public class SpecimenQuantityUnitDaoImpl extends AbstractDao<SpecimenQuantityUnit> implements SpecimenQuantityUnitDao {
	
	@Override
	public Class<SpecimenQuantityUnit> getType() {
		return SpecimenQuantityUnit.class;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SpecimenQuantityUnit> listAll() {
		return getSessionFactory().getCurrentSession().getNamedQuery(GET_ALL).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public SpecimenQuantityUnit getByClassAndType(String specimenClass, String type) {
		Query query = null; 
		if (StringUtils.isBlank(type)) {
			query = getSessionFactory().getCurrentSession().getNamedQuery(GET_BY_CLASS)
					.setString("specimenClass", specimenClass);
		} else {
			query = getSessionFactory().getCurrentSession().getNamedQuery(GET_BY_TYPE)
					.setString("specimenClass", specimenClass)
					.setString("type", type);
		}
		
		List<SpecimenQuantityUnit> result = query.list();
		return CollectionUtils.isEmpty(result) ? null : result.iterator().next();
	} 	
	
	private static final String FQN = SpecimenQuantityUnit.class.getName();
	
	private static final String GET_ALL = FQN + ".getAll";
	
	private static final String GET_BY_CLASS = FQN + ".getByClass";
	
	private static final String GET_BY_TYPE = FQN + ".getByType";
}
