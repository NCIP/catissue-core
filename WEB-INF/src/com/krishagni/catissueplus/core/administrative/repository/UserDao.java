
package com.krishagni.catissueplus.core.administrative.repository;

import java.util.List;

import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.common.repository.Dao;

public interface UserDao extends Dao<User> {

	edu.wustl.catissuecore.domain.User getUser(String witnessName);
	
	User getUser(Long userId);
	
	List<User> getAllUsers();
	
	Boolean isUniqueLoginName(String loginName);
}
