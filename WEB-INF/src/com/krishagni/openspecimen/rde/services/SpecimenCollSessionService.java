package com.krishagni.openspecimen.rde.services;

import java.util.List;

import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.openspecimen.rde.events.SpecimenCollSessionDetail;

public interface SpecimenCollSessionService {

	public ResponseEvent<List<SpecimenCollSessionDetail>> getSessions(); 
	
	public ResponseEvent<SpecimenCollSessionDetail> createSession(RequestEvent<SpecimenCollSessionDetail> req);
	
	public ResponseEvent<SpecimenCollSessionDetail> updateSession(RequestEvent<SpecimenCollSessionDetail> req);
	
	public ResponseEvent<Boolean> deleteSession(RequestEvent<Long> req);
	
}
