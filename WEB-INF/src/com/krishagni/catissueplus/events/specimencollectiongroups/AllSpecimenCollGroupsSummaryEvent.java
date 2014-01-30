
package com.krishagni.catissueplus.events.specimencollectiongroups;

import java.util.List;

import com.krishagni.catissueplus.events.EventStatus;
import com.krishagni.catissueplus.events.ResponseEvent;

public class AllSpecimenCollGroupsSummaryEvent extends ResponseEvent {

	private List<SpecimenCollectionGroupInfo> specimenCollectionGroupsInfo;

	public List<SpecimenCollectionGroupInfo> getSpecimenCollectionGroupsInfo() {
		return specimenCollectionGroupsInfo;
	}

	public void setSpecimenCollectionGroupsInfo(List<SpecimenCollectionGroupInfo> specimenCollectionGroupsInfo) {
		this.specimenCollectionGroupsInfo = specimenCollectionGroupsInfo;
	}

	public static AllSpecimenCollGroupsSummaryEvent ok(List<SpecimenCollectionGroupInfo> specimenCollectionGroupsInfo) {
		AllSpecimenCollGroupsSummaryEvent specimenCollGroupsSummaryEvent = new AllSpecimenCollGroupsSummaryEvent();
		specimenCollGroupsSummaryEvent.setSpecimenCollectionGroupsInfo(specimenCollectionGroupsInfo);
		specimenCollGroupsSummaryEvent.setStatus(EventStatus.OK);
		return specimenCollGroupsSummaryEvent;
	}

	public static AllSpecimenCollGroupsSummaryEvent serverError(Throwable... t) {
		Throwable t1 = t != null && t.length > 0 ? t[0] : null;
		AllSpecimenCollGroupsSummaryEvent resp = new AllSpecimenCollGroupsSummaryEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setException(t1);
		resp.setMessage(t1 != null ? t1.getMessage() : null);
		return resp;
	}
}
