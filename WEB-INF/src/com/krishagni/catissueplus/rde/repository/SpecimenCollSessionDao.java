package com.krishagni.catissueplus.rde.repository;

import java.util.List;

import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.common.repository.Dao;
import com.krishagni.catissueplus.rde.domain.SpecimenCollectionSession;

public interface SpecimenCollSessionDao extends Dao<SpecimenCollectionSession> {
	public List<SpecimenCollectionSession> getSessions(User user);
}
