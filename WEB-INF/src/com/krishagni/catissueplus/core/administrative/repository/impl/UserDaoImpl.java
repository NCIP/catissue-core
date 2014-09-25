
package com.krishagni.catissueplus.core.administrative.repository.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

import com.krishagni.catissueplus.core.administrative.domain.Password;
import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.administrative.repository.UserDao;
import com.krishagni.catissueplus.core.common.events.UserSummary;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;
import com.krishagni.catissueplus.core.common.util.Status;

public class UserDaoImpl extends AbstractDao<User> implements UserDao {


	@Override
	public User getUser(Long id) {
		return (User) sessionFactory.getCurrentSession().get(User.class, id);
	}

	@Override
	public User getUser(String witnessName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean isUniqueEmailAddress(String emailAddress) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_USER_BY_EMAIL_ADDRESS);
		query.setString("emailAddress", emailAddress);	
		return query.list().isEmpty() ? true : false;
	}

	@Override
	@SuppressWarnings(value = {"unchecked"})
	public List<String> getOldPasswords(Long id) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_OLD_PASSWORD_BY_USER_ID);
		query.setLong("userId", id);
		return query.list();
	}

	@Override
	public Boolean isUniqueLoginNameInDomain(String loginName, String domainName) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_USER_BY_LOGIN_NAME_AND_DOMAIN);
		query.setString("loginName", loginName);
		query.setString("domainName", domainName);
		return query.list().isEmpty() ? true : false;
	}
	
	@Override
	@SuppressWarnings(value = {"unchecked"})
	public User getUserByLoginNameAndDomainName(String loginName, String domainName) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_USER_BY_LOGIN_NAME_AND_DOMAIN);
		query.setString("loginName", loginName);
		query.setString("domainName", domainName);
		List<User> result = query.list();
		if (!result.isEmpty()) {
			return result.get(0);
		}
		return null;
	}
	
	@Override
	@SuppressWarnings(value = {"unchecked"})
	public User getUserByIdAndDomainName(Long userId, String domainName) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_USER_BY_ID_AND_DOMAIN);
		query.setLong("userId", userId);
		query.setString("domainName", domainName);
		List<User> result = query.list();
		if (!result.isEmpty()) {
			return result.get(0);
		}
		return null;
	}

	@Override
	public List<User> getUsersById(List<Long> userIds) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_USERS_BY_ID);
		return query.setParameterList("userIds", userIds).list();
	}

	@Override
	public User getActiveUser(String loginId, String domainName) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_ACTIVE_USER);
		query.setString("loginName", loginId);
		query.setString("domainName", domainName);
		List<User> result = query.list();
		return result.isEmpty() ? null : result.get(0);
	}


	private static final String FQN = User.class.getName();

	private static final String GET_USER_BY_ID_AND_DOMAIN = FQN + ".getUserByIdAndDomain";
	
	private static final String GET_USER_BY_EMAIL_ADDRESS = FQN + ".getUserByEmailAddress";
	
	private static final String GET_USER_BY_LOGIN_NAME_AND_DOMAIN = FQN + ".getUser";

	private static final String GET_OLD_PASSWORD_BY_USER_ID = Password.class.getName() + ".getOldPasswordByUserId";

	private static final String GET_USERS_BY_ID = FQN + ".getUsersById";
	
	private static final String GET_ACTIVE_USER = FQN+ ".getActiveUser";

	@Override
	public List<UserSummary> getAllUsers(int startAt, int maxRecords, List<String> sortBy,
			String ... searchString) {
		Criteria criteria = sessionFactory.getCurrentSession()
				.createCriteria(User.class, "u")
				.add(Restrictions.or(
						Restrictions.eq("u.activityStatus", Status.ACTIVITY_STATUS_ACTIVE.getStatus()),
						Restrictions.eq("u.activityStatus", Status.ACTIVITY_STATUS_CLOSED.getStatus())))
				.setProjection(Projections.countDistinct("u.id"));
		
		addSearchConditions(criteria, searchString);
		addProjectionFields(criteria);
		
		for(String sort : sortBy ){
			if(sort.startsWith("-")){
				criteria.addOrder(Property.forName(sort.substring(1)).desc());
			} else {
				criteria.addOrder(Property.forName(sort).asc());
			}
		}

		addLimits(criteria, startAt, maxRecords);
		return getUsers(criteria);
	}

	@Override
	public Long getUsersCount(String ... searchString) {
		Criteria criteria = sessionFactory.getCurrentSession()
				.createCriteria(User.class, "u")
				
				.add(Restrictions.or(
						Restrictions.eq("u.activityStatus", Status.ACTIVITY_STATUS_ACTIVE.getStatus()),
						Restrictions.eq("u.activityStatus", Status.ACTIVITY_STATUS_CLOSED.getStatus())))
				.setProjection(Projections.countDistinct("u.id"));
		
		addSearchConditions(criteria, searchString);
		return ((Number)criteria.uniqueResult()).longValue();
	}
	
	private void addSearchConditions(Criteria criteria, String[] searchString) {
		if (searchString == null || searchString.length == 0 || StringUtils.isBlank(searchString[0])) {
			return;
		}
		
		Disjunction srchCond = Restrictions.disjunction();
		
		srchCond.add(Restrictions.or(
				Restrictions.or(
				Restrictions.ilike("u.firstName", searchString[0], MatchMode.ANYWHERE),
				Restrictions.ilike("u.lastName", searchString[0], MatchMode.ANYWHERE)),
				Restrictions.ilike("u.loginName", searchString[0], MatchMode.ANYWHERE)
				));
		criteria.add(srchCond);
	}
	
	private void addProjectionFields(Criteria criteria) {
		criteria.setProjection(Projections.distinct(
				Projections.projectionList()
					.add(Projections.property("u.id"), "id")
					.add(Projections.property("u.firstName"), "firstName")
					.add(Projections.property("u.lastName"), "lastName")
					.add(Projections.property("u.loginName"), "loginName")
		));		
	}
	
	private void addLimits(Criteria criteria, int start, int maxRecords) {
		criteria.setFirstResult(start <= 0 ? 0 : start);
		if (maxRecords > 0) {
			criteria.setMaxResults(maxRecords);
		}
	}
	
	@SuppressWarnings("unchecked")
	private List<UserSummary> getUsers(Criteria criteria) {
		List<UserSummary> result = new ArrayList<UserSummary>();
		List<Object[]> rows = criteria.list();				
		for (Object[] row : rows) {			
			result.add(getUserSummary(row));			
		}
		
		return result;		
	}

	private UserSummary getUserSummary(Object[] row) {
		UserSummary userSummary = new UserSummary();
		userSummary.setId((Long)row[0]);
		userSummary.setFirstName((String)row[1]);
		userSummary.setLastName((String)row[2]);
		userSummary.setLoginName((String)row[3]);
		return userSummary;		
	}

}
