package com.krishagni.catissueplus.core.administrative.repository;

import com.krishagni.catissueplus.core.administrative.domain.ForgotPasswordToken;
import com.krishagni.catissueplus.core.common.repository.Dao;

public interface ForgotPasswordTokenDao extends Dao<ForgotPasswordToken> {
	public ForgotPasswordToken findByUser(Long userId);
	
	public ForgotPasswordToken findByToken(String token);
}
