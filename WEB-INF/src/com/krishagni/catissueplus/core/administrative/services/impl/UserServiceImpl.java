package com.krishagni.catissueplus.core.administrative.services.impl;

import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.administrative.domain.factory.UserErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.UserFactory;
import com.krishagni.catissueplus.core.administrative.events.CloseUserEvent;
import com.krishagni.catissueplus.core.administrative.events.CreateUserEvent;
import com.krishagni.catissueplus.core.administrative.events.UpdateUserEvent;
import com.krishagni.catissueplus.core.administrative.events.UserClosedEvent;
import com.krishagni.catissueplus.core.administrative.events.UserCreatedEvent;
import com.krishagni.catissueplus.core.administrative.events.UserDetails;
import com.krishagni.catissueplus.core.administrative.events.UserUpdatedEvent;
import com.krishagni.catissueplus.core.administrative.services.UserService;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.errors.ObjectCreationException;
import com.krishagni.catissueplus.core.common.errors.ObjectUpdationException;

public class UserServiceImpl implements UserService {

	private DaoFactory daoFactory;
	
	private UserFactory userFactory;
	
	private ObjectCreationException exceptionHandler;
	
	private final String LOGIN_NAME = "login name";
	
	private final String EMAIL_ADDRESS = "email address";

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
			ensureUniqueEmailAddress(user.getEmailAddress());
			exceptionHandler.checkErrorAndThrow();
			daoFactory.getUserDao().saveOrUpdate(user);
			return UserCreatedEvent.ok(UserDetails.fromDomain(user));
		}
		catch (ObjectCreationException ce) {
			return UserCreatedEvent.invalidRequest(UserErrorCode.ERROR_WHILE_USER_CREATION.message(), ce.getErroneousFields());
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
			validateFields(oldUser, user);
			exceptionHandler.checkErrorAndThrow();
			
			oldUser.update(user);
			daoFactory.getUserDao().saveOrUpdate(oldUser);	
			return UserUpdatedEvent.ok(UserDetails.fromDomain(oldUser));
		
		}
		catch (ObjectUpdationException ce) {
			return UserUpdatedEvent.invalidRequest(UserErrorCode.ERROR_WHILE_USER_UPDATION.message(), ce.getErroneousFields());
		} 
		catch (Exception e) {
			return UserUpdatedEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public UserClosedEvent closeUser(CloseUserEvent event) {
		try {
			User oldUser = daoFactory.getUserDao().getUser(event.getId());
			if (oldUser == null) {
				return UserClosedEvent.notFound(event.getId());
			}
			oldUser.close();
			daoFactory.getUserDao().saveOrUpdate(oldUser);
			return UserClosedEvent.ok();
		}
		catch (Exception e) {
			return UserClosedEvent.serverError(e);
		}
	}
	
	private void ensureUniqueLoginName(String loginName) {
		if(!daoFactory.getUserDao().isUniqueLoginName(loginName)) {
			exceptionHandler.addError(UserErrorCode.DUPLICATE_LOGIN_NAME, LOGIN_NAME);
		}
	}

	private void validateFields(User oldUser, User newUser) {
		if(!oldUser.getLoginName().equals(newUser.getLoginName())) {
			exceptionHandler.addError(UserErrorCode.CHANGE_IN_LOGIN_NAME, LOGIN_NAME);
		} else if (!oldUser.getEmailAddress().equals(newUser.getEmailAddress())) {
			ensureUniqueEmailAddress(newUser.getEmailAddress());
		}
	}
	
	private void ensureUniqueEmailAddress(String emailAddress) {
		if(!daoFactory.getUserDao().isUniqueEmailAddress(emailAddress)) {
			exceptionHandler.addError(UserErrorCode.DUPLICATE_EMAIL_ADDRESS, EMAIL_ADDRESS);
		}	
	}
}
