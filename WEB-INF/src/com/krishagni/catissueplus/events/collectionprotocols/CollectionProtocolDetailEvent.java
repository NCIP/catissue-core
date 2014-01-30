
package com.krishagni.catissueplus.events.collectionprotocols;

import com.krishagni.catissueplus.events.EventStatus;
import com.krishagni.catissueplus.events.ResponseEvent;

public class CollectionProtocolDetailEvent extends ResponseEvent {

	private CollectionProtocolDetail collectionProtocolDetail;

	public CollectionProtocolDetail getCollectionProtocolDetail() {
		return collectionProtocolDetail;
	}

	public void setCollectionProtocolDetail(CollectionProtocolDetail collectionProtocolDetail) {
		this.collectionProtocolDetail = collectionProtocolDetail;
	}

	public static CollectionProtocolDetailEvent ok(CollectionProtocolDetail collectionProtocolDetail) {
		CollectionProtocolDetailEvent collectionProtocolDetailEvent = new CollectionProtocolDetailEvent();
		collectionProtocolDetailEvent.setCollectionProtocolDetail(collectionProtocolDetail);
		collectionProtocolDetailEvent.setStatus(EventStatus.OK);
		return collectionProtocolDetailEvent;
	}
	
	public static CollectionProtocolDetailEvent serverError(Throwable ... t) {
		Throwable t1 = t != null && t.length > 0 ? t[0] : null;
		CollectionProtocolDetailEvent resp = new CollectionProtocolDetailEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setException(t1);
		resp.setMessage(t1 != null ? t1.getMessage() : null);
		return resp;		
	}
}
