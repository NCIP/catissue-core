package com.krishagni.catissueplus.core.biospecimen.services;

import java.util.List;

import com.krishagni.catissueplus.core.biospecimen.events.SpecimenTypeProps;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public interface SpecimenTypePropsService {
	public ResponseEvent<List<SpecimenTypeProps>> getProps();
}
