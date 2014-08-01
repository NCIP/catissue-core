
package com.krishagni.catissueplus.core.common;

import java.util.Arrays;
import java.util.List;

import com.krishagni.catissueplus.core.administrative.events.AllPvsEvent;
import com.krishagni.catissueplus.core.administrative.events.GetAllPVsEvent;
import com.krishagni.catissueplus.core.administrative.events.PvInfo;
import com.krishagni.catissueplus.core.administrative.events.ValidatePvEvent;
import com.krishagni.catissueplus.core.administrative.services.PermissibleValueService;
import com.krishagni.catissueplus.core.common.events.EventStatus;

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
	public List<PvInfo> getPermissibleValueList(String attribute) {
		GetAllPVsEvent event = new GetAllPVsEvent();
		event.setAttribute(attribute);
		AllPvsEvent resp = permissibleValueSvc.getPermissibleValues(event);

		if (resp.getStatus() == EventStatus.OK) {
			return resp.getPvs();
		}
		return null;
	}
	@Override
	public boolean validate(String attribute, String value) {
		ValidatePvEvent event = new ValidatePvEvent();
		event.setAttribute(attribute);
		if(!value.isEmpty())
		event.setValues(Arrays.asList(value));
		return permissibleValueSvc.validate(event);
	}
	@Override
	public boolean validate(String attribute, String[] valuesArray) {
		ValidatePvEvent event = new ValidatePvEvent();
		event.setAttribute(attribute);
		event.setValues(Arrays.asList(valuesArray));
		return permissibleValueSvc.validate(event);
	}

	@Override
	public boolean validate(String attribute, String parentValue, String value) {
		ValidatePvEvent event = new ValidatePvEvent();
		event.setAttribute(attribute);
		event.setValues(Arrays.asList(value));
		return permissibleValueSvc.validate(event);
	}
}
