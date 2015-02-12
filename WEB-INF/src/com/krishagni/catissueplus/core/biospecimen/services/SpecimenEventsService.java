package com.krishagni.catissueplus.core.biospecimen.services;

import java.util.List;

import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

import edu.common.dynamicextensions.napi.FormData;

public interface SpecimenEventsService {	
	public ResponseEvent<List<FormData>> saveSpecimenEvents(RequestEvent<List<FormData>> req);
}
