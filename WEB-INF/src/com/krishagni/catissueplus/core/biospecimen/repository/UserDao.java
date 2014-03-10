
package com.krishagni.catissueplus.core.biospecimen.repository;

import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.common.repository.Dao;

public interface UserDao extends Dao<User> {

	edu.wustl.catissuecore.domain.User getUser(String witnessName);
	
	User getUser(Long userId);
}
