package com.krishagni.catissueplus.core.administrative.services.impl;

import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.administrative.domain.factory.UserErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.UserFactory;
import com.krishagni.catissueplus.core.administrative.events.AllUsersEvent;
import com.krishagni.catissueplus.core.administrative.events.CreateUserEvent;
import com.krishagni.catissueplus.core.administrative.events.DeleteUserEvent;
import com.krishagni.catissueplus.core.administrative.events.ReqAllUsersEvent;
import com.krishagni.catissueplus.core.administrative.events.UpdateUserEvent;
import com.krishagni.catissueplus.core.administrative.events.UserCreatedEvent;
import com.krishagni.catissueplus.core.administrative.events.UserDeletedEvent;
import com.krishagni.catissueplus.core.administrative.events.UserDetails;
import com.krishagni.catissueplus.core.administrative.events.UserUpdatedEvent;
import com.krishagni.catissueplus.core.administrative.services.UserService;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.errors.CatissueException;
import com.krishagni.catissueplus.core.common.errors.ObjectCreationException;
import com.krishagni.catissueplus.core.common.errors.ObjectUpdationException;

public class UserServiceImpl implements UserService {

	private DaoFactory daoFactory;
	
	private UserFactory userFactory;
	
	private ObjectCreationException exceptionHandler;
	
	private final String LOGIN_NAME = "login name";

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}
	
	public void setUserFactory(UserFactory userFactory) {
		this.userFactory = userFactory;
	}	

	@Override
	@PlusTransactional
	public UserCreatedEvent createUser(CreateUserEvent event) {
		exceptionHandler = new ObjectUpdationException();
		try {
			User user = userFactory.createUser(event.getUserDetails(), exceptionHandler);
			ensureUniqueLoginName(user.getLoginName());
			exceptionHandler.checkErrorAndThrow();
			daoFactory.getUserDao().saveOrUpdate(user);
			return UserCreatedEvent.ok(UserDetails.fromDomain(user));
		}
		catch (ObjectCreationException ce) {
			return UserCreatedEvent.invalidRequest("Error while user creation", ce.getErroneousFields());
		}
		catch (Exception e) {
			return UserCreatedEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public UserUpdatedEvent updateUser(UpdateUserEvent event) {
		exceptionHandler = new ObjectUpdationException();
		try {
			Long userId = event.getUserDetails().getId();
			User oldUser = daoFactory.getUserDao().getUser(userId);
			if (oldUser == null) {
				return UserUpdatedEvent.notFound(userId);
			}
			User user = userFactory.createUser(event.getUserDetails(), exceptionHandler);
			validateLoginName(oldUser, user);
			exceptionHandler.checkErrorAndThrow();
			
			oldUser.update(user);
			daoFactory.getUserDao().saveOrUpdate(oldUser);	
			return UserUpdatedEvent.ok(UserDetails.fromDomain(oldUser));
		
		} catch (ObjectCreationException ce) {
			return UserUpdatedEvent.invalidRequest("Error while user updation", ce.getErroneousFields());
		} catch (Exception e) {
			return UserUpdatedEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
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

	@Override
	@PlusTransactional
	public AllUsersEvent getAllUsers(ReqAllUsersEvent req) {
		return AllUsersEvent.ok(daoFactory.getUserDao().getAllUsers());
	}
	
	private void ensureUniqueLoginName(String loginName) {
		if(!daoFactory.getUserDao().isUniqueLoginName(loginName)) {
			exceptionHandler.addError(UserErrorCode.DUPLICATE_LOGIN_NAME, LOGIN_NAME);
		}
	}

	private void validateLoginName(User oldUser, User newUser) {
		if(!oldUser.getLoginName().equals(newUser.getLoginName())) {
			exceptionHandler.addError(UserErrorCode.CHANGE_IN_LOGIN_NAME, LOGIN_NAME);
		}
	}

}
