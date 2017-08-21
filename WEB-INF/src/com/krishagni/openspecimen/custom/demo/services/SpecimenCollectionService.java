package com.krishagni.openspecimen.custom.demo.services;

import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.openspecimen.custom.demo.events.SpecimenCollectionDetail;

public interface SpecimenCollectionService {
	public ResponseEvent<SpecimenCollectionDetail> collect(RequestEvent<SpecimenCollectionDetail> req);
}
