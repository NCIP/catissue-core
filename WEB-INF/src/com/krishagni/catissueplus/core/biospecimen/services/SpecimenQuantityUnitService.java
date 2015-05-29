package com.krishagni.catissueplus.core.biospecimen.services;

import java.util.List;

import com.krishagni.catissueplus.core.biospecimen.events.SpecimenQuantityUnitDetail;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public interface SpecimenQuantityUnitService {
	public ResponseEvent<List<SpecimenQuantityUnitDetail>> getQuantityUnits();
	
	public ResponseEvent<SpecimenQuantityUnitDetail> saveOrUpdate(RequestEvent<SpecimenQuantityUnitDetail> req);
}
