package com.krishagni.catissueplus.core.administrative.domain.factory;

import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.administrative.events.UserDetails;
import com.krishagni.catissueplus.core.common.errors.ObjectCreationException;

public interface UserFactory {
	
	public User createUser(UserDetails details, ObjectCreationException exceptionHandler);
}
