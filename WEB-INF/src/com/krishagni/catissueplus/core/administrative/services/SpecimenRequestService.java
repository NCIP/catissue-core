package com.krishagni.catissueplus.core.administrative.services;

import java.util.List;
import java.util.Map;

import com.krishagni.catissueplus.core.administrative.events.RequestListSpecimensDetail;
import com.krishagni.catissueplus.core.administrative.events.SpecimenRequestDetail;
import com.krishagni.catissueplus.core.administrative.events.SpecimenRequestSummary;
import com.krishagni.catissueplus.core.administrative.repository.SpecimenRequestListCriteria;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public interface SpecimenRequestService {
	public ResponseEvent<List<SpecimenRequestSummary>> getRequests(RequestEvent<SpecimenRequestListCriteria> req);

	public ResponseEvent<SpecimenRequestDetail> getRequest(RequestEvent<Long> req);

	public ResponseEvent<Map<String, Object>> getRequestFormData(RequestEvent<Long> req);

	public ResponseEvent<List<SpecimenRequestSummary>> createRequest(RequestEvent<RequestListSpecimensDetail> req);

	/**
	 * Mostly useful for UI
	 */
	public ResponseEvent<List<Long>> getFormIds(RequestEvent<Long> req);

	public ResponseEvent<Boolean> haveRequests(RequestEvent<SpecimenRequestListCriteria> req);
}
