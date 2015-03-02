package com.krishagni.catissueplus.core.administrative.services;

import java.util.List;
import java.util.Map;

import com.krishagni.catissueplus.core.administrative.events.InstituteDetail;
import com.krishagni.catissueplus.core.administrative.events.InstituteQueryCriteria;
import com.krishagni.catissueplus.core.administrative.repository.InstituteListCriteria;
import com.krishagni.catissueplus.core.common.events.DeleteEntityOp;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public interface InstituteService {

	public ResponseEvent<List<InstituteDetail>> getInstitutes(RequestEvent<InstituteListCriteria> req);

	public ResponseEvent<InstituteDetail> getInstitute(RequestEvent<InstituteQueryCriteria> req);
	
	public ResponseEvent<InstituteDetail> createInstitute(RequestEvent<InstituteDetail> req);

	public ResponseEvent<InstituteDetail> updateInstitute(RequestEvent<InstituteDetail> req);

	public ResponseEvent<Map<String, List>> deleteInstitute(RequestEvent<DeleteEntityOp> req);
}
