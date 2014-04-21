package com.krishagni.catissueplus.core.de.repository.impl;

import java.util.List;

import org.hibernate.Query;

import com.krishagni.catissueplus.core.common.repository.AbstractDao;
import com.krishagni.catissueplus.core.de.domain.SavedQuery;
import com.krishagni.catissueplus.core.de.repository.SavedQueryDao;

public class SavedQueryDaoImpl extends AbstractDao<SavedQuery> implements SavedQueryDao {

	private static final String FQN = SavedQuery.class.getName();
	
	private static final String GET_SAVED_QUERIES = FQN + ".getSavedQueries";

	@SuppressWarnings("unchecked")
	@Override
	public List<SavedQuery> getQueries(int startAt, int maxRecords) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_SAVED_QUERIES);
		return query.setFirstResult(startAt).setMaxResults(maxRecords).list();
	}

	@Override
	public SavedQuery getQuery(Long queryId) {
		return (SavedQuery)sessionFactory.getCurrentSession().get(SavedQuery.class, queryId);
	}
}
