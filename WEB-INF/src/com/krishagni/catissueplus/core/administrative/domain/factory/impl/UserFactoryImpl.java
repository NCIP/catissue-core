package com.krishagni.catissueplus.core.administrative.domain.factory.impl;

import com.krishagni.catissueplus.core.administrative.domain.Address;
import com.krishagni.catissueplus.core.administrative.domain.Department;
import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.administrative.domain.factory.UserFactory;
import com.krishagni.catissueplus.core.administrative.events.UserDetails;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;

public class UserFactoryImpl implements UserFactory {

	private DaoFactory daoFactory;
	
	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}
	
	public User createUser(UserDetails details) {
		User user = new User();
		user.setId(details.getId());
		user.setLdapId(details.getLdapId());
		user.setStartDate(details.getStartDate());
		user.setEmailId(details.getEmailId());
		setLoginName(user, details);
		setName(user, details);
		setActivityStatus(user, details);
		setAddress(user, details);
		setDepartment(user, details);
		return user;
	}
	
	private void setName(User user, UserDetails details) {
		user.setFirstName(details.getFirstName());
		user.setLastName(details.getLastName());
	}
	
	private void setLoginName(User user, UserDetails details) {
		user.setLoginName(details.getLoginName());
	}

	private void setDepartment(User user, UserDetails details) {
		Department department = new Department();
		department.setId(details.getDepartmentId());
		user.setDepartment(department);
	}
	
	private void setAddress(User user, UserDetails details) {
		Address address = new Address();
		address.setId(details.getAddressId());
		user.setAddress(address);	
	}

	private void setActivityStatus(User user, UserDetails details) {
		user.setActivityStatus(details.getActivityStatus());
	}
}
