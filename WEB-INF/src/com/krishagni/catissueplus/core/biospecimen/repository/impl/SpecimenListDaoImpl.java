package com.krishagni.catissueplus.core.biospecimen.repository.impl;

import java.util.List;

import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenList;
import com.krishagni.catissueplus.core.biospecimen.repository.SpecimenListDao;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;

public class SpecimenListDaoImpl extends AbstractDao<SpecimenList> implements SpecimenListDao {
	private static final String FQN = SpecimenList.class.getName();
	
	private static final String GET_SPECIMEN_LISTS = FQN + ".getSpecimenLists";
	
	private static final String GET_SPECIMEN_LISTS_BY_USER = FQN + ".getSpecimenListsByUser";
	
	private static final String GET_SPECIMEN_LIST_BY_NAME = FQN + ".getSpecimenListByName";
	
	private static final String GET_LIST_SPECIMENS_COUNT = FQN + ".getListSpecimensCount";
	
	@SuppressWarnings("unchecked")
	@Override
	public List<SpecimenList> getSpecimenLists() {
		return sessionFactory.getCurrentSession()
				.getNamedQuery(GET_SPECIMEN_LISTS)
				.list();
	}

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
	@SuppressWarnings("unchecked")
	public SpecimenList getSpecimenListByName(String name) {
		List<SpecimenList> result = sessionFactory.getCurrentSession()
				.getNamedQuery(GET_SPECIMEN_LIST_BY_NAME)
				.setString("name", name)
				.list();
		
		return result.isEmpty() ? null : result.get(0);
	}

	@Override
	public SpecimenList getDefaultSpecimenList(Long userId) {
		return getSpecimenListByName(SpecimenList.getDefaultListName(userId));
	}

	@Override
	public Long getListSpecimensCount(Long listId) {
		return ((Number) sessionFactory.getCurrentSession()
				.getNamedQuery(GET_LIST_SPECIMENS_COUNT)
				.setLong("listId", listId)
				.uniqueResult()).longValue();
	}

	@Override
	public void deleteSpecimenList(SpecimenList list) {
		sessionFactory.getCurrentSession().delete(list);
	}
}
