package com.krishagni.catissueplus.core.administrative.services;

import com.krishagni.catissueplus.core.administrative.events.AllUsersEvent;
import com.krishagni.catissueplus.core.administrative.events.CreateUserEvent;
import com.krishagni.catissueplus.core.administrative.events.DeleteUserEvent;
import com.krishagni.catissueplus.core.administrative.events.ReqAllUsersEvent;
import com.krishagni.catissueplus.core.administrative.events.UpdateUserEvent;
import com.krishagni.catissueplus.core.administrative.events.UserCreatedEvent;
import com.krishagni.catissueplus.core.administrative.events.UserDeletedEvent;
import com.krishagni.catissueplus.core.administrative.events.UserUpdatedEvent;

public interface UserService {
	
	public AllUsersEvent getAllUsers(ReqAllUsersEvent req);
	
	public UserCreatedEvent createUser(CreateUserEvent event);

	public UserUpdatedEvent updateUser(UpdateUserEvent event);
	
	public UserDeletedEvent delete(DeleteUserEvent event);

}
