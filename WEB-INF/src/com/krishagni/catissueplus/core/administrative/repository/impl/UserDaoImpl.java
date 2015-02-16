
package com.krishagni.catissueplus.core.administrative.repository.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;

import com.krishagni.catissueplus.core.administrative.domain.ForgotPasswordToken;
import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.administrative.events.ListUserCriteria;
import com.krishagni.catissueplus.core.administrative.repository.UserDao;
import com.krishagni.catissueplus.core.common.events.UserSummary;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;

import  static com.krishagni.catissueplus.core.common.util.Util.numberToLong;

public class UserDaoImpl extends AbstractDao<User> implements UserDao {
	
	@Override
	public Class<?> getType() {
		return User.class;
	}

	public User getUser(String loginName, String domainName) {
		String hql = String.format(GET_USER_BY_LOGIN_NAME_HQL, " and activityStatus != 'Disabled'");
		List<User> users = executeGetUserByLoginNameHql(hql, loginName, domainName);
		return users.size() == 0 ? null : users.get(0);
	}
	
	public User getUserByEmailAddress(String emailAddress) {
		String hql = String.format(GET_USER_BY_EMAIL_HQL, " and activityStatus != 'Disabled'");
		List<User> users = executeGetUserByEmailAddressHql(hql, emailAddress);
		return users.size() == 0 ? null : users.get(0);
	}
	
	public Boolean isUniqueLoginName(String loginName, String domainName) {
		String hql = String.format(GET_USER_BY_LOGIN_NAME_HQL, "");
		List<User> users = executeGetUserByLoginNameHql(hql, loginName, domainName);
		return (users.size() == 0);
	}
	
	public Boolean isUniqueEmailAddress(String emailAddress) {
		String hql = String.format(GET_USER_BY_EMAIL_HQL, "");
		List<User> users = executeGetUserByEmailAddressHql(hql, emailAddress);
		
		return (users.size() == 0);
	}
	
	@SuppressWarnings("unchecked")
	public List<User> getUsersByIds(List<Long> userIds) {
		return sessionFactory.getCurrentSession()
				.getNamedQuery(GET_USERS_BY_IDS)
				.setParameterList("userIds", userIds)
				.list();
	}

	@SuppressWarnings("unchecked")
	public List<UserSummary> getUsers(ListUserCriteria criteria) {
		String sql = buildGetUsersSql(criteria.query());
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);
		
		if (StringUtils.isNotBlank(criteria.query())) {
			query.setString("search", criteria.query());
		}
		
		query.setFirstResult(criteria.startAt());
		query.setMaxResults(criteria.maxResults());
		
		return getUsers(query.list());
	}
	
	@Override
	public void saveFpToken(ForgotPasswordToken token) {
		sessionFactory.getCurrentSession().saveOrUpdate(token);
	};
	
	@Override
	public void deleteFpToken(ForgotPasswordToken token) {
		sessionFactory.getCurrentSession().delete(token);
	}
	
	@SuppressWarnings("unchecked")
	public ForgotPasswordToken getFpTokenByUser(Long userId) {
		List<ForgotPasswordToken> result = sessionFactory.getCurrentSession()
				.getNamedQuery(GET_FP_TOKEN_BY_USER)
				.setLong("userId", userId)
				.list();
		
		return result.size() == 0 ? null : result.get(0);
	}

	@SuppressWarnings("unchecked")
	public ForgotPasswordToken getFpToken(String token) {
		List<ForgotPasswordToken> result = sessionFactory.getCurrentSession()
				.getNamedQuery(GET_FP_TOKEN)
				.setString("token", token)
				.list();
		
		return result.size() == 0 ? null : result.get(0);
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
	
	private String buildGetUsersSql(String search) {
		String sql = GET_USERS_SQL;
		if (StringUtils.isNotBlank(search)) {
			sql = String.format(sql, "and (firstName like :search or lastName like :search or loginName :search)");
		} else {
			sql = String.format(sql, "");
		}
		
		return sql;
	}
	
	private static final String GET_USERS_SQL =  
			"select " + 
	        "  identifier as id, first_name as firstName, last_name as lastName, login_name as loginName " + 
			"from " + 
	        "  catissue_user " + 
			"where " + 
	        "  activity_status != 'Disabled' %s " +
			"order by " +
	        "  last_name, first_name ";
	
	private static final String GET_USER_BY_LOGIN_NAME_HQL = 
			"from User where loginName = :loginName and authDomain.name = :domainName  %s";
	
	private static final String GET_USER_BY_EMAIL_HQL = "from User where emailAddress = :emailAddress %s";
	
	private static final String FQN = User.class.getName();

	private static final String GET_USERS_BY_IDS = FQN + ".getUsersByIds";
	
	private static final String TOKEN_FQN = ForgotPasswordToken.class.getName();
	
	private static final String GET_FP_TOKEN_BY_USER = TOKEN_FQN + ".getFpTokenByUser";
	
	private static final String GET_FP_TOKEN = TOKEN_FQN + ".getFpToken";

}
