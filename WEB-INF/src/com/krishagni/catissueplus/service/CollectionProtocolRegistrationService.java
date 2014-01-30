
package com.krishagni.catissueplus.service;

import com.krishagni.catissueplus.events.specimencollectiongroups.AllSpecimenCollGroupsSummaryEvent;
import com.krishagni.catissueplus.events.specimencollectiongroups.ReqSpecimenCollGroupSummaryEvent;

public interface CollectionProtocolRegistrationService {

	public AllSpecimenCollGroupsSummaryEvent getSpecimenCollGroupsList(
			ReqSpecimenCollGroupSummaryEvent reqSpecimenCollGroupSummaryEvent);
}
