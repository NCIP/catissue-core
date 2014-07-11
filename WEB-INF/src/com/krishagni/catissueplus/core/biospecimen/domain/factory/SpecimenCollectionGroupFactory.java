
package com.krishagni.catissueplus.core.biospecimen.domain.factory;

import java.util.Map;

import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenCollectionGroup;
import com.krishagni.catissueplus.core.biospecimen.events.ScgDetail;
import com.krishagni.catissueplus.core.biospecimen.events.ScgReportDetail;

public interface SpecimenCollectionGroupFactory {

	public SpecimenCollectionGroup createScg(ScgDetail scgDetail);

	public SpecimenCollectionGroup patch(SpecimenCollectionGroup oldScg, Map<String, Object> scgProps);

	public SpecimenCollectionGroup updateReports(SpecimenCollectionGroup oldScg, ScgReportDetail detail);

}
