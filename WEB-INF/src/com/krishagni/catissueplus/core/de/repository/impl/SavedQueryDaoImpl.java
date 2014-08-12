package com.krishagni.catissueplus.core.de.repository.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.krishagni.catissueplus.core.common.events.UserSummary;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;
import com.krishagni.catissueplus.core.de.domain.SavedQuery;
import com.krishagni.catissueplus.core.de.events.SavedQuerySummary;
import com.krishagni.catissueplus.core.de.repository.SavedQueryDao;
import org.springframework.util.CollectionUtils;

public class SavedQueryDaoImpl extends AbstractDao<SavedQuery> implements SavedQueryDao {

	private static final String FQN = SavedQuery.class.getName();
	
	private static final String GET_QUERIES_BY_ID = FQN + ".getQueriesByIds";
		
	private static final String IS_QUERY_SHARED_WITH_USER = FQN + ".isQuerySharedWithUser";
	
	@Override
	public Long getQueriesCount(Long userId, String ... searchString) {		
		Criteria criteria = sessionFactory.getCurrentSession()
				.createCriteria(SavedQuery.class, "s")
				.createAlias("createdBy", "c")
				.createAlias("folders", "f", Criteria.LEFT_JOIN)
				.createAlias("f.sharedWith", "su", Criteria.LEFT_JOIN)
				.add(Restrictions.isNull("s.deletedOn"))
				.add(Restrictions.disjunction()
						.add(Restrictions.eq("f.sharedWithAll", true))
						.add(Restrictions.eq("c.id", userId))
						.add(Restrictions.eq("su.id", userId)))
				.setProjection(Projections.countDistinct("s.id"));
		
		addSearchConditions(criteria, searchString);
		return ((Number)criteria.uniqueResult()).longValue();
	}
	
	@Override
	public List<SavedQuerySummary> getQueries(Long userId, int startAt, int maxRecords, String ... searchString) {
		Criteria criteria = sessionFactory.getCurrentSession()
				.createCriteria(SavedQuery.class, "s")
				.createAlias("createdBy", "c")
				.createAlias("lastUpdatedBy", "m", Criteria.LEFT_JOIN)				
				.createAlias("folders", "f", Criteria.LEFT_JOIN)
				.createAlias("f.sharedWith", "su", Criteria.LEFT_JOIN)
				.add(Restrictions.isNull("s.deletedOn"))
				.add(Restrictions.disjunction()
						.add(Restrictions.eq("f.sharedWithAll", true))
						.add(Restrictions.eq("c.id", userId))
						.add(Restrictions.eq("su.id", userId)));
		
		addSearchConditions(criteria, searchString);
		addProjectionFields(criteria);
		criteria.addOrder(Order.desc("s.id"));
		addLimits(criteria, startAt, maxRecords);
		return getSavedQueries(criteria);
	}
	
	@Override
	public SavedQuery getQuery(Long queryId) {
		Criteria criteria = sessionFactory.getCurrentSession()
				.createCriteria(SavedQuery.class, "s")
				.add(Restrictions.eq("s.id", queryId))
				.add(Restrictions.isNull("s.deletedOn"));
		List<SavedQuery> queries = criteria.list();
		return CollectionUtils.isEmpty(queries) ? null : queries.iterator().next();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SavedQuery> getQueriesByIds(List<Long> queries) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_QUERIES_BY_ID);
		return query.setParameterList("queryIds", queries).list();
	}
	
	@Override
	public Long getQueriesCountByFolderId(Long folderId, String ... searchString) {
		Criteria criteria = sessionFactory.getCurrentSession()
				.createCriteria(SavedQuery.class, "s")
				.createAlias("folders", "f", Criteria.INNER_JOIN)
				.add(Restrictions.isNull("s.deletedOn"))
				.add(Restrictions.eq("f.id", folderId))
				.setProjection(Projections.countDistinct("s.id"));
		
		addSearchConditions(criteria, searchString);
		return ((Number)criteria.uniqueResult()).longValue();
	}

	@Override
	public List<SavedQuerySummary> getQueriesByFolderId(Long folderId, int startAt, int maxRecords, String ... searchString) {
		Criteria criteria = sessionFactory.getCurrentSession()
				.createCriteria(SavedQuery.class, "s")
				.createAlias("createdBy", "c")
				.createAlias("folders", "f", Criteria.INNER_JOIN)
				.createAlias("lastUpdatedBy", "m", Criteria.LEFT_JOIN)
				.add(Restrictions.isNull("s.deletedOn"))
				.add(Restrictions.eq("f.id", folderId));

		addSearchConditions(criteria, searchString);
		addProjectionFields(criteria);
		criteria.addOrder(Order.desc("s.id"));
		addLimits(criteria, startAt, maxRecords);
		return getSavedQueries(criteria);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean isQuerySharedWithUser(Long queryId, Long userId) {
		Criteria criteria = sessionFactory.getCurrentSession()
				.createCriteria(SavedQuery.class, "s")
				.createAlias("createdBy", "c")
				.createAlias("folders", "f", Criteria.INNER_JOIN)
				.createAlias("f.sharedWith", "su", Criteria.INNER_JOIN)
				.add(Restrictions.eq("s.id", queryId))
				.add(Restrictions.isNull("s.deletedOn"))
				.add(Restrictions.or(
						Restrictions.eq("su.id", userId), 
						Restrictions.eq("f.sharedWithAll", true)))
				.setProjection(Projections.count("s.id"));

		List<Number> count = criteria.list();
		if (CollectionUtils.isEmpty(count)) {
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
		
	private void addLimits(Criteria criteria, int start, int maxRecords) {
		criteria.setFirstResult(start <= 0 ? 0 : start);
		if (maxRecords > 0) {
			criteria.setMaxResults(maxRecords);
		}
	}
	
	private void addSearchConditions(Criteria criteria, String[] searchString) {
		if (searchString == null || searchString.length == 0 || StringUtils.isBlank(searchString[0])) {
			return;
		}
		
		Disjunction srchCond = Restrictions.disjunction();
		if (StringUtils.isNumeric(searchString[0])) {
			srchCond.add(Restrictions.eq("s.id", Long.parseLong(searchString[0])));
		}
		
		srchCond.add(Restrictions.ilike("s.title", searchString[0], MatchMode.ANYWHERE));
		criteria.add(srchCond);
	}
	
	private void addProjectionFields(Criteria criteria) {
		criteria.setProjection(Projections.distinct(
				Projections.projectionList()
					.add(Projections.property("s.id"), "id")
					.add(Projections.property("s.title"), "title")
					.add(Projections.property("c.id"), "cUserId")
					.add(Projections.property("c.firstName"), "cFirstName")
					.add(Projections.property("c.lastName"), "cLastName")
					.add(Projections.property("m.id"), "mUserId")
					.add(Projections.property("m.firstName"), "mFirstName")
					.add(Projections.property("m.lastName"), "mLastName")
					.add(Projections.property("lastUpdated"), "lastUpdated")
					.add(Projections.property("lastRunCount"), "lastRunCount")
					.add(Projections.property("lastRunOn"), "lastRunOn")
		));		
	}
	
	@SuppressWarnings("unchecked")
	private List<SavedQuerySummary> getSavedQueries(Criteria criteria) {
		List<SavedQuerySummary> result = new ArrayList<SavedQuerySummary>();
		List<Object[]> rows = criteria.list();				
		for (Object[] row : rows) {			
			result.add(getSavedQuerySummary(row));			
		}
		
		return result;		
	}
}