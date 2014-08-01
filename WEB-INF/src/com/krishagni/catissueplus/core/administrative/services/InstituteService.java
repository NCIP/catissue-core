package com.krishagni.catissueplus.core.administrative.services;

import com.krishagni.catissueplus.core.administrative.events.CreateInstituteEvent;
import com.krishagni.catissueplus.core.administrative.events.DisableInstituteEvent;
import com.krishagni.catissueplus.core.administrative.events.GetAllInstitutesEvent;
import com.krishagni.catissueplus.core.administrative.events.GetInstituteEvent;
import com.krishagni.catissueplus.core.administrative.events.InstituteCreatedEvent;
import com.krishagni.catissueplus.core.administrative.events.InstituteDisabledEvent;
import com.krishagni.catissueplus.core.administrative.events.InstituteGotEvent;
import com.krishagni.catissueplus.core.administrative.events.InstituteUpdatedEvent;
import com.krishagni.catissueplus.core.administrative.events.ReqAllInstitutesEvent;
import com.krishagni.catissueplus.core.administrative.events.UpdateInstituteEvent;

public interface InstituteService {

	public InstituteCreatedEvent createInstitute(CreateInstituteEvent event);

	public InstituteUpdatedEvent updateInstitute(UpdateInstituteEvent event);

	public InstituteDisabledEvent deleteInstitute(DisableInstituteEvent event);

	public InstituteGotEvent getInstitute(GetInstituteEvent event);

	public GetAllInstitutesEvent getInstitutes(ReqAllInstitutesEvent event);
}
