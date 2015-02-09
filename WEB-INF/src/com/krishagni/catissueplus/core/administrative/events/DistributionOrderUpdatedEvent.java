package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.common.errors.CatissueErrorCode;
import com.krishagni.catissueplus.core.common.errors.ErroneousField;
import com.krishagni.catissueplus.core.common.errors.ObjectCreationException;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class DistributionOrderUpdatedEvent extends ResponseEvent {
	private Long distributionOrderId;
	
	private DistributionOrderDetail detail;
		
	public Long getDistributionOrderId() {
		return distributionOrderId;
	}

	public void setDistributionOrderId(Long distributionOrderId) {
		this.distributionOrderId = distributionOrderId;
	}

	public DistributionOrderDetail getDetail() {
		return detail;
	}

	public void setDetail(DistributionOrderDetail detail) {
		this.detail = detail;
	}

	public static DistributionOrderUpdatedEvent ok(DistributionOrderDetail distributionOrder) {
		DistributionOrderUpdatedEvent resp = new DistributionOrderUpdatedEvent();
		resp.setDetail(distributionOrder);
		resp.setStatus(EventStatus.OK);
		return resp;
	}
	
	public static DistributionOrderUpdatedEvent badRequest(CatissueErrorCode error, String field) {
		DistributionOrderUpdatedEvent resp = new DistributionOrderUpdatedEvent();
		resp.setErroneousFields(new ErroneousField[] { new ErroneousField(error,field)});
		resp.setStatus(EventStatus.BAD_REQUEST);
		return resp;
	}
	
	public static DistributionOrderUpdatedEvent notFound(Long distributionOrderId) {
		DistributionOrderUpdatedEvent resp = new DistributionOrderUpdatedEvent();
		resp.setDistributionOrderId(distributionOrderId);
		resp.setStatus(EventStatus.BAD_REQUEST);
		return resp;
	}

	public static DistributionOrderUpdatedEvent badRequest(Exception e) {
		DistributionOrderUpdatedEvent resp = new DistributionOrderUpdatedEvent();
		resp.setStatus(EventStatus.BAD_REQUEST);
		resp.setException(e);
		resp.setMessage(e.getMessage());
		if (e instanceof ObjectCreationException) {
			resp.setErroneousFields(((ObjectCreationException)e).getErroneousFields());
		}
		
		return resp;
	}	
	
	public static DistributionOrderUpdatedEvent serverError(Exception e) {
		DistributionOrderUpdatedEvent resp = new DistributionOrderUpdatedEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setException(e);
		resp.setMessage(e.getMessage());
		return resp;		
	}
}
