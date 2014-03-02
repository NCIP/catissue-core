
package com.krishagni.catissueplus.core.biospecimen.repository;

import com.krishagni.catissueplus.core.common.repository.Dao;

import edu.wustl.catissuecore.domain.User;

public interface UserDao extends Dao<User> {

	User getUser(String witnessName);

}
