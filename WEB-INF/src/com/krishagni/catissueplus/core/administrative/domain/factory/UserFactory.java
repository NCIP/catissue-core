
package com.krishagni.catissueplus.core.administrative.domain.factory;

import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.administrative.events.UserDetail;

public interface UserFactory {
	public User createUser(UserDetail detail);
	
	public User createUser(User existing, UserDetail detail);
}
