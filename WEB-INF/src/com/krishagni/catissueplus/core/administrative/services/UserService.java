package com.krishagni.catissueplus.core.administrative.services;

import com.krishagni.catissueplus.core.administrative.events.CloseUserEvent;
import com.krishagni.catissueplus.core.administrative.events.CreateUserEvent;
import com.krishagni.catissueplus.core.administrative.events.PasswordUpdatedEvent;
import com.krishagni.catissueplus.core.administrative.events.UpdatePasswordEvent;
import com.krishagni.catissueplus.core.administrative.events.UpdateUserEvent;
import com.krishagni.catissueplus.core.administrative.events.UserClosedEvent;
import com.krishagni.catissueplus.core.administrative.events.UserCreatedEvent;
import com.krishagni.catissueplus.core.administrative.events.UserUpdatedEvent;

public interface UserService {
	
	public UserCreatedEvent createUser(CreateUserEvent event);

	public UserUpdatedEvent updateUser(UpdateUserEvent event);
	
	public UserClosedEvent closeUser(CloseUserEvent event);
	
	public PasswordUpdatedEvent resetPassword(UpdatePasswordEvent updatePasswordEvent);

	public PasswordUpdatedEvent setPassword(UpdatePasswordEvent updatePasswordEvent);

}
