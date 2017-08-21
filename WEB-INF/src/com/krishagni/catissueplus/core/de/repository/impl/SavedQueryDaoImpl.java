package com.krishagni.catissueplus.core.de.repository.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import com.krishagni.catissueplus.core.common.events.UserSummary;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;
import com.krishagni.catissueplus.core.de.domain.SavedQuery;
import com.krishagni.catissueplus.core.de.events.SavedQuerySummary;
import com.krishagni.catissueplus.core.de.repository.SavedQueryDao;

import org.springframework.util.CollectionUtils;

public class SavedQueryDaoImpl extends AbstractDao<SavedQuery> implements SavedQueryDao {

	private static final String FQN = SavedQuery.class.getName();
	
	private static final String GET_QUERIES_BY_ID = FQN + ".getQueriesByIds";
		
	@Override
	public Long getQueriesCount(Long userId, String ... searchString) {		
		Criteria criteria = sessionFactory.getCurrentSession()
				.createCriteria(SavedQuery.class, "s")
				.createAlias("createdBy", "c")
				.createAlias("folders", "f", JoinType.LEFT_OUTER_JOIN)
				.createAlias("f.sharedWith", "su", JoinType.LEFT_OUTER_JOIN)
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
				.createAlias("lastUpdatedBy", "m", JoinType.LEFT_OUTER_JOIN)		
				.createAlias("folders", "f", JoinType.LEFT_OUTER_JOIN)
				.createAlias("f.sharedWith", "su", JoinType.LEFT_OUTER_JOIN)
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
	
	@SuppressWarnings("unchecked")
	@Override
	public SavedQuery getQuery(Long queryId) {
		List<SavedQuery> queries = sessionFactory.getCurrentSession()
				.createCriteria(SavedQuery.class, "s")
				.add(Restrictions.eq("s.id", queryId))
				.add(Restrictions.isNull("s.deletedOn"))
				.list();
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
				.createAlias("folders", "f", JoinType.INNER_JOIN)
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
				.createAlias("folders", "f", JoinType.INNER_JOIN)
				.createAlias("lastUpdatedBy", "m", JoinType.LEFT_OUTER_JOIN)
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
				.createAlias("folders", "f", JoinType.INNER_JOIN)
				.createAlias("f.sharedWith", "su", JoinType.INNER_JOIN)
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
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getQueryChangeLogDetails(String fileName) {
		List<Object[]> rows = sessionFactory.getCurrentSession()
				.getNamedQuery(GET_QUERY_ID_AND_MD5_SQL)
				.setString("fileName", fileName)
				.list();
		if (CollectionUtils.isEmpty(rows)) {
			return null;
		}
		
		Map<String, Object> result = new HashMap<String, Object>();
		Object[] row = rows.iterator().next();
		result.put("queryId", row[0]);
		result.put("md5Digest", row[1]);
		return result;
	}
	
	@Override
	public void insertQueryChangeLog(String fileName, String digest, String status, Long queryId) {
		sessionFactory.getCurrentSession()
				.getNamedQuery(INSERT_QUERY_CHANGE_LOG_SQL)
				.setString("fileName", fileName)
				.setString("md5Digest", digest)
				.setString("status", status)
				.setLong("queryId", queryId)
				.setTimestamp("executedOn", Calendar.getInstance().getTime())
				.executeUpdate();
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
	
	private static final String INSERT_QUERY_CHANGE_LOG_SQL = FQN + ".insertQueryChangeLog"; 
	
	private static final String GET_QUERY_ID_AND_MD5_SQL = FQN + ".getQueryIdAndDigest"; 
}