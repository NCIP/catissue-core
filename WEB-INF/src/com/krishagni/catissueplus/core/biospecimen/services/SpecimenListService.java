package com.krishagni.catissueplus.core.biospecimen.services;

import com.krishagni.catissueplus.core.biospecimen.events.CreateSpecimenListEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ListSpecimensEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ListSpecimensUpdatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqListSpecimensEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqSpecimenListDetailEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ShareSpecimenListEvent;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenListCreatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenListDetailEvent;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenListSharedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenListUpdatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenListsEvent;
import com.krishagni.catissueplus.core.biospecimen.events.UpdateListSpecimensEvent;
import com.krishagni.catissueplus.core.biospecimen.events.UpdateSpecimenListEvent;
import com.krishagni.catissueplus.core.common.events.RequestEvent;

public interface SpecimenListService {
	public SpecimenListsEvent getUserSpecimenLists(RequestEvent req);
	
	public SpecimenListDetailEvent getSpecimenList(ReqSpecimenListDetailEvent req);
	
	public SpecimenListCreatedEvent createSpecimenList(CreateSpecimenListEvent req);
	
	public SpecimenListUpdatedEvent updateSpecimenList(UpdateSpecimenListEvent req);
	
	public ListSpecimensEvent getListSpecimens(ReqListSpecimensEvent req);
	
	public ListSpecimensUpdatedEvent updateListSpecimens(UpdateListSpecimensEvent req);
	
	public SpecimenListSharedEvent shareSpecimenList(ShareSpecimenListEvent req);
}
