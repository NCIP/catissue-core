
package com.krishagni.catissueplus.core.biospecimen.domain.factory;

import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenCollectionGroup;
import com.krishagni.catissueplus.core.biospecimen.events.ScgReportDetail;
import com.krishagni.catissueplus.core.biospecimen.events.VisitDetail;

public interface VisitFactory {
	public SpecimenCollectionGroup createVisit(VisitDetail scgDetail);

	public SpecimenCollectionGroup updateReports(SpecimenCollectionGroup oldScg, ScgReportDetail detail);
}
