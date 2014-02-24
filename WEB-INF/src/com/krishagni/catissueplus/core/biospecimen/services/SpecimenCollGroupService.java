
package com.krishagni.catissueplus.core.biospecimen.services;

import com.krishagni.catissueplus.core.biospecimen.events.AllSpecimensSummaryEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqSpecimenSummaryEvent;

public interface SpecimenCollGroupService {

	public AllSpecimensSummaryEvent getSpecimensList(ReqSpecimenSummaryEvent event);
}
