
package com.krishagni.catissueplus.core.administrative.repository.impl;

import static com.krishagni.catissueplus.core.common.util.Utility.numberToLong;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.krishagni.catissueplus.core.administrative.domain.ForgotPasswordToken;
import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.administrative.repository.UserDao;
import com.krishagni.catissueplus.core.administrative.repository.UserListCriteria;
import com.krishagni.catissueplus.core.common.events.UserSummary;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;
import com.krishagni.catissueplus.core.common.util.Status;

public class UserDaoImpl extends AbstractDao<User> implements UserDao {
	
	@Override
	public Class<?> getType() {
		return User.class;
	}
	
	@SuppressWarnings("unchecked")
	public List<UserSummary> getUsers(UserListCriteria userCriteria) {
		Criteria criteria = sessionFactory.getCurrentSession()
				.createCriteria(User.class, "u")
				.add(Restrictions.ne("u.activityStatus", Status.ACTIVITY_STATUS_DISABLED.getStatus()))
				.setProjection(Projections.countDistinct("u.id"))
				.setFirstResult(userCriteria.startAt())
				.setMaxResults(userCriteria.maxResults())
				.addOrder(Order.asc("u.lastName"))
				.addOrder(Order.asc("u.firstName"));
		
		addSearchConditions(criteria, userCriteria);
		addProjectionFields(criteria);
		
		return getUsers(criteria.list());
	}
	
	@SuppressWarnings("unchecked")
	public List<User> getUsersByIds(List<Long> userIds) {
		return sessionFactory.getCurrentSession()
				.getNamedQuery(GET_USERS_BY_IDS)
				.setParameterList("userIds", userIds)
				.list();
	}

	public User getUser(String loginName, String domainName) {
		String hql = String.format(GET_USER_BY_LOGIN_NAME_HQL, " and activityStatus != 'Disabled'");
		List<User> users = executeGetUserByLoginNameHql(hql, loginName, domainName);
		return users.isEmpty() ? null : users.get(0);
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
	
	private List<UserSummary> getUsers(List<Object[]> rows) {
		List<UserSummary> result = new ArrayList<UserSummary>();
		for (Object[] row : rows) {			
			result.add(getUserSummary(row));			
		}
		
		return result;		
	}

	private UserSummary getUserSummary(Object[] row) {
		UserSummary userSummary = new UserSummary();
		userSummary.setId(numberToLong(row[0]));
		userSummary.setFirstName((String)row[1]);
		userSummary.setLastName((String)row[2]);
		userSummary.setLoginName((String)row[3]);
		return userSummary;		
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
	
	private void addSearchConditions(Criteria criteria, UserListCriteria userCriteria) {
		String searchString = userCriteria.query();
		
		if (StringUtils.isBlank(searchString)) {
			addNameRestriction(criteria, userCriteria.name());
			addLoginNameRestriction(criteria, userCriteria.loginName());
		} else {
			Criterion srchCond = Restrictions.disjunction()
					.add(Restrictions.ilike("u.firstName", searchString, MatchMode.ANYWHERE))
					.add(Restrictions.ilike("u.lastName", searchString, MatchMode.ANYWHERE))
					.add(Restrictions.ilike("u.loginName", searchString, MatchMode.ANYWHERE));
			
			criteria.add(srchCond);
		}
		
		addActivityStatusRestriction(criteria, userCriteria.activityStatus());
	}

	private void addNameRestriction(Criteria criteria, String name) {
		if (StringUtils.isBlank(name)) {
			return;
		}
		
		Criterion nameCond = Restrictions.disjunction()
				.add(Restrictions.ilike("u.firstName", name, MatchMode.ANYWHERE))
				.add(Restrictions.ilike("u.lastName", name, MatchMode.ANYWHERE));
		criteria.add(nameCond);
	}
	
	private void addLoginNameRestriction(Criteria criteria, String loginName) {
		if (StringUtils.isBlank(loginName)) {
			return;
		}
		
		Criterion loginNameCond = Restrictions.ilike("u.loginName", loginName, MatchMode.ANYWHERE);
		criteria.add(loginNameCond);
	}
	
	private void addActivityStatusRestriction(Criteria criteria, String activityStatus) {
		if (StringUtils.isBlank(activityStatus)) {
			return;
		}
		
		Criterion activityStatusCond = Restrictions.eq("u.activityStatus", activityStatus);
		criteria.add(activityStatusCond);
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
	
	private static final String GET_USER_BY_LOGIN_NAME_HQL = 
			"from com.krishagni.catissueplus.core.administrative.domain.User where loginName = :loginName and authDomain.name = :domainName  %s";
	
	private static final String GET_USER_BY_EMAIL_HQL = 
			"from com.krishagni.catissueplus.core.administrative.domain.User where emailAddress = :emailAddress %s";
	
	private static final String FQN = User.class.getName();

	private static final String GET_USERS_BY_IDS = FQN + ".getUsersByIds";
	
	private static final String TOKEN_FQN = ForgotPasswordToken.class.getName();
	
	private static final String GET_FP_TOKEN_BY_USER = TOKEN_FQN + ".getFpTokenByUser";
	
	private static final String GET_FP_TOKEN = TOKEN_FQN + ".getFpToken";

}
