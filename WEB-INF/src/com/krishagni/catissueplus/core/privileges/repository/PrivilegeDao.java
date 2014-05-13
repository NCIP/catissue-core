
package com.krishagni.catissueplus.core.privileges.repository;

import com.krishagni.catissueplus.core.common.repository.Dao;
import com.krishagni.catissueplus.core.privileges.domain.Privilege;

public interface PrivilegeDao extends Dao<Privilege> {

	public Privilege getPrivilegeByName(String privilegeName);

}
