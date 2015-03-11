
package com.krishagni.catissueplus.core.common;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.krishagni.catissueplus.core.administrative.events.ListPvCriteria;
import com.krishagni.catissueplus.core.administrative.events.PvDetail;
import com.krishagni.catissueplus.core.administrative.services.PermissibleValueService;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class PermissibleValuesManagerImpl implements PermissibleValuesManager {

	//no autowired anywhere except controller 
	private PermissibleValueService permissibleValueSvc;

	public PermissibleValueService getPermissibleValueSvc() {
		return permissibleValueSvc;
	}

	public void setPermissibleValueSvc(PermissibleValueService permissibleValueSvc) {
		this.permissibleValueSvc = permissibleValueSvc;
	}

	@Override
	public List<PvDetail> getPermissibleValueList(String attribute) {
		ListPvCriteria crit = new ListPvCriteria().attribute(attribute);
		RequestEvent<ListPvCriteria> req = new RequestEvent<ListPvCriteria>(null, crit);
		ResponseEvent<List<PvDetail>> resp = permissibleValueSvc.getPermissibleValues(req);
		resp.throwErrorIfUnsuccessful();
		
		return resp.getPayload();
	}
	
	@Override
	public boolean validate(String attribute, String value) {
		return permissibleValueSvc.validate(attribute, Collections.singletonList(value));
	}
	
	@Override
	public boolean validate(String attribute, String[] values) {
		return permissibleValueSvc.validate(attribute, Arrays.asList(values));
	}

	@Override
	public boolean validate(String attribute, String parentValue, String value) {
		return permissibleValueSvc.validate(attribute, Collections.singletonList(value), parentValue);
	}
}
