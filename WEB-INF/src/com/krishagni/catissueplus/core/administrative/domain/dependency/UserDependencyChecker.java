package com.krishagni.catissueplus.core.administrative.domain.dependency;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;

public class UserDependencyChecker extends AbstractDependencyChecker<User> {
	private DaoFactory daoFactory;

	public DaoFactory getDaoFactory() {
		return daoFactory;
	}

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public List<Map<String, Object>> getDependentEntities(User user) {
		List<Map<String, Object>> dependencyStat = new ArrayList<Map<String, Object>>();
		
		//List<Object[]> stats = daoFactory.getUserDao().getUserDependentEntities(user.getId());
		//setDependentEntities(stats, dependencyStat); 
		
		return dependencyStat;
	}

}
