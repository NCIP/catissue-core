
package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.List;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class AllCollectionGroupsDetailEvent extends ResponseEvent {

	private List<ScgDetail> collectionGroupsDetail;

	private Long count;

	public List<ScgDetail> getCollectionGroupsDetail() {
		return collectionGroupsDetail;
	}

	public void setCollectionGroupsDetail(List<ScgDetail> collectionGroupsDetail) {
		this.collectionGroupsDetail = collectionGroupsDetail;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}
	
	public static AllCollectionGroupsDetailEvent ok(List<ScgDetail> scgsDetail, Long count) {
		AllCollectionGroupsDetailEvent event = new AllCollectionGroupsDetailEvent();
		event.setCollectionGroupsDetail(scgsDetail);
		event.setCount(count);
		event.setStatus(EventStatus.OK);
		return event;
	}

	public static AllCollectionGroupsDetailEvent serverError(Throwable... t) {
		Throwable t1 = t != null && t.length > 0 ? t[0] : null;
		AllCollectionGroupsDetailEvent resp = new AllCollectionGroupsDetailEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setException(t1);
		resp.setMessage(t1 != null ? t1.getMessage() : null);
		return resp;
	}

	public static AllCollectionGroupsDetailEvent badRequest(String msg, Throwable t) {
		AllCollectionGroupsDetailEvent resp = new AllCollectionGroupsDetailEvent();
		resp.setStatus(EventStatus.BAD_REQUEST);
		resp.setMessage(msg);
		resp.setException(t);
		return resp;
	}

}
