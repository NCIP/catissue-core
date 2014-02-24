
package com.krishagni.catissueplus.core.biospecimen.services;

import com.krishagni.catissueplus.events.specimens.AllSpecimensSummaryEvent;
import com.krishagni.catissueplus.events.specimens.ReqSpecimenSummaryEvent;

public interface SpecimenCollGroupService {

	public AllSpecimensSummaryEvent getSpecimensList(ReqSpecimenSummaryEvent event);
}
