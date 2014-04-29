
package com.krishagni.catissueplus.core.administrative.repository;

import java.util.List;

import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.common.repository.Dao;

public interface UserDao extends Dao<User> {

	User getUser(String witnessName);

	User getUser(Long userId);
	
	User getUserByIdAndDomainName(Long userId, String domainName);

	Boolean isUniqueEmailAddress(String emailAddress);

	List<String> getOldPasswords(Long id);

	List<String> getOldPasswordsByLoginName(String loginName);
	
	Boolean isUniqueLoginNameInDomain(String loginName, String domainName);
	
	User getUserByLoginNameAndDomainName(String loginName, String domainName);

	List<User> getUsersById(List<Long> userIds);
}
