package com.krishagni.openspecimen.custom.le.services;

import java.util.List;

import com.krishagni.catissueplus.core.biospecimen.events.VisitSpecimenDetail;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.openspecimen.custom.le.events.SpecimenAndFrozenEventDetail;

public interface SpecimenCollectionService {
	public ResponseEvent<List<VisitSpecimenDetail>> collectVisitsAndSpecimens(RequestEvent<List<VisitSpecimenDetail>> visitsAndSpecimens);
	
	public ResponseEvent<SpecimenAndFrozenEventDetail> collectChildSpecimens(RequestEvent<SpecimenAndFrozenEventDetail> req);
}
