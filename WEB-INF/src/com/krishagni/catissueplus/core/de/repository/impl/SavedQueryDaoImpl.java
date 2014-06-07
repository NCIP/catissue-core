package com.krishagni.catissueplus.core.de.repository.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;

import com.krishagni.catissueplus.core.common.events.UserSummary;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;
import com.krishagni.catissueplus.core.de.domain.SavedQuery;
import com.krishagni.catissueplus.core.de.events.SavedQuerySummary;
import com.krishagni.catissueplus.core.de.repository.SavedQueryDao;

public class SavedQueryDaoImpl extends AbstractDao<SavedQuery> implements SavedQueryDao {

	private static final String FQN = SavedQuery.class.getName();
	
	private static final String GET_SAVED_QUERIES_COUNT = FQN + ".getSavedQueriesCount";
	
	private static final String GET_SAVED_QUERIES = FQN + ".getSavedQueries";

	private static final String GET_QUERIES_BY_ID = FQN + ".getQueriesByIds";
	
	private static final String GET_QUERIES_COUNT_BY_FOLDER_ID = FQN + ".getQueriesCountByFolderId";
	
	private static final String GET_QUERIES_BY_FOLDER_ID = FQN + ".getQueriesByFolderId";
	
	private static final String IS_QUERY_SHARED_WITH_USER = FQN + ".isQuerySharedWithUser";
	
	@Override
	public Long getQueriesCount(Long userId) {
		return ((Number)sessionFactory.getCurrentSession()
			.getNamedQuery(GET_SAVED_QUERIES_COUNT)
			.setLong("userId", userId)
			.uniqueResult())
			.longValue();		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<SavedQuerySummary> getQueries(Long userId, int startAt, int maxRecords) {
		List<SavedQuerySummary> result = new ArrayList<SavedQuerySummary>();
		
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_SAVED_QUERIES);
		query.setLong("userId", userId).setFirstResult(startAt).setMaxResults(maxRecords).list();
		
		List<Object[]> rows = query.list();
		for (Object[] row : rows) {
			result.add(getSavedQuerySummary(row));
		}
		
		return result;		
	}
	
	@Override
	public SavedQuery getQuery(Long queryId) {
		return (SavedQuery)sessionFactory.getCurrentSession().get(SavedQuery.class, queryId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SavedQuery> getQueriesByIds(List<Long> queries) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_QUERIES_BY_ID);
		return query.setParameterList("queryIds", queries).list();
	}
	
	@Override
	public Long getQueriesCountByFolderId(Long folderId) {
		return ((Number)sessionFactory.getCurrentSession()
			.getNamedQuery(GET_QUERIES_COUNT_BY_FOLDER_ID)
			.setLong("folderId", folderId)
			.uniqueResult()).longValue();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SavedQuerySummary> getQueriesByFolderId(Long folderId, int startAt, int maxRecords) {
		List<SavedQuerySummary> result = new ArrayList<SavedQuerySummary>();
		
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_QUERIES_BY_FOLDER_ID);
		query.setLong("folderId", folderId);
		
		List<Object[]> rows = query.setFirstResult(startAt).setMaxResults(maxRecords).list();				
		for (Object[] row : rows) {			
			result.add(getSavedQuerySummary(row));			
		}
		
		return result;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean isQuerySharedWithUser(Long queryId, Long userId) {
		List<Number> count = sessionFactory.getCurrentSession()
			.getNamedQuery(IS_QUERY_SHARED_WITH_USER)
			.setLong("userId", userId)
			.setLong("queryId", queryId)
			.list();
		
		if (count == null || count.isEmpty()) {
			return false;
		}
		
		return count.iterator().next().intValue() != 0;
	}	
	
	private SavedQuerySummary getSavedQuerySummary(Object[] row) {
		SavedQuerySummary savedQuery = new SavedQuerySummary();
		savedQuery.setId((Long)row[0]);
		savedQuery.setTitle((String)row[1]);
		
		UserSummary createdBy = new UserSummary();
		createdBy.setId((Long)row[2]);
		createdBy.setFirstName((String)row[3]);
		createdBy.setLastName((String)row[4]);
		savedQuery.setCreatedBy(createdBy);
		
		UserSummary modifiedBy = new UserSummary();
		modifiedBy.setId((Long)row[5]);
		modifiedBy.setFirstName((String)row[6]);
		modifiedBy.setLastName((String)row[7]);
		savedQuery.setLastModifiedBy(modifiedBy);
		
		savedQuery.setLastModifiedOn((Date)row[8]);
		savedQuery.setLastRunCount((Long)row[9]);
		savedQuery.setLastRunOn((Date)row[10]);
		
		return savedQuery;		
	}
}