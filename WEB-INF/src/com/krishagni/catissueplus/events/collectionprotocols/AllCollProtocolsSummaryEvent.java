
package com.krishagni.catissueplus.events.collectionprotocols;

import java.util.List;

import com.krishagni.catissueplus.events.EventStatus;
import com.krishagni.catissueplus.events.ResponseEvent;

public class AllCollProtocolsSummaryEvent extends ResponseEvent {

	private List<CollectionProtocolInfo> collectionProtocolsInfo;

	public List<CollectionProtocolInfo> getCollectionProtocolsInfo() {
		return collectionProtocolsInfo;
	}

	public void setCollectionProtocolsInfo(List<CollectionProtocolInfo> collectionProtocolsInfo) {
		this.collectionProtocolsInfo = collectionProtocolsInfo;
	}

	public static AllCollProtocolsSummaryEvent ok(List<CollectionProtocolInfo> collectionProtocolInfos) {
		AllCollProtocolsSummaryEvent protocolsSummaryEvent = new AllCollProtocolsSummaryEvent();
		protocolsSummaryEvent.setCollectionProtocolsInfo(collectionProtocolInfos);
		protocolsSummaryEvent.setStatus(EventStatus.OK);
		return protocolsSummaryEvent;
	}
	
	public static AllCollProtocolsSummaryEvent serverError(Throwable ... t) {
		Throwable t1 = t != null && t.length > 0 ? t[0] : null;
		AllCollProtocolsSummaryEvent resp = new AllCollProtocolsSummaryEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setException(t1);
		resp.setMessage(t1 != null ? t1.getMessage() : null);
		return resp;		
	}
}
