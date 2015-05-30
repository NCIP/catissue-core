
package com.krishagni.catissueplus.core.biospecimen.services;

import java.util.List;

import com.krishagni.catissueplus.core.biospecimen.events.SprDetail;
import com.krishagni.catissueplus.core.biospecimen.events.VisitDetail;
import com.krishagni.catissueplus.core.biospecimen.events.VisitSpecimenDetail;
import com.krishagni.catissueplus.core.common.events.DependentEntityDetail;
import com.krishagni.catissueplus.core.common.events.EntityQueryCriteria;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public interface VisitService {
	public ResponseEvent<VisitDetail> getVisit(RequestEvent<EntityQueryCriteria> req);
	
	public ResponseEvent<VisitDetail> addOrUpdateVisit(RequestEvent<VisitDetail> req);
	
	public ResponseEvent<VisitDetail> patchVisit(RequestEvent<VisitDetail> req);
	
	public ResponseEvent<List<DependentEntityDetail>> getDependentEntities(RequestEvent<EntityQueryCriteria> req);
	
	public ResponseEvent<VisitDetail> deleteVisit(RequestEvent<EntityQueryCriteria> req);	
			
	public ResponseEvent<VisitSpecimenDetail> collectVisitAndSpecimens(RequestEvent<VisitSpecimenDetail> req);

	public ResponseEvent<String> uploadSpr(RequestEvent<SprDetail> req);
}
