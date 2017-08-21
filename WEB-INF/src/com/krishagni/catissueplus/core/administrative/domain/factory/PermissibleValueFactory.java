package com.krishagni.catissueplus.core.administrative.domain.factory;

import com.krishagni.catissueplus.core.administrative.domain.PermissibleValue;
import com.krishagni.catissueplus.core.administrative.events.PermissibleValueDetails;


public interface PermissibleValueFactory {
	
	public PermissibleValue createPermissibleValue(PermissibleValueDetails details);

}
