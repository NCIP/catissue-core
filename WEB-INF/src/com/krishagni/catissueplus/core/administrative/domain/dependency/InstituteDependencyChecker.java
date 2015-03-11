package com.krishagni.catissueplus.core.administrative.domain.dependency;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

import com.krishagni.catissueplus.core.administrative.domain.Department;
import com.krishagni.catissueplus.core.administrative.domain.Institute;
import com.krishagni.catissueplus.core.administrative.domain.User;

public class InstituteDependencyChecker implements EntityDependencyChecker<Institute> {

	@Override
	public Map<String, List> getDependencies(Institute institute) {
		List<User> users = new ArrayList<User>();
		for(Department department : institute.getDepartments()) {
			if (CollectionUtils.isNotEmpty(department.getUsers())) {
				users.addAll(department.getUsers());
			}
		}
		
		Map<String, List> depedencies = new HashMap<String, List>();
		if (CollectionUtils.isNotEmpty(users)) {
			depedencies.put("Users", users); 
		}
		
		return depedencies;
	}

}
