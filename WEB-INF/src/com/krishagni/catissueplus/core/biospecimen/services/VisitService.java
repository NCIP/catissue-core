
package com.krishagni.catissueplus.core.biospecimen.services;

import com.krishagni.catissueplus.core.biospecimen.events.VisitDetail;
import com.krishagni.catissueplus.core.common.events.EntityQueryCriteria;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public interface VisitService {
	ResponseEvent<VisitDetail> getVisit(RequestEvent<EntityQueryCriteria> req);
	
	ResponseEvent<VisitDetail> addVisit(RequestEvent<VisitDetail> req);
}
