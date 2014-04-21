
package com.krishagni.catissueplus.core.administrative.repository;

import java.util.List;

import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.common.repository.Dao;

public interface UserDao extends Dao<User> {

	User getUser(String witnessName);

	User getUser(Long userId);

	Boolean isUniqueLoginName(String loginName);

	Boolean isUniqueEmailAddress(String emailAddress);

	List<String> getOldPasswords(Long id);

	List<String> getOldPasswordsByLoginName(String loginName);
	
	Boolean isValidLdapId(Long ldapId);

	User getUserByLoginName(String loginName);

}
