
package com.krishagni.catissueplus.core.administrative.repository;

import java.util.List;

import com.krishagni.catissueplus.core.administrative.domain.ForgotPasswordToken;
import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.administrative.events.ListUserCriteria;
import com.krishagni.catissueplus.core.common.events.UserSummary;
import com.krishagni.catissueplus.core.common.repository.Dao;

public interface UserDao extends Dao<User> {
	
	public User getUser(String loginName, String domain);
	
	public User getUserByEmailAddress(String emailAddress);
	
	public Boolean isUniqueLoginName(String loginName, String domainName);
	
	public Boolean isUniqueEmailAddress(String emailAddress);
	
	public List<User> getUsersByIds(List<Long> userIds);
	
	public List<UserSummary> getUsers(ListUserCriteria criteria);

	public void saveFpToken(ForgotPasswordToken token);
	
	public void deleteFpToken(ForgotPasswordToken token);
	
	public ForgotPasswordToken getFpTokenByUser(Long userId);
	
	public ForgotPasswordToken getFpToken(String token);
}
