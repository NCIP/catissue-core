
package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.List;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class AllSpecimenCollGroupsSummaryEvent extends ResponseEvent {

	private List<SpecimenCollectionGroupInfo> scgList;

	public List<SpecimenCollectionGroupInfo> getScgList() {
		return scgList;
	}

	public void setScgList(List<SpecimenCollectionGroupInfo> scgList) {
		this.scgList = scgList;
	}

	public static AllSpecimenCollGroupsSummaryEvent ok(List<SpecimenCollectionGroupInfo> scgList) {
		AllSpecimenCollGroupsSummaryEvent resp = new AllSpecimenCollGroupsSummaryEvent();
		resp.setScgList(scgList);
		resp.setStatus(EventStatus.OK);
		return resp;
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
