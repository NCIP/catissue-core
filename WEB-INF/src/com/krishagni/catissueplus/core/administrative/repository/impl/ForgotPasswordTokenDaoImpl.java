package com.krishagni.catissueplus.core.administrative.repository.impl;

import com.krishagni.catissueplus.core.administrative.domain.ForgotPasswordToken;
import com.krishagni.catissueplus.core.administrative.repository.ForgotPasswordTokenDao;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;

public class ForgotPasswordTokenDaoImpl extends AbstractDao<ForgotPasswordToken> 
	implements ForgotPasswordTokenDao {
	
	private static final String FQN = ForgotPasswordToken.class.getName();
	
	private static final String FIND_BY_USER = FQN + ".findByUser";
	
	private static final String FIND_BY_TOKEN = FQN + ".findByToken";
	
	@Override
	public Class<?> getType() {
		return ForgotPasswordToken.class;
	}

	@Override
	public ForgotPasswordToken findByUser(Long userId) {
		return (ForgotPasswordToken) sessionFactory.getCurrentSession()
				.getNamedQuery(FIND_BY_USER)
				.setLong("userId", userId)
				.uniqueResult();
	}

	@Override
	public ForgotPasswordToken findByToken(String token) {
		return (ForgotPasswordToken) 
				sessionFactory.getCurrentSession()
				.getNamedQuery(FIND_BY_TOKEN)
				.setString("token", token)
				.uniqueResult();
	}
}
