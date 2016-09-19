
package com.krishagni.catissueplus.core.administrative.repository.impl;

import static com.krishagni.catissueplus.core.common.util.Utility.numberToLong;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.krishagni.catissueplus.core.administrative.domain.ForgotPasswordToken;
import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.administrative.repository.UserDao;
import com.krishagni.catissueplus.core.administrative.repository.UserListCriteria;
import com.krishagni.catissueplus.core.common.events.DependentEntityDetail;
import com.krishagni.catissueplus.core.common.events.UserSummary;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;

public class UserDaoImpl extends AbstractDao<User> implements UserDao {
	
	@Override
	public Class<?> getType() {
		return User.class;
	}
	
	@SuppressWarnings("unchecked")
	public List<UserSummary> getUsers(UserListCriteria listCrit) {
		Criteria query = getUsersListQuery(listCrit)
			.setFirstResult(listCrit.startAt())
			.setMaxResults(listCrit.maxResults())
			.addOrder(Order.asc("u.lastName"))
			.addOrder(Order.asc("u.firstName"));
		
		addProjectionFields(query);
		return getUsers(query.list(), listCrit);
	}
	
	public Long getUsersCount(UserListCriteria listCrit) {
		Number count = (Number) getUsersListQuery(listCrit)
			.setProjection(Projections.rowCount())
			.uniqueResult();
		return count.longValue();
	}

	public List<User> getUsersByIds(List<Long> userIds) {
		return getUsersByIdsAndInstitute(userIds, null);
	}
	
	@SuppressWarnings("unchecked")
	public List<User> getUsersByIdsAndInstitute(List<Long> userIds, Long instituteId) {
		Criteria criteria = sessionFactory.getCurrentSession()
				.createCriteria(User.class, "u")
				.add(Restrictions.in("u.id", userIds));
		
		if (instituteId != null) {
			criteria.createAlias("u.institute", "inst")
				.add(Restrictions.eq("inst.id", instituteId));
		}
		
		return criteria.list();
	}
	
	public User getUser(String loginName, String domainName) {
		String hql = String.format(GET_USER_BY_LOGIN_NAME_HQL, " and activityStatus != 'Disabled'");
		List<User> users = executeGetUserByLoginNameHql(hql, loginName, domainName);
		return users.isEmpty() ? null : users.get(0);
	}
	
	@Override
	public User getSystemUser() {
		return getUser(User.SYS_USER, User.DEFAULT_AUTH_DOMAIN);
	}
	
	public User getUserByEmailAddress(String emailAddress) {
		String hql = String.format(GET_USER_BY_EMAIL_HQL, " and activityStatus != 'Disabled'");
		List<User> users = executeGetUserByEmailAddressHql(hql, emailAddress);
		return users.isEmpty() ? null : users.get(0);
	}
	
	public Boolean isUniqueLoginName(String loginName, String domainName) {
		String hql = String.format(GET_USER_BY_LOGIN_NAME_HQL, "");
		List<User> users = executeGetUserByLoginNameHql(hql, loginName, domainName);
		return users.isEmpty();
	}
	
	public Boolean isUniqueEmailAddress(String emailAddress) {
		String hql = String.format(GET_USER_BY_EMAIL_HQL, "");
		List<User> users = executeGetUserByEmailAddressHql(hql, emailAddress);
		
		return users.isEmpty();
	}
	
	@SuppressWarnings("unchecked")
	public List<DependentEntityDetail> getDependentEntities(Long userId) {
		List<Object[]> rows = sessionFactory.getCurrentSession()
				.getNamedQuery(GET_DEPENDENT_ENTITIES)
				.setLong("userId", userId)
				.list();
		
		return getDependentEntities(rows);
	}

	@SuppressWarnings("unchecked")
	public ForgotPasswordToken getFpToken(String token) {
		List<ForgotPasswordToken> result = sessionFactory.getCurrentSession()
				.getNamedQuery(GET_FP_TOKEN)
				.setString("token", token)
				.list();
		
		return result.isEmpty() ? null : result.get(0);
	}
	
	@SuppressWarnings("unchecked")
	public ForgotPasswordToken getFpTokenByUser(Long userId) {
		List<ForgotPasswordToken> result = sessionFactory.getCurrentSession()
				.getNamedQuery(GET_FP_TOKEN_BY_USER)
				.setLong("userId", userId)
				.list();
		
		return result.isEmpty() ? null : result.get(0);
	}
	
	@Override
	public void saveFpToken(ForgotPasswordToken token) {
		sessionFactory.getCurrentSession().saveOrUpdate(token);
	};
	
	@Override
	public void deleteFpToken(ForgotPasswordToken token) {
		sessionFactory.getCurrentSession().delete(token);
	}

	private Criteria getUsersListQuery(UserListCriteria crit) {
		Criteria criteria = sessionFactory.getCurrentSession()
			.createCriteria(User.class, "u")
			.add( // not system user
				Restrictions.not(Restrictions.conjunction()
					.add(Restrictions.eq("u.loginName", User.SYS_USER))
					.add(Restrictions.eq("u.authDomain.name", User.DEFAULT_AUTH_DOMAIN))
				)
			);
		
		return addSearchConditions(criteria, crit);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<String> getActiveUsersEmailIds(Date startDate, Date endDate) {
		return sessionFactory.getCurrentSession()
			.getNamedQuery(GET_ACTIVE_USERS_EMAIL_IDS)
			.setTimestamp("startDate", startDate)
			.setTimestamp("endDate", endDate)
			.list();
	}

	private List<UserSummary> getUsers(List<Object[]> rows, UserListCriteria listCrit) {		
		Map<Long, UserSummary> userSummaryMap = new HashMap<Long, UserSummary>();

		List<UserSummary> result = new ArrayList<UserSummary>();
		for (Object[] row : rows) {
			UserSummary userSummary = getUserSummary(row);
			if (listCrit.includeStat()) {
				userSummaryMap.put(userSummary.getId(), userSummary);
			}
			
			result.add(userSummary);
		}

		if (!listCrit.includeStat() || result.isEmpty()) {
			return result;
		}

		List<Object[]> countRows  = getCpCount(userSummaryMap.keySet());
		for (Object[] row : countRows) {
			UserSummary userSummary = userSummaryMap.get((Long)row[0]);
			userSummary.setCpCount((Integer)row[1]);
		}
		
		return result;		
	}

	private UserSummary getUserSummary(Object[] row) {
		int idx = 0;

		UserSummary userSummary = new UserSummary();
		userSummary.setId(numberToLong(row[idx++]));
		userSummary.setFirstName((String)row[idx++]);
		userSummary.setLastName((String)row[idx++]);
		userSummary.setLoginName((String)row[idx++]);
		userSummary.setEmailAddress((String)row[idx++]);
		userSummary.setCreationDate((Date)row[idx++]);
		return userSummary;
	}

	@SuppressWarnings("unchecked")
	private List<Object[]> getCpCount(Set<Long> userIds) {
		return sessionFactory.getCurrentSession()
				.getNamedQuery(GET_CP_COUNT_BY_USERS)
				.setParameterList("userIds", userIds)
				.list();

	}

	@SuppressWarnings("unchecked")
	private List<User> executeGetUserByLoginNameHql(String hql, String loginName, String domainName) {
		return sessionFactory.getCurrentSession()
				.createQuery(hql)
				.setString("loginName", loginName)
				.setString("domainName", domainName)
				.list();
	}
	
	@SuppressWarnings("unchecked")
	private List<User> executeGetUserByEmailAddressHql(String hql, String emailAddress) {
		return sessionFactory.getCurrentSession()
				.createQuery(hql)
				.setString("emailAddress", emailAddress)
				.list();
	}
	
	private Criteria addSearchConditions(Criteria criteria, UserListCriteria listCrit) {
		String searchString = listCrit.query();
		
		if (StringUtils.isBlank(searchString)) {
			addNameRestriction(criteria, listCrit.name());
			addLoginNameRestriction(criteria, listCrit.loginName());
		} else {
			criteria.add(
				Restrictions.disjunction()
					.add(Restrictions.ilike("u.firstName", searchString, MatchMode.ANYWHERE))
					.add(Restrictions.ilike("u.lastName",  searchString, MatchMode.ANYWHERE))
					.add(Restrictions.ilike("u.loginName", searchString, MatchMode.ANYWHERE))
			);
		}
		
		addActivityStatusRestriction(criteria, listCrit.activityStatus());
		addInstituteRestriction(criteria, listCrit.instituteName());
		addDomainRestriction(criteria, listCrit.domainName());
		return criteria;
	}

	private void addNameRestriction(Criteria criteria, String name) {
		if (StringUtils.isBlank(name)) {
			return;
		}
		
		criteria.add(
			Restrictions.disjunction()
				.add(Restrictions.ilike("u.firstName", name, MatchMode.ANYWHERE))
				.add(Restrictions.ilike("u.lastName", name, MatchMode.ANYWHERE))
		);
	}
	
	private void addLoginNameRestriction(Criteria criteria, String loginName) {
		if (StringUtils.isBlank(loginName)) {
			return;
		}
		
		criteria.add(Restrictions.ilike("u.loginName", loginName, MatchMode.ANYWHERE));
	}
	
	private void addActivityStatusRestriction(Criteria criteria, String activityStatus) {
		if (StringUtils.isBlank(activityStatus)) {
			return;
		}
		
		criteria.add(Restrictions.eq("u.activityStatus", activityStatus));
	}
	
	private void addInstituteRestriction(Criteria criteria, String instituteName) {
		if (StringUtils.isBlank(instituteName)) {
			return;
		}
		
		criteria.createAlias("u.institute", "institute")
			.add(Restrictions.eq("institute.name", instituteName));
	}
	
	private void addDomainRestriction(Criteria criteria, String domainName) {
		if (StringUtils.isBlank(domainName)) {
			return;
		}
		
		criteria.createAlias("u.authDomain", "domain")
			.add(Restrictions.eq("domain.name", domainName));
	}

	private void addProjectionFields(Criteria criteria) {
		criteria.setProjection(Projections.distinct(
			Projections.projectionList()
				.add(Projections.property("u.id"), "id")
				.add(Projections.property("u.firstName"), "firstName")
				.add(Projections.property("u.lastName"), "lastName")
				.add(Projections.property("u.loginName"), "loginName")
				.add(Projections.property("u.emailAddress"), "emailAddress")
				.add(Projections.property("u.creationDate"), "creationDate")
		));
	}
	
	private List<DependentEntityDetail> getDependentEntities(List<Object[]> rows) {
		List<DependentEntityDetail> dependentEntities = new ArrayList<DependentEntityDetail>();
		
		for (Object[] row: rows) {
			String name = (String)row[0];
			int count = ((Number)row[1]).intValue();
			dependentEntities.add(DependentEntityDetail.from(name, count));
		}
		
		return dependentEntities;
 	}
	
	private static final String GET_USER_BY_LOGIN_NAME_HQL = 
			"from com.krishagni.catissueplus.core.administrative.domain.User where loginName = :loginName and authDomain.name = :domainName  %s";
	
	private static final String GET_USER_BY_EMAIL_HQL = 
			"from com.krishagni.catissueplus.core.administrative.domain.User where emailAddress = :emailAddress %s";
	
	private static final String FQN = User.class.getName();

	private static final String GET_DEPENDENT_ENTITIES = FQN + ".getDependentEntities";

	private static final String GET_CP_COUNT_BY_USERS = FQN + ".getCpCountByUsers";

	private static final String TOKEN_FQN = ForgotPasswordToken.class.getName();
	
	private static final String GET_FP_TOKEN_BY_USER = TOKEN_FQN + ".getFpTokenByUser";
	
	private static final String GET_FP_TOKEN = TOKEN_FQN + ".getFpToken";

	private static final String GET_ACTIVE_USERS_EMAIL_IDS = FQN + ".getActiveUsersEmailIds";
}
