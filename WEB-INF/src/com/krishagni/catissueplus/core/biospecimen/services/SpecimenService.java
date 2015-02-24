
package com.krishagni.catissueplus.core.biospecimen.services;

import java.util.List;

import com.krishagni.catissueplus.core.biospecimen.events.SpecimenDetail;
import com.krishagni.catissueplus.core.common.events.EntityQueryCriteria;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public interface SpecimenService {
	public ResponseEvent<SpecimenDetail> getSpecimen(RequestEvent<EntityQueryCriteria> req);
	
	public ResponseEvent<SpecimenDetail> createSpecimen(RequestEvent<SpecimenDetail> req);
	
	public ResponseEvent<List<SpecimenDetail>> collectSpecimens(RequestEvent<List<SpecimenDetail>> req);
	
	public ResponseEvent<Boolean> doesSpecimenExists(RequestEvent<String> label);
}
