
package com.krishagni.catissueplus.core.biospecimen.services;

import com.krishagni.catissueplus.core.biospecimen.events.VisitDetail;
import com.krishagni.catissueplus.core.common.events.EntityQueryCriteria;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public interface VisitService {
	public ResponseEvent<VisitDetail> getVisit(RequestEvent<EntityQueryCriteria> req);
	
	public ResponseEvent<VisitDetail> addVisit(RequestEvent<VisitDetail> req);
	
	public ResponseEvent<VisitDetail> updateVisit(RequestEvent<VisitDetail> req);
}
