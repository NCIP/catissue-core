package com.krishagni.catissueplus.core.biospecimen.repository.impl;

import java.util.List;

import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenList;
import com.krishagni.catissueplus.core.biospecimen.repository.SpecimenListDao;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;

public class SpecimenListDaoImpl extends AbstractDao<SpecimenList> implements SpecimenListDao {
	private static final String FQN = SpecimenList.class.getName();
	
	private static final String GET_SPECIMEN_LISTS_BY_USER = FQN + ".getSpecimenListsByUser";

	@SuppressWarnings("unchecked")
	@Override
	public List<SpecimenList> getUserSpecimenLists(Long userId) {
		return sessionFactory.getCurrentSession()
				.getNamedQuery(GET_SPECIMEN_LISTS_BY_USER)
				.setLong("userId", userId)
				.list();
	}

	@Override
	public SpecimenList getSpecimenList(Long listId) {
		return (SpecimenList)sessionFactory.getCurrentSession()
				.get(SpecimenList.class, listId);
	}

	@Override
	public void deleteSpecimenList(SpecimenList list) {
		sessionFactory.getCurrentSession().delete(list);
	}
}
