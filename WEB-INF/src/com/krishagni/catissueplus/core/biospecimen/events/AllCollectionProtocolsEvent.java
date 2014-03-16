	
package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.List;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class AllCollectionProtocolsEvent extends ResponseEvent {

	private List<CollectionProtocolSummary> cpList;

	public List<CollectionProtocolSummary> getCpList() {
		return cpList;
	}

	public void setCpList(List<CollectionProtocolSummary> cpList) {
		this.cpList = cpList;
	}

	public static AllCollectionProtocolsEvent ok(List<CollectionProtocolSummary> cpList) {
		AllCollectionProtocolsEvent resp = new AllCollectionProtocolsEvent();
		resp.setCpList(cpList);
		resp.setStatus(EventStatus.OK);
		return resp;
	}
	
	public static AllCollectionProtocolsEvent serverError(Throwable ... t) {
		Throwable t1 = t != null && t.length > 0 ? t[0] : null;
		AllCollectionProtocolsEvent resp = new AllCollectionProtocolsEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setException(t1);
		resp.setMessage(t1 != null ? t1.getMessage() : null);
		return resp;		
	}
}
