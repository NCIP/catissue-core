package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.List;

import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenList;
import com.krishagni.catissueplus.core.common.events.UserSummary;

public class SpecimenListDetails extends SpecimenListSummary {
	private List<UserSummary> sharedWith;
	
	private List<SpecimenSummary> specimens;

	public List<UserSummary> getSharedWith() {
		return sharedWith;
	}

	public void setSharedWith(List<UserSummary> sharedWith) {
		this.sharedWith = sharedWith;
	}

	public List<SpecimenSummary> getSpecimens() {
		return specimens;
	}

	public void setSpecimens(List<SpecimenSummary> specimens) {
		this.specimens = specimens;
	}
	
	public static SpecimenListDetails from(SpecimenList list) {
		SpecimenListDetails details = new SpecimenListDetails();
		details.setId(list.getId());
		details.setName(list.getName());
		details.setOwner(UserSummary.fromUser(list.getOwner()));
		details.setSharedWith(UserSummary.fromUsers(list.getSharedWith()));
		details.setSpecimens(SpecimenSummary.from(list.getSpecimens()));
		return details;
	}
}
