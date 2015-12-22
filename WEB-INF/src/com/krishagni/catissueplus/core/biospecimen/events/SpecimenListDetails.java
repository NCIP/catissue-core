package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.Collection;
import java.util.List;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenList;
import com.krishagni.catissueplus.core.common.ListenAttributeChanges;
import com.krishagni.catissueplus.core.common.events.UserSummary;

@ListenAttributeChanges
public class SpecimenListDetails extends SpecimenListSummary {
	private List<UserSummary> sharedWith;
	
	private List<SpecimenDetail> specimens;

	public List<UserSummary> getSharedWith() {
		return sharedWith;
	}

	public void setSharedWith(List<UserSummary> sharedWith) {
		this.sharedWith = sharedWith;
	}

	public List<SpecimenDetail> getSpecimens() {
		return specimens;
	}

	public void setSpecimens(List<SpecimenDetail> specimens) {
		this.specimens = specimens;
	}
	
	public static SpecimenListDetails from(SpecimenList list) {
		return from(list, list.getSpecimens());
	}
	
	public static SpecimenListDetails from(SpecimenList list, Collection<Specimen> specimens) {
		SpecimenListDetails details = new SpecimenListDetails();
		details.setId(list.getId());
		details.setName(list.getName());
		details.setDescription(list.getDescription());
		details.setOwner(UserSummary.from(list.getOwner()));
		details.setSharedWith(UserSummary.from(list.getSharedWith()));
		details.setSpecimens(SpecimenDetail.from(specimens));
		details.setDefaultList(list.isDefaultList());
		return details;
		
	}
}
