package com.krishagni.catissueplus.core.administrative.services.impl;

import com.krishagni.catissueplus.core.administrative.events.DistributionOrderDetail;
import com.krishagni.catissueplus.core.administrative.services.DistributionOrderService;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.importer.events.ImportObjectDetail;
import com.krishagni.catissueplus.core.importer.services.ObjectImporter;

public class DistributionOrderImporter implements ObjectImporter<DistributionOrderDetail, DistributionOrderDetail> {
	private DistributionOrderService orderSvc;

	public void setOrderSvc(DistributionOrderService orderSvc) {
		this.orderSvc = orderSvc;
	}

	@Override
	@PlusTransactional
	public ResponseEvent<DistributionOrderDetail> importObject(RequestEvent<ImportObjectDetail<DistributionOrderDetail>> req) {
		try {
			ImportObjectDetail<DistributionOrderDetail> detail = req.getPayload();
			RequestEvent<DistributionOrderDetail> orderReq = new RequestEvent<>(detail.getObject());

			ResponseEvent<DistributionOrderDetail> resp = null;
			if (detail.isCreate()) {
				resp = orderSvc.createOrder(orderReq);
			} else {
				resp = orderSvc.updateOrder(orderReq);
			}

			resp.throwErrorIfUnsuccessful();
			return resp;
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
}
