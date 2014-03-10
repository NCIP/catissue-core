package com.krishagni.catissueplus.core.administrative.services.impl;

import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.administrative.domain.factory.UserFactory;
import com.krishagni.catissueplus.core.administrative.events.CreateUserEvent;
import com.krishagni.catissueplus.core.administrative.events.DeleteUserEvent;
import com.krishagni.catissueplus.core.administrative.events.UpdateUserEvent;
import com.krishagni.catissueplus.core.administrative.events.UserCreatedEvent;
import com.krishagni.catissueplus.core.administrative.events.UserDeletedEvent;
import com.krishagni.catissueplus.core.administrative.events.UserDetails;
import com.krishagni.catissueplus.core.administrative.events.UserUpdatedEvent;
import com.krishagni.catissueplus.core.administrative.services.UserService;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.errors.CatissueException;

public class UserServiceImpl implements UserService {

	private DaoFactory daoFactory;
	
	private UserFactory userFactory;

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}
	
	public void setUserFactory(UserFactory userFactory) {
		this.userFactory = userFactory;
	}	

	@Override
	public UserCreatedEvent createUser(CreateUserEvent event) {
		try {
			User user = userFactory.createUser(event.getUserDetails());
			daoFactory.getUserDao().saveOrUpdate(user);
			return UserCreatedEvent.ok(UserDetails.fromDomain(user));
		}
		catch (CatissueException ce) {
			return UserCreatedEvent.invalidRequest(ce.getMessage() + " : " + ce.getErroneousFields());
		}
		catch (Exception e) {
			return UserCreatedEvent.serverError(e);
		}
	}

	@Override
	public UserUpdatedEvent updateUser(UpdateUserEvent event) {
		try {
			Long userId = event.getUserDetails().getId();
			User oldUser = daoFactory.getUserDao().getUser(userId);
			if (oldUser == null) {
				return UserUpdatedEvent.notFound(userId);
			}
			User user = userFactory.createUser(event.getUserDetails());
			oldUser.update(user);
			daoFactory.getUserDao().saveOrUpdate(oldUser);	
			return UserUpdatedEvent.ok(UserDetails.fromDomain(oldUser));
		
		} catch (CatissueException ce) {
			return UserUpdatedEvent.invalidRequest(ce.getMessage() + " : " + ce.getErroneousFields());
		} catch (Exception e) {
			return UserUpdatedEvent.serverError(e);
		}
	}
	
	@Override
	public UserDeletedEvent delete(DeleteUserEvent event) {
		try {
			User user = daoFactory.getUserDao().getUser(event.getId());
			if (user == null) {
				return UserDeletedEvent.notFound(event.getId());
			}
			user.delete();
			daoFactory.getUserDao().saveOrUpdate(user);
			return UserDeletedEvent.ok();
		}
		catch (CatissueException ce) {
			return UserDeletedEvent.invalidRequest(ce.getMessage() + " : " + ce.getErroneousFields());
		}
		catch (Exception e) {
			return UserDeletedEvent.serverError(e);
		}
	}

}
