package com.krishagni.catissueplus.core.common.service;

import com.krishagni.catissueplus.core.biospecimen.domain.Participant;


public interface MpiGenerator {

	public String generateMpi(Participant participant);
}
