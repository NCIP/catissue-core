package com.krishagni.openspecimen.rde.repository;

import java.util.List;

import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.common.repository.Dao;
import com.krishagni.openspecimen.rde.domain.SpecimenCollectionSession;

public interface SpecimenCollSessionDao extends Dao<SpecimenCollectionSession> {
	public List<SpecimenCollectionSession> getSessions(User user);
}
