package com.krishagni.catissueplus.core.administrative.domain.factory;

import com.krishagni.catissueplus.core.administrative.domain.Institute;
import com.krishagni.catissueplus.core.administrative.events.InstituteDetails;


public interface InstituteFactory {

	public Institute createInstitute(InstituteDetails details);
		
}
	